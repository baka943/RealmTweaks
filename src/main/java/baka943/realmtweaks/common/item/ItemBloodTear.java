package baka943.realmtweaks.common.item;

import WayofTime.bloodmagic.altar.IAltarManipulator;
import WayofTime.bloodmagic.altar.IBloodAltar;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Optional.Interface(modid = "bloodmagic", iface = "WayofTime.bloodmagic.altar.IAltarManipulator", striprefs = true)
public class ItemBloodTear extends ItemMod implements IAltarManipulator {

	public ItemBloodTear() {
		super("blood_tear");
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		RayTraceResult rayTrace = this.rayTrace(world, player, false);
		BlockPos pos = rayTrace.getBlockPos();
		IBlockState state = world.getBlockState(pos);

		if(rayTrace == null) {
			return super.onItemRightClick(world, player, hand);
		} else if(rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
			TileEntity tile = world.getTileEntity(pos);

			if(tile instanceof IBloodAltar && !((IBloodAltar) tile).isActive()) {
				if(!player.capabilities.isCreativeMode) stack.shrink(1);

				((IBloodAltar) tile).fillMainTank(250);
				world.notifyBlockUpdate(pos, state, state, 3);
			}
		}

		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
		tooltip.add(I18n.format("tooltip." + LibMisc.MOD_ID + ".blood_tear"));
		tooltip.add(I18n.format("tooltip." + LibMisc.MOD_ID + ".blood_tear.stored", 250));
	}

}
