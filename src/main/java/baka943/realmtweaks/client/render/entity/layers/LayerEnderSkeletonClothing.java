package baka943.realmtweaks.client.render.entity.layers;

import baka943.realmtweaks.client.core.model.ModelEnderSkeleton;
import baka943.realmtweaks.common.entity.monster.EntityEnderSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerEnderSkeletonClothing implements LayerRenderer<EntityEnderSkeleton> {

	private static final ResourceLocation ENDER_CLOTHES_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray_overlay.png");
	private final RenderLivingBase<?> renderer;
	private final ModelEnderSkeleton layerModel = new ModelEnderSkeleton(0.25F, true);

	public LayerEnderSkeletonClothing(RenderLivingBase<?> renderer) {
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(@Nonnull EntityEnderSkeleton skeleton, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.layerModel.setModelAttributes(this.renderer.getMainModel());
		this.layerModel.setLivingAnimations(skeleton, limbSwing, limbSwingAmount, partialTicks);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.renderer.bindTexture(ENDER_CLOTHES_TEXTURES);
		this.layerModel.render(skeleton, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}

}
