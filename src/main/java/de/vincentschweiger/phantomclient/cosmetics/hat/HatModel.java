package de.vincentschweiger.phantomclient.cosmetics.hat;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class HatModel<T extends LivingEntity> extends AnimalModel<T> {
    public ModelPart part;

    public HatModel(ModelPart root) {
        this.part = root.getChild(EntityModelPartNames.HAT);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -9.0F, -5.0F, 10.0F, 1.0F, 10.0F, Dilation.NONE).uv(0, 12).cuboid(-3.0F, -16.0F, -3.0F, 6.0F, 7.0F, 6.0F, Dilation.NONE), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public void render(MatrixStack matrixStack, AbstractClientPlayerEntity entity, VertexConsumer buffer, int packedLight, int packedOverlay) {
        part.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(part);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}