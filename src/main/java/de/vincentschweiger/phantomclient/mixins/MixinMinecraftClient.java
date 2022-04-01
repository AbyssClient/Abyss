package de.vincentschweiger.phantomclient.mixins;

import de.vincentschweiger.phantomclient.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;instance:Lnet/minecraft/client/MinecraftClient;", shift = At.Shift.AFTER))
    public void onPreInit(RunArgs args, CallbackInfo ci) {
        Mod.getInstance().preInit();
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void onPostInit(RunArgs args, CallbackInfo ci) {
        Mod.getInstance().postInit();
    }

    @Inject(method = "close", at = @At(value = "HEAD"))
    public void onShutdown(CallbackInfo ci) {
        Mod.getInstance().shutdown();
    }


}