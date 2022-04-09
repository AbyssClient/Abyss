package me.cookie.abyssclient.mixins.client;

import me.cookie.abyssclient.event.EventManager;
import me.cookie.abyssclient.event.KeyEvent;
import me.cookie.abyssclient.event.KeyboardCharEvent;
import me.cookie.abyssclient.event.KeyboardKeyEvent;
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
        EventManager.INSTANCE.callEvent(new KeyboardCharEvent(window, i));
    }

    /**
     * Hook key event
     */
    @Inject(method = "onKey", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", shift = At.Shift.BEFORE, ordinal = 0))
    private void hookKeyboardKey(long window, int key, int scancode, int i, int j, CallbackInfo callback) {
        EventManager.INSTANCE.callEvent(new KeyboardKeyEvent(window, key, scancode, i, j));
    }

    /**
     * Hook key event
     */
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/InputUtil;fromKeyCode(II)Lnet/minecraft/client/util/InputUtil$Key;", shift = At.Shift.AFTER))
    private void hookKey(long window, int key, int scancode, int i, int j, CallbackInfo callback) {
        EventManager.INSTANCE.callEvent(new KeyEvent(InputUtil.fromKeyCode(key, scancode), i, j));
    }
}
