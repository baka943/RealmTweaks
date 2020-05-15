package baka943.realmtweaks.client.render.renderer;

import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderSwampSpider extends RenderSpider<EntitySwampSpider> {

	private static final ResourceLocation SWAMP_SPIDER_TEXTURES = Utils.getRL("textures/entity/swamp_spider.png");

	public RenderSwampSpider(RenderManager renderManager) {
		super(renderManager);
		this.shadowSize *= 0.7F;
	}

	@Override
	protected void preRenderCallback(@Nonnull EntitySwampSpider entitySwampSpider, float partialTickTime) {
		GlStateManager.scale(0.7F, 0.7F, 0.7F);
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntitySwampSpider entity) {
		return SWAMP_SPIDER_TEXTURES;
	}

}
