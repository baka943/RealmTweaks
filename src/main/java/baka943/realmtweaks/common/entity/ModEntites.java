package baka943.realmtweaks.common.entity;

import baka943.realmtweaks.client.render.renderer.RenderSwampSpider;
import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MobEntites {

	public static void init() {
		int id = 1;

		EntityRegistry.registerModEntity(Utils.getRL("swamp_spider"), EntitySwampSpider.class, "swamp_spider", id++, RealmTweaks.instance, 64, 3, true, 0x996600, 0x00ff00);
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		RenderingRegistry.registerEntityRenderingHandler(EntitySwampSpider.class, RenderSwampSpider::new);
	}

}
