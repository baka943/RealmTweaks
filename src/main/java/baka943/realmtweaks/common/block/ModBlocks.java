package baka943.realmtweaks.common.block;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.fluid.ModFluids;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import epicsquid.roots.api.CustomPlantType;
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

	public static Block ANOTHER_SAPLING ;
	public static Block ALTERNATE_SAPLING;
	public static Block NIGHTMARE_SAPLING;

	public static final Block BLOODED_ENDSTONE = new BlockBloodedEndstone();

	public static Block IMPURE_LIFE_ESSENCE;
	public static Block END_PORTAL = new BlockEndPortal();

	public static final Block METEORITE = new BlockOres("meteorite", 4);
	public static final Block METEORITE_IRON_ORE = new BlockOres("meteorite_iron_ore", 4);
	public static final Block METEORITE_DILITHIUM_ORE = new BlockOres("meteorite_dilithium_ore", 4);
	public static final Block METEORITE_TRITANIUM_ORE = new BlockOres("meteorite_tritanium_ore", 4);

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		if(RealmTweaks.isBetweenlandsLoaded && RealmTweaks.isLostCitiesLoaded) {
			registry.register(ANOTHER_SAPLING = new BlockRealmSapling("another_sapling", Utils.getDimId("lostcities")));
		}

		if(RealmTweaks.isBloodMagicLoaded) {
			if(RealmTweaks.isBetweenlandsLoaded) {
				registry.register(NIGHTMARE_SAPLING = new BlockRealmSapling("nightmare_sapling", Utils.getDimId("the_end")));
			}

			registry.register(END_PORTAL);
			registry.register(BLOODED_ENDSTONE);

			registry.register(IMPURE_LIFE_ESSENCE = new BlockFluid(ModFluids.IMPURE_LIFE_ESSENCE, Material.WATER, 2));

			registry.register(METEORITE);
			registry.register(METEORITE_IRON_ORE);
			registry.register(METEORITE_DILITHIUM_ORE);
			registry.register(METEORITE_TRITANIUM_ORE);
		}

		if(RealmTweaks.isBetweenlandsLoaded && RealmTweaks.isBetterNetherLoaded) {
			registry.register(ALTERNATE_SAPLING = new BlockRealmSapling("alternate_sapling", Utils.getDimId("the_nether")));
		}
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		if(RealmTweaks.isBetweenlandsLoaded && RealmTweaks.isLostCitiesLoaded) {
			registry.register(new ItemBlock(ANOTHER_SAPLING).setRegistryName(Utils.getRL("another_sapling")));
		}

		if(RealmTweaks.isBloodMagicLoaded) {
			if(RealmTweaks.isBetweenlandsLoaded) {
				registry.register(new ItemBlock(NIGHTMARE_SAPLING).setRegistryName(Utils.getRL("nightmare_sapling")));
			}

			registry.register(new ItemBlock(BLOODED_ENDSTONE).setRegistryName(Utils.getRL("blooded_end_stone")));

			registry.register(new ItemBlock(METEORITE).setRegistryName(Utils.getRL("meteorite")));
			registry.register(new ItemBlock(METEORITE_IRON_ORE).setRegistryName(Utils.getRL("meteorite_iron_ore")));
			registry.register(new ItemBlock(METEORITE_DILITHIUM_ORE).setRegistryName(Utils.getRL("meteorite_dilithium_ore")));
			registry.register(new ItemBlock(METEORITE_TRITANIUM_ORE).setRegistryName(Utils.getRL("meteorite_tritanium_ore")));
		}

		if(RealmTweaks.isBetweenlandsLoaded && RealmTweaks.isBetterNetherLoaded) {
			registry.register(new ItemBlock(ALTERNATE_SAPLING).setRegistryName(Utils.getRL("alternate_sapling")));
		}
	}

}
