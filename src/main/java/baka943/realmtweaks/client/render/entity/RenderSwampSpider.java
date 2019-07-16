package baka943.realmtweaks.client.render.entity;

import baka943.realmtweaks.client.render.entity.layers.LayerSwampSpiderEyes;
import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderSwampSpider<T extends EntitySwampSpider> extends RenderLiving<T> {

	private static final ResourceLocation SWAMP_SPIDER_TEXTURES = Utils.getRL("textures/entity/swamp_spider.png");

	public RenderSwampSpider(RenderManager render) {
		super(render, new ModelSpider(), 1.0F);
		this.addLayer(new LayerSwampSpiderEyes(this));
		this.shadowSize *= 0.7F;
	}

	@Override
	protected float getDeathMaxRotation(EntitySwampSpider spider) {
		return 180.0F;
	}

	protected void preRenderCallback(EntitySwampSpider entity, float f) {
		GlStateManager.scale(0.7F, 0.7F, 0.7F);
	}

	protected ResourceLocation getEntityTexture(@Nonnull EntitySwampSpider entity) {
		return SWAMP_SPIDER_TEXTURES;
	}

}
