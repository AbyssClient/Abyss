package de.vincentschweiger.phantomclient.cosmetics.hat

import com.google.common.collect.ImmutableList
import net.minecraft.client.model.*
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.AnimalModel
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity


class HatModel<T : LivingEntity?>(root: ModelPart) : AnimalModel<T>() {
    var part: ModelPart

    init {
        part = root.getChild(EntityModelPartNames.HAT)
    }

    fun render(matrixStack: MatrixStack?, entity: AbstractClientPlayerEntity?, buffer: VertexConsumer?, packedLight: Int, packedOverlay: Int) {
        part.render(matrixStack, buffer, packedLight, packedOverlay)
    }

    override fun getHeadParts(): Iterable<ModelPart> {
        return ImmutableList.of()
    }

    override fun getBodyParts(): Iterable<ModelPart> {
        return ImmutableList.of(part)
    }

    override fun setAngles(entity: T, limbAngle: Float, limbDistance: Float, animationProgress: Float, headYaw: Float, headPitch: Float) {}

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(0, 0).cuboid(-5.0f, -9.0f, -5.0f, 10.0f, 1.0f, 10.0f, Dilation.NONE).uv(0, 12).cuboid(-3.0f, -16.0f, -3.0f, 6.0f, 7.0f, 6.0f, Dilation.NONE), ModelTransform.pivot(0.0f, 24.0f, 0.0f))
                return TexturedModelData.of(modelData, 64, 64)
            }
    }
}