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

	public static final Item ENDER_SHARD = new ItemEnderShard();
	public static final Item FOREST_BAT = new ItemForestBat();
	public static final ItemPaperTool PAPER_TOOL = new ItemPaperTool("paper_tool");
	public static final ItemPaperTool PARCHMENT_TOOL = new ItemPaperTool("parchment_tool");
	public static final ItemPaperTool ENDER_TOOL = new ItemPaperTool("ender_tool");
	public static final ItemPaperTool DEBUG_TOOL = new ItemPaperTool("debug_tool");
	public static final Item ADVANCEMENT_BOOK = new ItemAdvancementBook();
	public static Item BLOOD_TEAR;
	public static final Item BARK_LIVINGWOOD = new ItemMod("bark_livingwood");
	public static Item OCTINE_FLINTSTONES;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		if(RealmTweaks.isBloodMagicLoaded) {
			registry.register(ENDER_SHARD);
			registry.register(BLOOD_TEAR = new ItemBloodTear());
		}

		if(RealmTweaks.isRootsLoaded) {
			registry.register(BARK_LIVINGWOOD);
		}

		registry.register(FOREST_BAT);
		registry.register(PAPER_TOOL);
		registry.register(PARCHMENT_TOOL);
		registry.register(ENDER_TOOL);
		registry.register(DEBUG_TOOL);
		registry.register(ADVANCEMENT_BOOK);

		if(RealmTweaks.isBetweenlandsLoaded) {
			registry.register(OCTINE_FLINTSTONES = new ItemOctineFlintstones());
		}
	}

}
