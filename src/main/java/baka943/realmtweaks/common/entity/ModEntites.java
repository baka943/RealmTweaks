package baka943.realmtweaks.common.entity;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public final class ModEntites {

	public static void init() {
		int id = 0;

		EntityRegistry.registerModEntity(Utils.getRL("swamp_spider"), EntitySwampSpider.class, LibMisc.MOD_ID + ".swamp_spider", id++, RealmTweaks.instance, 64, 10, true);
	}

}
