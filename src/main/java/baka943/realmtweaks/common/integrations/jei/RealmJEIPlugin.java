package baka943.realmtweaks.common.integrations.jei;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.block.ModBlocks;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeCategory;

@JEIPlugin
public class RealmJEIPlugin implements IModPlugin {

	public static IJeiHelpers HELPERS;

	@Override
	public void register(IModRegistry registry) {
		HELPERS = registry.getJeiHelpers();

		if(RealmTweaks.BTLoaded)
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.BETWEEN_ALTAR), PetalApothecaryRecipeCategory.UID);
	}

}
