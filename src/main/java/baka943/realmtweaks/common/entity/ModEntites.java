package baka943.realmtweaks.common.entity;

import baka943.realmtweaks.client.render.entity.RenderEnderSkeleton;
import baka943.realmtweaks.client.render.entity.RenderEnderZombie;
import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.entity.monster.EntityEnderSkeleton;
import baka943.realmtweaks.common.entity.monster.EntityEnderZombie;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntites {

	public static void init() {
		int id = 1;

		EntityRegistry.registerModEntity(Utils.getRL("ender_zombie"), EntityEnderZombie.class, "realmtweaks.ender_zombie", id++, RealmTweaks.instance, 64, 3, true, 1447446, 0);
		EntityRegistry.registerModEntity(Utils.getRL("ender_skeleton"), EntityEnderSkeleton.class, "realmtweaks.ender_skeleton", id++, RealmTweaks.instance, 64, 3, true, 1447446, 0);
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		RenderingRegistry.registerEntityRenderingHandler(EntityEnderZombie.class, RenderEnderZombie::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityEnderSkeleton.class, RenderEnderSkeleton::new);
	}

}
