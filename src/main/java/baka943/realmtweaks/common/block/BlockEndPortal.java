package baka943.realmtweaks.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockEndPortal extends net.minecraft.block.BlockEndPortal {

	protected BlockEndPortal() {
		super(Material.WATER);
		this.setRegistryName(new ResourceLocation("end_portal"));
		this.setTranslationKey("end_portal");
	}

	@Override
	public void onEntityCollision(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entityIn) {
		this.turnIntoEnder(worldIn, pos);
	}

	@Override
	public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
		if(rand.nextInt() == 0) {
			this.turnIntoEnder(worldIn, pos);
		}
	}

	private void turnIntoEnder(@Nonnull World world, BlockPos pos) {
		if (world.provider.doesWaterVaporize()) {
			world.setBlockToAir(pos);
		} else {
			world.setBlockState(pos, ModBlocks.ENDER_ESSENCE.getDefaultState());
			world.neighborChanged(pos, ModBlocks.ENDER_ESSENCE, pos);
		}
	}

}
