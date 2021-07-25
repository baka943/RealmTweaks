package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModItems {

	public static final Item ENDER_SHARD = new ItemMod("ender_shard");
	public static final Item FOREST_BAT = new ItemForestBat();
	public static final Item PAPER_TOOL = new ItemPaperTool();
	public static final Item POWDER_AIR = new ItemMod("powder_air");
	public static final Item POWDER_EARTH = new ItemMod("powder_earth");
	public static final Item POWDER_WATER = new ItemMod("powder_water");
	public static final Item LEVITATED_STONE = new ItemMod("levitated_stone");
	public static final Item BLOOD_TEAR = new ItemBloodTear();
	public static final Item OCTINE_FLINTSTONES = new ItemOctineFlintstones();
	public static final Item BITCOIN = new ItemMod("bitcoin");
	public static final Item DRAGON_FRUIT = new ItemDragonFruit();

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		if(RealmTweaks.BMLoaded) {
			registry.register(ENDER_SHARD);
			registry.register(BLOOD_TEAR);
			registry.register(LEVITATED_STONE);
			registry.register(DRAGON_FRUIT);
		}

		if(RealmTweaks.BTLoaded) {
			registry.register(POWDER_AIR);
			registry.register(POWDER_EARTH);
			registry.register(POWDER_WATER);

			registry.register(OCTINE_FLINTSTONES);
		}

		registry.register(FOREST_BAT);
		registry.register(PAPER_TOOL);
		registry.register(BITCOIN);
	}

}
