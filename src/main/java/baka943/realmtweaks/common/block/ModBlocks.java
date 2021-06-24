package baka943.realmtweaks.common.block;

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

	public static Block ANOTHER_SAPLING ;
	public static Block ALTERNATE_SAPLING;
	public static Block NIGHTMARE_SAPLING;

	public static final Block ALTAR = new BlockBetweenAltar();

	public static final Block BLOODED_ENDSTONE = new BlockBloodedEndstone();

	public static Block IMPURE_LIFE_ESSENCE;
	public static Block END_PORTAL = new BlockEndPortal();

	public static final Block METEORITE = new BlockOres("meteorite", 4);
	public static final Block METEORITE_IRON_ORE = new BlockOres("meteorite_iron_ore", 4);

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		registry.register(ANOTHER_SAPLING = new BlockRealmSapling("another_sapling", Utils.getDimId("lostcities")));

		registry.register(ALTAR);
		GameRegistry.registerTileEntity(TileBetweenAltar.class, Utils.getRL("between_altar"));

		registry.register(END_PORTAL);

		registry.register(NIGHTMARE_SAPLING = new BlockRealmSapling("nightmare_sapling", Utils.getDimId("the_end")));

		registry.register(BLOODED_ENDSTONE);

		registry.register(IMPURE_LIFE_ESSENCE = new BlockFluid(ModFluids.IMPURE_LIFE_ESSENCE, Material.WATER, 2));

		registry.register(METEORITE);
		registry.register(METEORITE_IRON_ORE);

		registry.register(ALTERNATE_SAPLING = new BlockRealmSapling("alternate_sapling", Utils.getDimId("overworld")));
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(new ItemBlock(ANOTHER_SAPLING).setRegistryName(Utils.getRL("another_sapling")));

		registry.register(new ItemBlock(ALTAR).setRegistryName(ALTAR.getRegistryName()));

		registry.register(new ItemBlock(NIGHTMARE_SAPLING).setRegistryName(Utils.getRL("nightmare_sapling")));

		registry.register(new ItemBlock(BLOODED_ENDSTONE).setRegistryName(Utils.getRL("blooded_end_stone")));

		registry.register(new ItemBlock(METEORITE).setRegistryName(Utils.getRL("meteorite")));
		registry.register(new ItemBlock(METEORITE_IRON_ORE).setRegistryName(Utils.getRL("meteorite_iron_ore")));

		registry.register(new ItemBlock(ALTERNATE_SAPLING).setRegistryName(Utils.getRL("alternate_sapling")));
	}

}
