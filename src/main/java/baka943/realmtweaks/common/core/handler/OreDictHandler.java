package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.block.ModBlocks;
import baka943.realmtweaks.common.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thebetweenlands.common.registries.BlockRegistry;

public class OreDictHandler {

	public static void registerOreDict() {
		if(RealmTweaks.isBloodMagicLoaded) {
			OreDictionary.registerOre("enderShard", ModItems.ENDER_SHARD);

			OreDictionary.registerOre("oreDilithium", ModBlocks.METEORITE_DILITHIUM_ORE);
			OreDictionary.registerOre("oreTritanium", ModBlocks.METEORITE_TRITANIUM_ORE);
		}

		if(RealmTweaks.isBetweenlandsLoaded) {
			OreDictionary.registerOre("logWeedwood", BlockRegistry.WEEDWOOD);
			OreDictionary.registerOre("logWeedwood", new ItemStack(BlockRegistry.LOG_WEEDWOOD, 1, 0));
			OreDictionary.registerOre("logWeedwood", new ItemStack(BlockRegistry.LOG_WEEDWOOD, 1, 12));

			OreDictionary.registerOre("hopper", Blocks.HOPPER);
			OreDictionary.registerOre("hopper", BlockRegistry.SYRMORITE_HOPPER);
		}

		if(RealmTweaks.isRootsLoaded) {
			OreDictionary.registerOre("rootsBark", ModItems.BARK_LIVINGWOOD);
			OreDictionary.registerOre("rootsBark", ModItems.BARK_WEEDWOOD);
		}
	}

}
