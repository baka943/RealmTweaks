package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModItems {

	public static final Item ENDER_SHARD = new ItemEnderShard();
	public static final Item FOREST_BAT = new ItemForestBat();
	public static final ItemPaperTool PAPER_TOOL = new ItemPaperTool();
	public static final ItemMod POWDER_WATER = new ItemMod("powder_water");
	public static final ItemMod POWDER_AIR = new ItemMod("powder_air");
	public static final ItemMod POWDER_EARTH = new ItemMod("powder_earth");
	public static final ItemMod ENDSTONE_SHARD = new ItemMod("end_stone_shard");
	public static Item BLOOD_TEAR = new ItemBloodTear();
	public static Item OCTINE_FLINTSTONES = new ItemOctineFlintstones();

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(ENDER_SHARD);
		registry.register(BLOOD_TEAR);
		registry.register(ENDSTONE_SHARD);

		registry.register(FOREST_BAT);
		registry.register(PAPER_TOOL);
		registry.register(POWDER_AIR);
		registry.register(POWDER_WATER);
		registry.register(POWDER_EARTH);

		registry.register(OCTINE_FLINTSTONES);
	}

}
