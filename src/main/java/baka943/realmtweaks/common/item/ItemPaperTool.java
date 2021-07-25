package baka943.realmtweaks.common.item;

import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.handler.ItemTooltipHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPaperTool extends ItemSword implements IModelRegister {

	public ItemPaperTool() {
		super(ToolMaterial.IRON);
		this.setRegistryName(Utils.getRL("paper_tool"));
		this.setTranslationKey(LibMisc.MOD_ID + ".paper_tool");
		this.setMaxDamage(-1);
		this.setCreativeTab(null);
	}

	@Override
	public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull IBlockState state) {
		return 0.0F;
	}

	@Override
	public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, @Nonnull Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean isEnchantable(@Nonnull ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag) {
		ItemStack originalStack = this.getOriginalStack(stack);

		if(!originalStack.isEmpty() && originalStack.getItem() != Items.AIR) {
			list.addAll(ItemTooltipHandler.splitTooltip(I18n.format("tooltip." + LibMisc.MOD_ID + ".paper_tool_original", originalStack.getRarity().color + originalStack.getDisplayName()).trim(), 0));
		}

		list.addAll(ItemTooltipHandler.splitTooltip(I18n.format("tooltip." + LibMisc.MOD_ID + ".paper_tool"), 0));
	}

	public ItemStack getOriginalStack(ItemStack stack) {
		return stack.getTagCompound() != null ? new ItemStack(stack.getTagCompound().getCompoundTag("originalStack")) : ItemStack.EMPTY;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
