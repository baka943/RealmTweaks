package baka943.realmtweaks.common.integrations;

import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import baka943.realmtweaks.common.lib.Utils;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import thebetweenlands.common.entity.mobs.EntityLurker;

public class RootsTweaks {

	public static void init() {
		RitualRegistry.ritualRegistry.remove("ritual_overgrowth");
		RitualRegistry.ritualRegistry.remove("ritual_healing_aura");

		ModRecipes.getRunicShearRecipes().clear();
		ModRecipes.getRunicShearEntityRecipes().clear();

		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("wildewheet"), ModBlocks.wildroot, null, new ItemStack(ModItems.wildewheet), new ItemStack(ModItems.wildroot)));
		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("spirit_herb"), ModBlocks.wildewheet, null, new ItemStack(ModItems.spirit_herb), new ItemStack(ModItems.wildewheet)));
//		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("crystal_air"), ModBlocks.cloud_berry, null, new ItemStack(BlocksTC.crystalAir), new ItemStack(ModItems.cloud_berry)));
//		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("crystal_earth"), ModBlocks.stalicripe, null, new ItemStack(BlocksTC.crystalEarth), new ItemStack(ModItems.stalicripe)));
//		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("crystal_fire"), ModBlocks.infernal_bulb, null, new ItemStack(BlocksTC.crystalFire), new ItemStack(ModItems.infernal_bulb)));
//		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("crystal_water"), ModBlocks.dewgonia, null, new ItemStack(BlocksTC.crystalWater), new ItemStack(ModItems.dewgonia)));
//		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("crystal_order"), ModBlocks.moonglow, null, new ItemStack(BlocksTC.crystalOrder), new ItemStack(ModItems.moonglow_leaf)));
//		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("crystal_entropy"), ModBlocks.spirit_herb, null, new ItemStack(BlocksTC.crystalEntropy), new ItemStack(ModItems.spirit_herb)));

		ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(Utils.getRL("string"), new ItemStack(Items.STRING), EntitySwampSpider.class, 600));
		ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(Utils.getRL("fey_leather"), new ItemStack(ModItems.fey_leather), EntityLurker.class, 600));
	}

}
