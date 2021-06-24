package baka943.realmtweaks.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thebetweenlands.common.config.BetweenlandsConfig;

import javax.annotation.Nonnull;

public class BlockEndPortal extends net.minecraft.block.BlockEndPortal {

	protected BlockEndPortal() {
		super(Material.PORTAL);
		this.setRegistryName(new ResourceLocation("end_portal"));
		this.setTranslationKey("end_portal");
	}

	@Override
	public void onEntityCollision(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entityIn) {
		if(!worldIn.isRemote && !entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss() && entityIn.getEntityBoundingBox().intersects(state.getBoundingBox(worldIn, pos).offset(pos))) {
			entityIn.changeDimension(1);
		}
	}

}
