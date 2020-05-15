package baka943.realmtweaks.common.block;

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
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModBlocks {

	public static final Block ANOTHER_SAPLING = new BlockRealmSapling("another_sapling", Utils.getDimensionId("lostcities"));
	public static final Block ALTERNATE_SAPLING = new BlockRealmSapling("alternate_sapling", Utils.getDimensionId("the_nether"));
	public static final Block NIGHTMARE_SAPLING = new BlockRealmSapling("nightmare_sapling", Utils.getDimensionId("the_end"));

	private static final Block END_PORTAL = new BlockEndPortal();
	public static final Block BLOODED_ENDSTONE = new BlockBloodedEndstone();

	public static final Block MOLTEN_OCTINE = new BlockFluid(ModFluids.OCTINE, Material.LAVA, 8);
	public static final Block MOLTEN_SYRMORITE = new BlockFluid(ModFluids.SYRMORITE, Material.LAVA, 8);
	public static final Block LIFE_CORE = new BlockFluid(ModFluids.LIFE_CORE, Material.WATER, 2);

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		registry.register(ANOTHER_SAPLING);
		registry.register(ALTERNATE_SAPLING);
		registry.register(NIGHTMARE_SAPLING);

		registry.register(END_PORTAL);
		registry.register(BLOODED_ENDSTONE);

		registry.register(MOLTEN_OCTINE);
		registry.register(MOLTEN_SYRMORITE);
		registry.register(LIFE_CORE);
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(new ItemBlock(ANOTHER_SAPLING).setRegistryName(Utils.getRL("another_sapling")));
		registry.register(new ItemBlock(ALTERNATE_SAPLING).setRegistryName(Utils.getRL("alternate_sapling")));
		registry.register(new ItemBlock(NIGHTMARE_SAPLING).setRegistryName(Utils.getRL("nightmare_sapling")));

		registry.register(new ItemBlock(BLOODED_ENDSTONE).setRegistryName(Utils.getRL("blooded_end_stone")));
	}

}
