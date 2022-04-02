package de.vincentschweiger.phantomclient.cosmetics.cape

import de.vincentschweiger.phantomclient.cosmetics.CosmeticUtils.checkClientUser
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f

class CapeRenderer(featureRendererContext: FeatureRendererContext<AbstractClientPlayerEntity?, PlayerEntityModel<AbstractClientPlayerEntity?>?>?) : FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity?>?>(featureRendererContext) {
    private var capeRotateX = 0f
    private var capeRotateY = 0f
    private var capeRotateZ = 0f
    private val texture = Identifier("phantom", "textures/cape.png")


    override fun render(pMatrixStack: MatrixStack, pBuffer: VertexConsumerProvider, pPackedLight: Int, pLivingEntity: AbstractClientPlayerEntity, pLimbSwing: Float, pLimbSwingAmount: Float, pPartialTicks: Float, pAgeInTicks: Float, pNetHeadYaw: Float, pHeadPitch: Float) {
        if (!checkClientUser(pLivingEntity)) return
        pMatrixStack.push()
        pMatrixStack.translate(0.0, 0.0, 0.125)
        val d0 = MathHelper.lerp(pPartialTicks.toDouble(), pLivingEntity.prevCapeX, pLivingEntity.capeX) - MathHelper.lerp(pPartialTicks.toDouble(), pLivingEntity.prevX, pLivingEntity.x)
        val d1 = MathHelper.lerp(pPartialTicks.toDouble(), pLivingEntity.prevCapeY, pLivingEntity.capeY) - MathHelper.lerp(pPartialTicks.toDouble(), pLivingEntity.prevY, pLivingEntity.y)
        val d2 = MathHelper.lerp(pPartialTicks.toDouble(), pLivingEntity.prevCapeZ, pLivingEntity.capeZ) - MathHelper.lerp(pPartialTicks.toDouble(), pLivingEntity.prevZ, pLivingEntity.z)
        val f = pLivingEntity.prevBodyYaw + (pLivingEntity.bodyYaw - pLivingEntity.prevBodyYaw)
        val d3 = MathHelper.sin(f * (Math.PI.toFloat() / 180f)).toDouble()
        val d4 = -MathHelper.cos(f * (Math.PI.toFloat() / 180f)).toDouble()
        var f1 = d1.toFloat() * 10.0f
        f1 = MathHelper.clamp(f1, -6.0f, 32.0f)
        var f2 = (d0 * d3 + d2 * d4).toFloat() * 100.0f
        f2 = MathHelper.clamp(f2, 0.0f, 150.0f)
        var f3 = (d0 * d4 - d2 * d3).toFloat() * 100.0f
        f3 = MathHelper.clamp(f3, -20.0f, 20.0f)
        if (f2 < 0.0f) {
            f2 = 0.0f
        }
        if (f2 > 165.0f) {
            f2 = 165.0f
        }
        if (f1 < -5.0f) {
            f1 = -5.0f
        }
        val f4 = MathHelper.lerp(pPartialTicks, pLivingEntity.prevStrideDistance, pLivingEntity.strideDistance)
        f1 += MathHelper.sin(MathHelper.lerp(pPartialTicks, pLivingEntity.prevHorizontalSpeed, pLivingEntity.horizontalSpeed) * 6.0f) * 32.0f * f4
        if (pLivingEntity.isSneaking) {
            f1 += 25.0f
        }
        var f5 = StutterFix.averageFrameTimeSec * 20.0f
        f5 = StutterFix.limit(f5, 0.02f, 1.0f)
        capeRotateX = MathHelper.lerp(f5, capeRotateX, 6.0f + f2 / 2.0f + f1)
        capeRotateZ = MathHelper.lerp(f5, capeRotateZ, f3 / 2.0f)
        capeRotateY = MathHelper.lerp(f5, capeRotateY, 180.0f - f3 / 2.0f)
        pMatrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(capeRotateX))
        pMatrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(capeRotateZ))
        pMatrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(capeRotateY))
        val vertexconsumer = pBuffer.getBuffer(RenderLayer.getEntitySolid(texture))
        val elytraEquipped = pLivingEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
        if (useAsElytra) {
            if (elytraEquipped) this.contextModel!!.renderCape(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.DEFAULT_UV)
        } else {
            if (!elytraEquipped) this.contextModel!!.renderCape(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.DEFAULT_UV)
        }
        pMatrixStack.pop()
    }

    companion object {
        var useAsElytra = false
    }
}
