package baka943.realmtweaks.common.block;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.block.tile.TileBetweenAltar;
import baka943.realmtweaks.common.fluid.ModFluids;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModBlocks {

	public static final Block BETWEEN_ALTAR = new BlockBetweenAltar();

	public static final Block ENDER_ESSENCE = new BlockFluid(ModFluids.ENDER_ESSENCE, Material.WATER, 1);
	public static final Block END_PORTAL = new BlockEndPortal();

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		if(RealmTweaks.BTLoaded) {
			registry.register(BETWEEN_ALTAR);
			GameRegistry.registerTileEntity(TileBetweenAltar.class, Utils.getRL("between_altar"));
		}

		if(RealmTweaks.BMLoaded) {
			registry.register(END_PORTAL);
			registry.register(ENDER_ESSENCE);
		}
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		if(RealmTweaks.BTLoaded)
			registry.register(new ItemBlock(BETWEEN_ALTAR).setRegistryName(BETWEEN_ALTAR.getRegistryName()));
	}

}
