package baka943.realmtweaks.common.integrations.jei;

import baka943.realmtweaks.common.lib.LibMisc;
import com.brandon3055.draconicevolution.DEFeatures;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import sonar.fluxnetworks.client.jei.FluxCraftingCategory;
import sonar.fluxnetworks.common.registry.RegistryItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RealmCategory implements IRecipeCategory<RealmWrapper> {

	private final IDrawable background;
	private final IDrawable icon;

	static ITickTimer timer;

	public RealmCategory(@Nonnull IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(FluxCraftingCategory.TEXTURES, 0, -20, 128, 80);
		icon = guiHelper.createDrawableIngredient(new ItemStack(RegistryItems.FLUX));
		timer = guiHelper.createTickTimer(60, 320, false);
	}

	public static void register(@Nonnull IModRegistry registry) {
		registry.addRecipes(getRecipes(), "flux");
		registry.addRecipeCatalyst(new ItemStack(RegistryItems.FLUX), "flux");
	}

	@Nonnull
	public static List<RealmWrapper> getRecipes() {
		List<RealmWrapper> recipes = new ArrayList<>();
		recipes.add(new RealmWrapper(new ItemStack(DEFeatures.draconiumDust), new ItemStack(RegistryItems.FLUX)));

		return recipes;
	}

	@Nonnull
	@Override
	public String getUid() {
		return "flux";
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.format("info.fluxnetworks.jei.creatingfluxrecipe");
	}

	@Nonnull
	@Override
	public String getModName() {
		return LibMisc.MOD_NAME;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Nullable
	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout iRecipeLayout, @Nonnull RealmWrapper realmWrapper, @Nonnull IIngredients iIngredients) {
		IGuiItemStackGroup guiItemStack = iRecipeLayout.getItemStacks();

		guiItemStack.init(0, false, 8, 24);
		guiItemStack.init(1,false, 102, 24);

		guiItemStack.set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
		guiItemStack.set(1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

}
