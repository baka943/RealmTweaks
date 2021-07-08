package baka943.realmtweaks.common.integrations;

import baka943.realmtweaks.common.lib.Utils;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thebetweenlands.common.entity.mobs.EntityLurker;

public class RootsTweaks {

	public static void init() {
		RitualRegistry.ritualRegistry.remove("ritual_animal_harvest");
		RitualRegistry.ritualRegistry.remove("ritual_fire_storm");
		RitualRegistry.ritualRegistry.remove("ritual_frost_lands");
		RitualRegistry.ritualRegistry.remove("ritual_healing_aura");
		RitualRegistry.ritualRegistry.remove("ritual_overgrowth");
		RitualRegistry.ritualRegistry.remove("ritual_purity");
		RitualRegistry.ritualRegistry.remove("ritual_spreading_forest");
		RitualRegistry.ritualRegistry.remove("ritual_summon_creatures");
		RitualRegistry.ritualRegistry.remove("ritual_transmutation");

		SpellRegistry.spellRegistry.remove("spell_growth_infusion");
		SpellRegistry.spellRegistry.remove("spell_iced_touch");
		SpellRegistry.spellRegistry.remove("spell_life_drain");
		SpellRegistry.spellRegistry.remove("spell_sanctuary");
		SpellRegistry.spellRegistry.remove("spell_sense_animals");
		SpellRegistry.spellRegistry.remove("spell_shatter");
		SpellRegistry.spellRegistry.remove("spell_time_stop");

		ModRecipes.getRunicShearRecipes().clear();
		ModRecipes.getRunicShearEntityRecipes().clear();

		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation("roots", "wildewheet"), ModBlocks.wildroot, null, new ItemStack(ModItems.wildewheet), new ItemStack(ModItems.wildroot)));
		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation("roots", "spirit_herb"), ModBlocks.wildewheet, null, new ItemStack(ModItems.spirit_herb), new ItemStack(ModItems.wildewheet)));
		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation("roots", "crystal_air"), ModBlocks.cloud_berry, null, new ItemStack(baka943.realmtweaks.common.item.ModItems.POWDER_AIR), new ItemStack(ModItems.cloud_berry)));
		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation("roots", "crystal_earth"), ModBlocks.stalicripe, null, new ItemStack(baka943.realmtweaks.common.item.ModItems.POWDER_EARTH), new ItemStack(ModItems.stalicripe)));
		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation("roots", "crystal_fire"), ModBlocks.infernal_bulb, null, new ItemStack(Items.BLAZE_POWDER), new ItemStack(ModItems.infernal_bulb)));
		ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation("roots", "crystal_water"), ModBlocks.dewgonia, null, new ItemStack(baka943.realmtweaks.common.item.ModItems.POWDER_WATER), new ItemStack(ModItems.dewgonia)));

		ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation("roots", "fey_leather"), new ItemStack(ModItems.fey_leather), EntityLurker.class, 600));
	}

}
