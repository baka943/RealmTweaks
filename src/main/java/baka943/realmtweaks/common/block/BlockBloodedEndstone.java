package baka943.realmtweaks.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockBloodedEndstone extends BlockMod {

	public BlockBloodedEndstone() {
		super(Material.ICE, "blooded_end_stone");
		this.setSoundType(SoundType.STONE);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void harvestBlock(@Nonnull World worldIn, @Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, @Nonnull ItemStack stack) {
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);

		if(this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
			ItemStack stack1 = this.getItem(worldIn, pos, state);

			if(!stack1.isEmpty()) {
				spawnAsEntity(worldIn, pos, stack1);
			}
		} else {
			Material material = worldIn.getBlockState(pos.down()).getMaterial();

			if (material.blocksMovement() || material.isLiquid()) {
				worldIn.setBlockState(pos, ModBlocks.IMPURE_LIFE_ESSENCE.getDefaultState());
			}
		}
	}

	@Override
	public int quantityDropped(@Nonnull Random random) {
		return 0;
	}

	@Override
	public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
		if(rand.nextInt(1) == 0) {
			this.turnIntoBlood(worldIn, pos);
		}
	}

	@Override
	public void onFallenUpon(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Entity entityIn, float fallDistance) {
		if(!worldIn.isRemote && entityIn instanceof EntityPlayer && fallDistance > 0.5F && worldIn.rand.nextInt(6) == 0) {
			this.turnIntoBlood(worldIn, pos);
		}
	}

	private void turnIntoBlood(@Nonnull World world, BlockPos pos) {
		if (world.provider.doesWaterVaporize()) {
			world.setBlockToAir(pos);
		} else {
			this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
			world.setBlockState(pos, ModBlocks.IMPURE_LIFE_ESSENCE.getDefaultState());
			world.neighborChanged(pos, ModBlocks.IMPURE_LIFE_ESSENCE, pos);
		}
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public EnumPushReaction getPushReaction(@Nonnull IBlockState state) {
		return EnumPushReaction.NORMAL;
	}

}
