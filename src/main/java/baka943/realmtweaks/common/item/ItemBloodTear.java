package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBloodTear extends ItemMod{

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

}
