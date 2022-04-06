package de.vincentschweiger.phantomclient.mixins.gui;

import de.vincentschweiger.phantomclient.event.ChatSendEvent;
import de.vincentschweiger.phantomclient.event.EventManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Screen.class)
public abstract class MixinScreen {

    @Shadow
    @Nullable
    protected MinecraftClient client;

    @Shadow
    public abstract void sendMessage(String message);

    /**
     * Handle user chat messages
     *
     * @param message      chat message by client user
     * @param callbackInfo callback
     */
    @Inject(method = "sendMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    private void handleChatMessage(String message, CallbackInfo callbackInfo) {
        ChatSendEvent chatSendEvent = new ChatSendEvent(message);
        EventManager.INSTANCE.callEvent(chatSendEvent);
        if (chatSendEvent.isCancelled()) {
            client.inGameHud.getChatHud().addToMessageHistory(message);
            callbackInfo.cancel();
        }
    }

}
