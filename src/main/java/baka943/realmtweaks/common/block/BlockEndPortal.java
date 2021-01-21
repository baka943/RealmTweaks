package baka943.realmtweaks.common.block;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;

public class BlockEndPortal extends net.minecraft.block.BlockEndPortal {

	protected BlockEndPortal() {
		super(Material.PORTAL);
		this.setRegistryName(new ResourceLocation("end_portal"));
		this.setTranslationKey("end_portal");
	}

	@Override
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
		return null;
	}

	@Override
	public void onEntityCollision(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entityIn) {
		if(entityIn instanceof EntityPlayer && !(entityIn instanceof FakePlayer)) {
			EntityPlayer player = (EntityPlayer)entityIn;
			SoulNetwork network = NetworkHelper.getSoulNetwork(player);

			if(!worldIn.isRemote && network.getOrbTier() == 0) {
				player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 2, 1, false, true));
			}
		}
	}

}
