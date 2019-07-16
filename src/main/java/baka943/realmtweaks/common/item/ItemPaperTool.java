package baka943.realmtweaks.common.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

public class ItemPaperTool extends ItemMod {

	public ItemPaperTool() {
		super("paper_tool");
		this.setMaxStackSize(1);
		this.setCreativeTab(null);
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public String getItemStackDisplayName(@Nonnull ItemStack stack) {
		ItemStack originalStack = this.getOriginalStack(stack);

		if(!originalStack.isEmpty() && originalStack.getItem() != Items.AIR) {
			return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".name", originalStack.getRarity().color + originalStack.getDisplayName()).trim();
		}

		return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + "_empty.name").trim();
	}

	public void setOriginalStack(ItemStack stack, ItemStack originalStack) {
		stack.setTagInfo("originalStack", originalStack.writeToNBT(new NBTTagCompound()));
	}

	public ItemStack getOriginalStack(ItemStack stack) {
		return stack.getTagCompound() != null ? new ItemStack(stack.getTagCompound().getCompoundTag("originalStack")) : ItemStack.EMPTY;
	}

}
