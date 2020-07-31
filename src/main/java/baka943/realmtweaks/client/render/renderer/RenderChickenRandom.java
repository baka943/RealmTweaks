package baka943.realmtweaks.client.render.renderer;

import baka943.realmtweaks.common.core.handler.RandomChickenTextures;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderChickenRandom extends RenderChicken {

	public RenderChickenRandom(RenderManager manager) {
		super(manager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityChicken entity) {
		if(entity.isChild()) {
			return RandomChickenTextures.getRandomTexture(entity, RandomChickenTextures.RandomTextureType.CHICK);
		} else return RandomChickenTextures.getRandomTexture(entity, RandomChickenTextures.RandomTextureType.CHICKEN);
	}

	public static IRenderFactory<EntityChicken> factory() {
		return RenderChickenRandom::new;
	}

}
