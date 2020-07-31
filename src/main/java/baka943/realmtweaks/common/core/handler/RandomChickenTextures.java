package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.client.render.renderer.RenderChickenRandom;
import baka943.realmtweaks.common.lib.Utils;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class RandomChickenTextures {

	private static ListMultimap<RandomTextureType, ResourceLocation> textures;

	private static final int CHICKEN_COUNT = 7;
	private static final int CHICK_COUNT = 7;

	@SideOnly(Side.CLIENT)
	public static void preInitClient(FMLPreInitializationEvent event) {
		textures = Multimaps.newListMultimap(new EnumMap<>(RandomTextureType.class), ArrayList::new);

		registerTextures(RandomTextureType.CHICKEN, CHICKEN_COUNT, new ResourceLocation("textures/entity/chicken.png"));
		registerTextures(RandomTextureType.CHICK, CHICK_COUNT, null);

		registerOverride(EntityChicken.class, RenderChickenRandom.factory());
	}

	@SideOnly(Side.CLIENT)
	public static ResourceLocation getRandomTexture(Entity entity, RandomTextureType type) {
		List<ResourceLocation> styles = textures.get(type);

		UUID id = entity.getUniqueID();
		int choice = Math.abs((int)(id.getMostSignificantBits() % styles.size()));

		return styles.get(choice);
	}

	@SideOnly(Side.CLIENT)
	private static void registerTextures(RandomTextureType type, int count, ResourceLocation vanilla) {
		String name = type.name().toLowerCase();

		for(int i = 1; i < count + 1; i++) {
			textures.put(type, Utils.getRL(String.format("textures/entity/random/%s%d.png", name, i)));

		}

		if(vanilla != null) {
			textures.put(type, vanilla);
		}
	}

	@SideOnly(Side.CLIENT)
	private static <T extends Entity>void registerOverride(Class<T> clazz, IRenderFactory<? super T> factory) {
		RenderingRegistry.registerEntityRenderingHandler(clazz, factory);
	}

	public enum RandomTextureType {
		CHICKEN, CHICK;
	}

}
