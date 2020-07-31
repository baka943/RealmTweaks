package baka943.realmtweaks.client.render.renderer;

import baka943.realmtweaks.client.core.model.ModelEnderSkeleton;
import baka943.realmtweaks.client.render.renderer.layers.LayerEnderSkeletonClothing;
import baka943.realmtweaks.common.entity.monster.EntityEnderSkeleton;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderEnderSkeleton extends RenderBiped<EntityEnderSkeleton> {

	private static final ResourceLocation ENDER_SKELETON_TEXTURES = Utils.getRL("textures/entity/ender_skeleton.png");

	public RenderEnderSkeleton(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelEnderSkeleton(), 0.5F);

		LayerBipedArmor layerBipedArmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				this.modelLeggings = new ModelEnderSkeleton(0.5F, true);
				this.modelArmor = new ModelEnderSkeleton(1.0F, true);
			}
		};

		this.addLayer(layerBipedArmor);
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerEnderSkeletonClothing(this));
	}

	@Override
	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityEnderSkeleton entity) {
		return ENDER_SKELETON_TEXTURES;
	}

}
