package de.vincentschweiger.phantomclient.cosmetics.dragonwings;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.model.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.opengl.GL11;

public class DragonwingsModel<T extends LivingEntity> extends AnimalModel<T> {


    private final ModelPart leftWing;
    private final ModelPart leftWingTip;
    private final ModelPart rightWing;
    private final ModelPart rightWingTip;

    public DragonwingsModel(ModelPart root) {
        this.leftWing = root.getChild(EntityModelPartNames.LEFT_WING);
        this.leftWingTip = leftWing.getChild(EntityModelPartNames.LEFT_WING_TIP);
        this.rightWing = root.getChild(EntityModelPartNames.RIGHT_WING);
        this.rightWingTip = rightWing.getChild(EntityModelPartNames.RIGHT_WING_TIP);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartLeftWing = modelPartData.addChild(EntityModelPartNames.LEFT_WING, ModelPartBuilder.create().mirrored().cuboid("bone", 0.0F, -1.0F, -1.0F, 10, 2, 2, Dilation.NONE, 0, 0).cuboid("skin", 0.0F, 0.0F, 0.5F, 10, 0, 10, Dilation.NONE, -10, 8), ModelTransform.pivot(1.0F, 0.0F, 1.0F));
        modelPartLeftWing.addChild(EntityModelPartNames.LEFT_WING_TIP, ModelPartBuilder.create().mirrored().cuboid("bone", 0.0F, -0.5F, -0.5F, 10, 1, 1, Dilation.NONE, 0, 5).cuboid("skin", 0.0F, 0.0F, 0.5F, 10, 0, 10, Dilation.NONE, -10, 18), ModelTransform.pivot(10.0F, 0.0F, 0.0F));
        ModelPartData modelPartRightWing = modelPartData.addChild(EntityModelPartNames.RIGHT_WING, ModelPartBuilder.create().cuboid("bone", -10.0F, -1.0F, -1.0F, 10, 2, 2, Dilation.NONE, 0, 0).cuboid("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10, Dilation.NONE, -10, 8), ModelTransform.pivot(-1.0F, 0.0F, 1.0F));
        modelPartRightWing.addChild(EntityModelPartNames.RIGHT_WING_TIP, ModelPartBuilder.create().cuboid("bone", -10.0F, -0.5F, -0.5F, 10, 1, 1, Dilation.NONE, 0, 5).cuboid("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10, Dilation.NONE, -10, 18), ModelTransform.pivot(-10.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 30, 30);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.leftWing, this.rightWing, this.leftWingTip, this.rightWingTip);
    }

    public void render(ClientPlayerEntity player, MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float partialTicks) {
        if (player != null) {
            matrixStackIn.push();
            matrixStackIn.scale((float) (0.01D * DragonwingsRenderer.scale), (float) (0.01D * DragonwingsRenderer.scale), (float) (0.01D * DragonwingsRenderer.scale));
            //matrixStackIn.translate(0.0D, 0.0D, 0.2D / 30.0D);
            if (player.isInSneakingPose()) {
                this.rightWing.pivotY = 5.3F;
                this.leftWing.pivotY = 5.3F;
            } else {
                this.rightWing.pivotY = 2.4F;
                this.leftWing.pivotY = 2.4F;
            }
            this.leftWing.pivotZ = 2;
            this.rightWing.pivotZ = 2;
            RenderSystem.setShaderTexture(0, DragonwingsRenderer.texture);
            float f = (float) (System.currentTimeMillis() % 1000L) / 1000.0F * 3.1415927F * 2.0F;
            GL11.glEnable(2884);
            this.rightWing.pitch = (float) Math.toRadians(DragonwingsRenderer.anglePitch) - (float) Math.cos(f) * 0.2F;
            this.rightWing.yaw = (float) Math.toRadians(DragonwingsRenderer.angleYaw) + (float) Math.sin(f) * 0.4F;
            this.rightWing.roll = (float) Math.toRadians(DragonwingsRenderer.angleRoll);
            this.rightWingTip.roll = -((float) (Math.sin((double) (f + 2.0F)) + 0.5D)) * 0.75F;
            this.leftWing.pitch = this.rightWing.pitch;
            this.leftWing.yaw = -this.rightWing.yaw;
            this.leftWing.roll = -this.rightWing.roll;
            this.leftWingTip.roll = -this.rightWingTip.roll;
            this.leftWing.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
            this.rightWing.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
            matrixStackIn.pop();
        }
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }
}