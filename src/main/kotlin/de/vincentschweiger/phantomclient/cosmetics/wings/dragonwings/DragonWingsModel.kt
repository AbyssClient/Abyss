package de.vincentschweiger.phantomclient.cosmetics.wings.dragonwings

import com.google.common.collect.ImmutableList
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.model.*
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.AnimalModel
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import org.lwjgl.opengl.GL11
import kotlin.math.cos
import kotlin.math.sin


class DragonWingsModel<T : LivingEntity?>(root: ModelPart) : AnimalModel<T>() {
    private val leftWing: ModelPart
    private val leftWingTip: ModelPart
    private val rightWing: ModelPart
    private val rightWingTip: ModelPart

    init {
        leftWing = root.getChild(EntityModelPartNames.LEFT_WING)
        leftWingTip = leftWing.getChild(EntityModelPartNames.LEFT_WING_TIP)
        rightWing = root.getChild(EntityModelPartNames.RIGHT_WING)
        rightWingTip = rightWing.getChild(EntityModelPartNames.RIGHT_WING_TIP)
    }

    override fun getHeadParts(): Iterable<ModelPart> {
        return ImmutableList.of()
    }

    override fun getBodyParts(): Iterable<ModelPart> {
        return ImmutableList.of(leftWing, rightWing, leftWingTip, rightWingTip)
    }

    fun render(player: ClientPlayerEntity?, matrixStackIn: MatrixStack, bufferIn: VertexConsumer?, packedLightIn: Int, packedOverlayIn: Int, partialTicks: Float) {
        if (player != null) {
            matrixStackIn.push()
            matrixStackIn.scale((0.01 * DragonWingsRenderer.scale).toFloat(), (0.01 * DragonWingsRenderer.scale).toFloat(), (0.01 * DragonWingsRenderer.scale).toFloat())
            if (player.isInSneakingPose) {
                rightWing.pivotY = 5.3f
                leftWing.pivotY = 5.3f
            } else {
                rightWing.pivotY = 2.4f
                leftWing.pivotY = 2.4f
            }
            leftWing.pivotZ = 2f
            rightWing.pivotZ = 2f
            RenderSystem.setShaderTexture(0, DragonWingsRenderer.texture)
            val f = (System.currentTimeMillis() % 1000L).toFloat() / 1000.0f * 3.1415927f * 2.0f
            GL11.glEnable(2884)
            rightWing.pitch = Math.toRadians(DragonWingsRenderer.anglePitch).toFloat() - cos(f.toDouble()).toFloat() * 0.2f
            rightWing.yaw = Math.toRadians(DragonWingsRenderer.angleYaw).toFloat() + sin(f.toDouble()).toFloat() * 0.4f
            rightWing.roll = Math.toRadians(DragonWingsRenderer.angleRoll).toFloat()
            rightWingTip.roll = -(sin((f + 2.0f).toDouble()) + 0.5).toFloat() * 0.75f
            leftWing.pitch = rightWing.pitch
            leftWing.yaw = -rightWing.yaw
            leftWing.roll = -rightWing.roll
            leftWingTip.roll = -rightWingTip.roll
            leftWing.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn)
            rightWing.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn)
            matrixStackIn.pop()
        }
    }

    override fun setAngles(entity: T, limbAngle: Float, limbDistance: Float, animationProgress: Float, headYaw: Float, headPitch: Float) {}

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val modelPartLeftWing = modelPartData.addChild(EntityModelPartNames.LEFT_WING, ModelPartBuilder.create().mirrored().cuboid("bone", 0.0f, -1.0f, -1.0f, 10, 2, 2, Dilation.NONE, 0, 0).cuboid("skin", 0.0f, 0.0f, 0.5f, 10, 0, 10, Dilation.NONE, -10, 8), ModelTransform.pivot(1.0f, 0.0f, 1.0f))
                modelPartLeftWing.addChild(EntityModelPartNames.LEFT_WING_TIP, ModelPartBuilder.create().mirrored().cuboid("bone", 0.0f, -0.5f, -0.5f, 10, 1, 1, Dilation.NONE, 0, 5).cuboid("skin", 0.0f, 0.0f, 0.5f, 10, 0, 10, Dilation.NONE, -10, 18), ModelTransform.pivot(10.0f, 0.0f, 0.0f))
                val modelPartRightWing = modelPartData.addChild(EntityModelPartNames.RIGHT_WING, ModelPartBuilder.create().cuboid("bone", -10.0f, -1.0f, -1.0f, 10, 2, 2, Dilation.NONE, 0, 0).cuboid("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10, Dilation.NONE, -10, 8), ModelTransform.pivot(-1.0f, 0.0f, 1.0f))
                modelPartRightWing.addChild(EntityModelPartNames.RIGHT_WING_TIP, ModelPartBuilder.create().cuboid("bone", -10.0f, -0.5f, -0.5f, 10, 1, 1, Dilation.NONE, 0, 5).cuboid("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10, Dilation.NONE, -10, 18), ModelTransform.pivot(-10.0f, 0.0f, 0.0f))
                return TexturedModelData.of(modelData, 30, 30)
            }
    }
}