package baka943.realmtweaks.common.item;

import WayofTime.bloodmagic.altar.IAltarManipulator;
import WayofTime.bloodmagic.altar.IBloodAltar;
import baka943.realmtweaks.common.block.ModBlocks;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
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
	public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);

		if(!player.canPlayerEdit(pos, facing, stack)) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState state = worldIn.getBlockState(pos);
			Block block = state.getBlock();

			if(!worldIn.isRemote && block == Blocks.END_STONE) {
				worldIn.setBlockState(pos, ModBlocks.BLOODED_ENDSTONE.getDefaultState());

				if(!player.capabilities.isCreativeMode) stack.shrink(1);

				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.FAIL;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		RayTraceResult rayTrace = this.rayTrace(world, player, false);
		BlockPos pos = rayTrace.getBlockPos();

		if(!world.isRemote) {
			if(rayTrace == null) {
				return super.onItemRightClick(world, player, hand);
			} else {
				if(rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
					TileEntity tile = world.getTileEntity(pos);

					if(tile instanceof IBloodAltar && !((IBloodAltar)tile).isActive()) {
						if(!player.capabilities.isCreativeMode) stack.shrink(1);

						((IBloodAltar)tile).fillMainTank(250);
						world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
					} else return super.onItemRightClick(world, player, hand);
				}
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
