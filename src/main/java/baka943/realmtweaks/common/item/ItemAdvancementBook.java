package baka943.realmtweaks.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemAdvancementBook extends ItemMod{

	public ItemAdvancementBook() {
		super("advancement_book");
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand) {
		if(worldIn.isRemote) {
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityPlayerSP playerSP = minecraft.player;

			minecraft.displayGuiScreen(new GuiScreenAdvancements(playerSP.connection.getAdvancementManager()));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}

}
