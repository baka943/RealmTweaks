package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.handler.ItemTooltipHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPaperTool extends ItemMod {

	public ItemPaperTool(String name) {
		super(name);
		this.setMaxStackSize(1);
		this.setCreativeTab(null);
	}

	@Override
	public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull IBlockState state) {
		return 0.0F;
	}

	@Override
	public boolean canDestroyBlockInCreative(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull ItemStack stack, @Nonnull EntityPlayer player) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag) {
		ItemStack originalStack = this.getOriginalStack(stack);

		if(!originalStack.isEmpty() && originalStack.getItem() != Items.AIR) {
			list.addAll(ItemTooltipHandler.splitTooltip(net.minecraft.util.text.translation.I18n.translateToLocalFormatted("tooltip." + LibMisc.MOD_ID + ".paper_tool_original", originalStack.getRarity().color + originalStack.getDisplayName()).trim(), 0));
		}

		list.addAll(ItemTooltipHandler.splitTooltip(I18n.format("tooltip." + LibMisc.MOD_ID + ".paper_tool"), 0));
	}

	public void setOriginalStack(ItemStack stack, ItemStack originalStack) {
		stack.setTagInfo("originalStack", originalStack.writeToNBT(new NBTTagCompound()));
	}

	public ItemStack getOriginalStack(ItemStack stack) {
		return stack.getTagCompound() != null ? new ItemStack(stack.getTagCompound().getCompoundTag("originalStack")) : ItemStack.EMPTY;
	}

}
