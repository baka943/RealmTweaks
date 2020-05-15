package baka943.realmtweaks.client.render.renderer;

import baka943.realmtweaks.common.entity.monster.EntityEnderZombie;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderEnderZombie extends RenderBiped<EntityEnderZombie> {

	private static final ResourceLocation ENDER_ZOMBIE_TEXTURES = Utils.getRL("textures/entity/ender_zombie.png");

	public RenderEnderZombie(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelZombie(), 0.5F);

		LayerBipedArmor layerBipedArmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				this.modelArmor = new ModelZombie(1.0F, true);
				this.modelLeggings = new ModelZombie(0.5F, true);
			}
		};

		this.addLayer(layerBipedArmor);
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityEnderZombie entity) {
		return ENDER_ZOMBIE_TEXTURES;
	}

}
