package de.vincentschweiger.phantomclient.cosmetics.wings.dragonwings

import de.vincentschweiger.phantomclient.cosmetics.CosmeticUtils.checkClientUser
import de.vincentschweiger.phantomclient.mixins.InvokerEntityModelLayers
import de.vincentschweiger.phantomclient.socket.ServerInfoProvider.hasCosmetic
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Items
import net.minecraft.util.Identifier


class DragonWingsRenderer<T : LivingEntity?, M : EntityModel<T>?>(context: FeatureRendererContext<T, M>?, loader: EntityModelLoader) : FeatureRenderer<T, M>(context) {
    private val model: DragonWingsModel<T>

    init {
        model = DragonWingsModel(loader.getModelPart(LAYER))
    }

    override fun render(matrixStackIn: MatrixStack, bufferIn: VertexConsumerProvider, packedLightIn: Int, entity: T, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, headYaw: Float, headPitch: Float) {
        if (entity is ClientPlayerEntity
                && checkClientUser(entity)
                && hasCosmetic(
                        entity.gameProfile.id,
                        "wings"
                )) {
            val elytraEquipped = entity.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
            if (useAsElytra) {
                if (elytraEquipped) {
                    if (entity.getUuid() == MinecraftClient.getInstance().session.profile.id) {
                        val player = entity as ClientPlayerEntity
                        matrixStackIn.push()
                        val builder = bufferIn.getBuffer(RenderLayer.getArmorCutoutNoCull(texture))
                        model.render(player, matrixStackIn, builder, packedLightIn, OverlayTexture.DEFAULT_UV, partialTicks)
                        matrixStackIn.pop()
                    }
                }
            } else {
                if (!elytraEquipped) {
                    if (entity.getUuid() == MinecraftClient.getInstance().session.profile.id) {
                        matrixStackIn.push()
                        val builder = bufferIn.getBuffer(RenderLayer.getArmorCutoutNoCull(texture))
                        model.render(entity, matrixStackIn, builder, packedLightIn, OverlayTexture.DEFAULT_UV, partialTicks)
                        matrixStackIn.pop()
                    }
                }
            }
        }
    }

    companion object {
        var LAYER: EntityModelLayer = InvokerEntityModelLayers.invokeRegisterMain("phantom_dragonwings")
        var anglePitch = -65.0
        var angleYaw = 45.0
        var angleRoll = 20.0
        var useAsElytra = false
        var scale = 70.0
        var texture = Identifier("phantom", "textures/dragonwings.png")
    }
}