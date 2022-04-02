package de.vincentschweiger.phantomclient.mixins;

import de.vincentschweiger.phantomclient.events.impl.KeyEvent;
import de.vincentschweiger.phantomclient.events.impl.KeyboardCharEvent;
import de.vincentschweiger.phantomclient.events.impl.KeyboardKeyEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    /**
     * Hook char event
     */
    @Inject(method = "onChar", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", shift = At.Shift.BEFORE))
    private void hookKeyboardChar(long window, int i, int j, CallbackInfo callback) {
        new KeyboardCharEvent(window, i).call();
    }

    /**
     * Hook key event
     */
    @Inject(method = "onKey", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", shift = At.Shift.BEFORE, ordinal = 0))
    private void hookKeyboardKey(long window, int key, int scancode, int i, int j, CallbackInfo callback) {
        new KeyboardKeyEvent(window, key, scancode, i, j).call();
    }

    /**
     * Hook key event
     */
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/InputUtil;fromKeyCode(II)Lnet/minecraft/client/util/InputUtil$Key;", shift = At.Shift.AFTER))
    private void hookKey(long window, int key, int scancode, int i, int j, CallbackInfo callback) {
        new KeyEvent(InputUtil.fromKeyCode(key, scancode), i, j).call();
    }
}
