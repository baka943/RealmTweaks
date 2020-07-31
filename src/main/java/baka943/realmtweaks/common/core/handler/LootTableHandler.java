package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class LootTableHandler {

	@SubscribeEvent
	public static void onLootTableLoaded(LootTableLoadEvent event) {
		ResourceLocation lootName = event.getName();
		LootTableManager manager = event.getLootTableManager();

		if(RealmTweaks.isBetweenlandsLoaded) {
			if(lootName.equals(new ResourceLocation("thebetweenlands", "animator/scroll"))) {
				event.setTable(manager.getLootTableFromLocation(Utils.getRL("betweenlands/animator/scroll")));
			}

			if(lootName.equals(new ResourceLocation("thebetweenlands", "entities/dark_druid"))) {
				event.setTable(LootTable.EMPTY_LOOT_TABLE);
			}
		}

		if(RealmTweaks.isBloodMagicLoaded) {
			if(lootName.equals(new ResourceLocation("minecraft", "entities/enderman"))) {
				event.setTable(manager.getLootTableFromLocation(Utils.getRL("override/entities/enderman")));
			}
		}
	}

}
