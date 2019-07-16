package baka943.realmtweaks.client.render.entity.layers;

import baka943.realmtweaks.client.render.entity.RenderSwampSpider;
import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerSwampSpiderEyes<T extends EntitySwampSpider> implements LayerRenderer<T> {

	private static final ResourceLocation SWAMP_SPIDER_EYES = Utils.getRL("textures/entity/swamp_spider_eyes.png");
	private final RenderSwampSpider<T> spiderRenderer;

	public LayerSwampSpiderEyes(RenderSwampSpider<T> spiderRenderer) {
		this.spiderRenderer = spiderRenderer;
	}

	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.spiderRenderer.bindTexture(SWAMP_SPIDER_EYES);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

		if(entitylivingbaseIn.isInvisible()) {
			GlStateManager.depthMask(false);
		} else {
			GlStateManager.depthMask(true);
		}

		int i = 61680;
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
		this.spiderRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);

		i = entitylivingbaseIn.getBrightnessForRender();
		j = i % 65536;
		k = i / 65536;

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
		this.spiderRenderer.setLightmap(entitylivingbaseIn);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	public boolean shouldCombineTextures() {
		return false;
	}

}
