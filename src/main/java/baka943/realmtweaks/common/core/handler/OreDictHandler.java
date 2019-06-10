package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thebetweenlands.common.registries.BlockRegistry;

public class OreDictHandler {

	public static void registerOreDict() {
		OreDictionary.registerOre("enderShard", ModItems.ENDER_SHARD);

		OreDictionary.registerOre("logWeedwood", BlockRegistry.WEEDWOOD);
		OreDictionary.registerOre("logWeedwood", new ItemStack(BlockRegistry.LOG_WEEDWOOD, 1, 0));
		OreDictionary.registerOre("logWeedwood", new ItemStack(BlockRegistry.LOG_WEEDWOOD, 1, 12));

		OreDictionary.registerOre("hopper", Blocks.HOPPER);
		OreDictionary.registerOre("hopper", BlockRegistry.SYRMORITE_HOPPER);
	}
}
