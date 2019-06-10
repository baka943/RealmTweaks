package baka943.realmtweaks.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEndPortal extends net.minecraft.block.BlockEndPortal {

	public BlockEndPortal() {
		super(Material.PORTAL);
		this.setRegistryName(new ResourceLocation("end_portal"));
		this.setTranslationKey("end_portal");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {}

}
