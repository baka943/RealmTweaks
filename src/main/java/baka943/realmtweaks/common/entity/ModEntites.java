package baka943.realmtweaks.common.entity;

import baka943.realmtweaks.client.render.renderer.RenderEnderSkeleton;
import baka943.realmtweaks.client.render.renderer.RenderEnderZombie;
import baka943.realmtweaks.client.render.renderer.RenderSwampSpider;
import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.entity.monster.EntityEnderSkeleton;
import baka943.realmtweaks.common.entity.monster.EntityEnderZombie;
import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntites {

	public static void init() {
		int id = 1;

		if(RealmTweaks.isBetweenlandsLoaded) {
			EntityRegistry.registerModEntity(Utils.getRL("swamp_spider"), EntitySwampSpider.class, "realmtweaks.swamp_spider", id++, RealmTweaks.instance, 64, 3, true, 0x00ff00, 0x996600);
		}

		if(RealmTweaks.isBloodMagicLoaded) {
			EntityRegistry.registerModEntity(Utils.getRL("ender_zombie"), EntityEnderZombie.class, "realmtweaks.ender_zombie", id++, RealmTweaks.instance, 64, 3, true, 1447446, 0);
			EntityRegistry.registerModEntity(Utils.getRL("ender_skeleton"), EntityEnderSkeleton.class, "realmtweaks.ender_skeleton", id++, RealmTweaks.instance, 64, 3, true, 1447446, 0);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		if(RealmTweaks.isBetweenlandsLoaded) {
			RenderingRegistry.registerEntityRenderingHandler(EntitySwampSpider.class, RenderSwampSpider::new);
		}

		if(RealmTweaks.isBloodMagicLoaded) {
			RenderingRegistry.registerEntityRenderingHandler(EntityEnderZombie.class, RenderEnderZombie::new);
			RenderingRegistry.registerEntityRenderingHandler(EntityEnderSkeleton.class, RenderEnderSkeleton::new);
		}
	}

}
