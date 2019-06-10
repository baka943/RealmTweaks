package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.handler.ItemTooltipHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemAdvancementBook extends ItemMod {

	public ItemAdvancementBook() {
		super("advancement_book");
	}

	@Override
	@Nonnull
	@SideOnly(Side.CLIENT)
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
		if(worldIn.isRemote) {
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityPlayerSP playerSP = minecraft.player;

			minecraft.displayGuiScreen(new GuiScreenAdvancements(playerSP.connection.getAdvancementManager()));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
		list.addAll(ItemTooltipHandler.splitTooltip(I18n.format("tooltip." + LibMisc.MOD_ID + "." + this.getRegistryName().getPath(), new Object[0]), 0));
	}
}
