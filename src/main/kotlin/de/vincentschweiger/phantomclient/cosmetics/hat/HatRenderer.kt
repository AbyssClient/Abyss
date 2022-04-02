package de.vincentschweiger.phantomclient.cosmetics.hat

import de.vincentschweiger.phantomclient.cosmetics.CosmeticUtils
import de.vincentschweiger.phantomclient.socket.ServerInfoProvider
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier

class HatRenderer<T : LivingEntity, M : PlayerEntityModel<T>>(context: FeatureRendererContext<T, M>?, loader: EntityModelLoader) : FeatureRenderer<T, M>(context) {
    private val model: HatModel<T>

    init {
        model = HatModel(loader.getModelPart(LAYER))
    }

    override fun render(matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int, entity: T, limbAngle: Float, limbDistance: Float, tickDelta: Float, animationProgress: Float, headYaw: Float, headPitch: Float) {
        if (entity is ClientPlayerEntity) {
            if (CosmeticUtils.checkClientUser(entity) && ServerInfoProvider.hasCosmetic(entity.gameProfile.id, "hat")) {
                matrices?.push()
                val builder = vertexConsumers?.getBuffer(RenderLayer.getEntityCutoutNoCull(texture))
                val model = this.model
                if (!entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty) {
                    matrices?.translate(0.0, -0.07, 0.0)
                    model.part.pivotY = 26.0F
                }
                model.part.copyTransform(this.contextModel.getHead())
                model.render(matrices, entity, builder, light, OverlayTexture.getUv(0.0f, false))
                matrices?.pop()
            }
        }
    }

    companion object {
        var LAYER = EntityModelLayer(Identifier("phantom"), "hat")
        var texture = Identifier("phantom", "textures/hat.png")
    }
}