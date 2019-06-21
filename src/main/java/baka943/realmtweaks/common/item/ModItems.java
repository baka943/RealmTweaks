package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModItems {

	public static final Item ENDER_SHARD = new ItemMod("ender_shard");
	public static final Item ADVANCEMENT_BOOK = new ItemAdvancementBook();
	public static final Item WEEDWOOD_FISHING_ROD = new ItemWeedwoodRod();
	public static final Item REALM_TALISMAN = new ItemRealmTalisman();

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(ENDER_SHARD);
		registry.register(ADVANCEMENT_BOOK);
		registry.register(WEEDWOOD_FISHING_ROD);
		registry.register(REALM_TALISMAN);
	}

}
