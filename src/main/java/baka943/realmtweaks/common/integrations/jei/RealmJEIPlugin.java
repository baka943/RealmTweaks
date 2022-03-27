package baka943.realmtweaks.common.integrations.jei;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.block.ModBlocks;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeCategory;

import javax.annotation.Nonnull;

@JEIPlugin
public class RealmJEIPlugin implements IModPlugin {

	public static IJeiHelpers HELPERS;

	@Override
	public void register(IModRegistry registry) {
		HELPERS = registry.getJeiHelpers();

		if(RealmTweaks.BTLoaded)
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.BETWEEN_ALTAR), PetalApothecaryRecipeCategory.UID);

		RealmCategory.register(registry);
	}

	@Override
	public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new RealmCategory(registry.getJeiHelpers().getGuiHelper()));
	}

}
