package me.cookie.abyssclient.cosmetics

import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.PlayerModelPart
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.Items
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f


class CapeRenderer(featureRendererContext: FeatureRendererContext<AbstractClientPlayerEntity?, PlayerEntityModel<AbstractClientPlayerEntity?>?>?) :
    FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity?>?>(featureRendererContext) {

    override fun render(
        matrices: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        i: Int,
        player: AbstractClientPlayerEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        if (player.canRenderCapeTexture() && !player.isInvisible && player.isPartVisible(
                PlayerModelPart.CAPE
            ) && Cosmetic.hasCape(player)
        ) {
            val itemStack = player.getEquippedStack(EquipmentSlot.CHEST)
            if (!itemStack.isOf(Items.ELYTRA)) {
                matrices.push()
                matrices.translate(0.0, 0.0, 0.125)
                val d = MathHelper.lerp(
                    h.toDouble(),
                    player.prevCapeX,
                    player.capeX
                ) - MathHelper.lerp(h.toDouble(), player.prevX, player.x)
                val e = MathHelper.lerp(
                    h.toDouble(),
                    player.prevCapeY,
                    player.capeY
                ) - MathHelper.lerp(h.toDouble(), player.prevY, player.y)
                val m = MathHelper.lerp(
                    h.toDouble(),
                    player.prevCapeZ,
                    player.capeZ
                ) - MathHelper.lerp(h.toDouble(), player.prevZ, player.z)
                val n =
                    player.prevBodyYaw + (player.bodyYaw - player.prevBodyYaw)
                val o = MathHelper.sin(n * 0.017453292f).toDouble()
                val p = (-MathHelper.cos(n * 0.017453292f)).toDouble()
                var q = e.toFloat() * 10.0f
                q = MathHelper.clamp(q, -6.0f, 32.0f)
                var r = (d * o + m * p).toFloat() * 100.0f
                r = MathHelper.clamp(r, 0.0f, 150.0f)
                var s = (d * p - m * o).toFloat() * 100.0f
                s = MathHelper.clamp(s, -20.0f, 20.0f)
                if (r < 0.0f) {
                    r = 0.0f
                }
                val t = MathHelper.lerp(
                    h,
                    player.prevStrideDistance,
                    player.strideDistance
                )
                q += MathHelper.sin(
                    MathHelper.lerp(
                        h,
                        player.prevHorizontalSpeed,
                        player.horizontalSpeed
                    ) * 6.0f
                ) * 32.0f * t
                if (player.isInSneakingPose) {
                    q += 25.0f
                }
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0f + r / 2.0f + q))
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(s / 2.0f))
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - s / 2.0f))
                val vertexConsumer =
                    vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(Cosmetic.getCapeTexture(player)!!))
                (this.contextModel as PlayerEntityModel<*>?)!!.renderCape(
                    matrices,
                    vertexConsumer,
                    i,
                    OverlayTexture.DEFAULT_UV
                )
                matrices.pop()
            }
        }
    }
}