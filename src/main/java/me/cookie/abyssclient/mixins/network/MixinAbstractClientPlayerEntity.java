package me.cookie.abyssclient.mixins.network;

import com.mojang.authlib.GameProfile;
import me.cookie.abyssclient.cosmetics.Cosmetic;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class MixinAbstractClientPlayerEntity extends PlayerEntity {
    public MixinAbstractClientPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(at = @At("HEAD"), method = "getCapeTexture", cancellable = true)
    public void hookGetCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        Identifier texture = Cosmetic.INSTANCE.getCapeTexture((AbstractClientPlayerEntity) (Object) this);
        if (texture != null) cir.setReturnValue(texture);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
