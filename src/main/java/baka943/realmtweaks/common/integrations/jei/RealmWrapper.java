package baka943.realmtweaks.common.integrations.jei;

import com.brandon3055.draconicevolution.DEFeatures;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

import static sonar.fluxnetworks.client.jei.FluxRecipeWrapper.makeQuaternion;

public class RealmWrapper implements IRecipeWrapper {

	public final ItemStack input;
	public final ItemStack output;

	public RealmWrapper(ItemStack input, ItemStack output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		RenderItem itemRender = minecraft.getRenderItem();
		int value = RealmCategory.timer.getValue();
		double offset = (value > 160 ? 160 - (value - 160) : value) / 10F;

		GlStateManager.pushMatrix();
		GlStateManager.translate(63, 19 + offset, 128);
		GlStateManager.scale(32, 32, 32);
		GlStateManager.rotate(makeQuaternion(30, 45, 0));
		itemRender.renderItem(Item.getItemFromBlock(Blocks.OBSIDIAN).getDefaultInstance(), ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(63, 49, 128 + -32);
		GlStateManager.scale(32, 32, 32);
		GlStateManager.rotate(makeQuaternion(30, 45, 0));
		itemRender.renderItem(Item.getItemFromBlock(Blocks.BEDROCK).getDefaultInstance(), ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(63, 36, 128 + -16);
		GlStateManager.scale(16, -16, 16);
		ItemStack toDisplay = value > 160 ? output : input;
		GlStateManager.rotate(makeQuaternion(toDisplay.getItem() instanceof ItemBlock ? 30 : 0, -90 + 180 * ((float) value / 320), 0));
		itemRender.renderItem(toDisplay, ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();

		FontRenderer fontRenderer = minecraft.fontRenderer;
		String help = I18n.format("info.fluxnetworks.jei.leftclickhelp", Blocks.OBSIDIAN.getLocalizedName());
		fontRenderer.drawString(help, (int) (64 - fontRenderer.getStringWidth(help) / 2f), 68, 0xff404040);
	}

	@Nonnull
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		if(mouseX > 40 && mouseX < 80 && mouseY > 8 && mouseY < 60) {
			return Lists.newArrayList(
					"Y+2 = " + Blocks.OBSIDIAN.getLocalizedName(),
					"Y+1 = " + DEFeatures.draconiumDust.getDefaultInstance().getDisplayName(),
					"Y+0 = " + Blocks.BEDROCK.getLocalizedName()
			);
		}

		return Collections.emptyList();
	}

	@Override
	public void getIngredients(IIngredients iIngredients) {
		iIngredients.setInput(VanillaTypes.ITEM, input);
		iIngredients.setOutput(VanillaTypes.ITEM, output);
	}

}
