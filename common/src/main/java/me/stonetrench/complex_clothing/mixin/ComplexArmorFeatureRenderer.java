package me.stonetrench.complex_clothing.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.stonetrench.complex_clothing.ComplexPattern;
import me.stonetrench.complex_clothing.Complex_clothing;
import me.stonetrench.complex_clothing.items.ComplexArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(HumanoidArmorLayer.class)
public abstract class ComplexArmorFeatureRenderer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    @Shadow protected abstract boolean usesInnerModel(EquipmentSlot equipmentSlot);

    @Shadow @Final private static Map<String, ResourceLocation> ARMOR_LOCATION_CACHE;

    @Shadow protected abstract void setPartVisibility(A humanoidModel, EquipmentSlot equipmentSlot);

    public ComplexArmorFeatureRenderer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(method = "renderArmorPiece", at= @At("HEAD"), cancellable = true)
    void renderArmorParts(PoseStack poseStack, MultiBufferSource multiBufferSource, T livingEntity, EquipmentSlot equipmentSlot, int light, A humanoidModel, CallbackInfo ci){

        ItemStack itemStack = livingEntity.getItemBySlot(equipmentSlot);
        if (itemStack.getItem() instanceof ComplexArmorItem complexArmorItem) {
            if (complexArmorItem.getSlot() == equipmentSlot) {
                this.getParentModel().copyPropertiesTo(humanoidModel);
                this.setPartVisibility(humanoidModel, equipmentSlot);

                boolean legs = this.usesInnerModel(equipmentSlot);
                boolean glint = itemStack.hasFoil();
                int patternCount = ComplexArmorItem.getPatternCount(itemStack);

                if (patternCount == 0) patternCount = 1;

                for (int p = 0; p < patternCount; p++) {
                    var pattern = ComplexArmorItem.getPattern(itemStack, p);
                    int i = pattern.getSecond();
                    float r = (float) (i >> 16 & 255) / 255.0F;
                    float g = (float) (i >> 8 & 255) / 255.0F;
                    float b = (float) (i & 255) / 255.0F;

                    RenderComplexArmorPart(poseStack, multiBufferSource, light, pattern.getFirst(), glint, humanoidModel, legs, r, g, b);
                }

                ci.cancel();
            }
        }
    }

    void RenderComplexArmorPart(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, ComplexPattern pattern, boolean glint, A humanoidModel, boolean legs, float red, float green, float blue) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(getArmorTexture(pattern, legs)), false, glint);
        humanoidModel.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    ResourceLocation getArmorTexture(ComplexPattern pattern, boolean legs){
        String path = "textures/models/armor/" + pattern.getName() + "_layer_" + (legs ? 2 : 1) + ".png";
        return ARMOR_LOCATION_CACHE.computeIfAbsent(path, ResourceLocation::new);
    }
}
