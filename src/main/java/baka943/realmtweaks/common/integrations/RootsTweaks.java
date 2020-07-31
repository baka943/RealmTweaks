package baka943.realmtweaks.common.integrations;

import baka943.realmtweaks.common.RealmTweaks;
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
import thebetweenlands.common.registries.BlockRegistry;

public class RootsTweaks {

	public static void init() {
		RitualRegistry.ritualRegistry.remove("ritual_overgrowth");
		RitualRegistry.ritualRegistry.remove("ritual_healing_aura");

		ModRecipes.getRunicShearRecipes().clear();
		ModRecipes.getRunicShearEntityRecipes().clear();
		ModRecipes.getBarkRecipes().clear();

		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("wildewheet"), ModBlocks.wildroot, null, new ItemStack(ModItems.wildewheet), new ItemStack(ModItems.wildroot)));
		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Utils.getRL("spirit_herb"), ModBlocks.wildewheet, null, new ItemStack(ModItems.spirit_herb), new ItemStack(ModItems.wildewheet)));

		ModRecipes.addModdedBarkRecipe("wildwood", new ItemStack(ModItems.bark_wildwood, 4), new ItemStack(ModBlocks.wildwood_log));

		if(RealmTweaks.isBetweenlandsLoaded) {
			ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(Utils.getRL("string"), new ItemStack(Items.STRING), EntitySwampSpider.class, 600));
			ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(Utils.getRL("fey_leather"), new ItemStack(ModItems.fey_leather), EntityLurker.class, 600));

			ModRecipes.addModdedBarkRecipe("weedwood", new ItemStack(baka943.realmtweaks.common.item.ModItems.BARK_WEEDWOOD, 4), new ItemStack(BlockRegistry.WEEDWOOD));
			ModRecipes.addModdedBarkRecipe("weedwood_log", new ItemStack(baka943.realmtweaks.common.item.ModItems.BARK_WEEDWOOD, 4), new ItemStack(BlockRegistry.LOG_WEEDWOOD));
		}

		if(RealmTweaks.isBotaniaLoaded) {
			ModRecipes.addModdedBarkRecipe("livingwood", new ItemStack(baka943.realmtweaks.common.item.ModItems.BARK_LIVINGWOOD, 4), new ItemStack(vazkii.botania.common.block.ModBlocks.livingwood));
		}
	}

}
