package baka943.realmtweaks.common.integrations.jei;

import ferro2000.immersivetech.common.ITContent;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class RealmJEIPlugin implements IModPlugin {

	public static IJeiHelpers HELPERS;

	@Override
	public void register(IModRegistry registry) {
		HELPERS = registry.getJeiHelpers();

		registry.addRecipeCatalyst(new ItemStack(ITContent.blockStoneMultiblock), "ie.cokeoven");
	}

}
