package baka943.realmtweaks.common.block;

import baka943.realmtweaks.client.core.handler.ModelHandler;
import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBloodedEndstone extends Block implements IModelRegister {

	public BlockBloodedEndstone() {
		super(Material.ICE);
		this.setRegistryName(Utils.getRL("blooded_end_stone"));
		this.setTranslationKey(LibMisc.MOD_ID + ".blooded_end_stone");
		this.setSoundType(SoundType.STONE);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
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
				worldIn.setBlockState(pos, ModBlocks.LIFE_CORE.getDefaultState());
			}
		}
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(rand.nextInt(1) == 0) {
			this.turnIntoBlood(worldIn, pos);
		}
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if(!worldIn.isRemote && entityIn instanceof EntityPlayer && fallDistance > 0.5F && worldIn.rand.nextInt(6) == 0) {
			this.turnIntoBlood(worldIn, pos);
		}
	}

	private void turnIntoBlood(World world, BlockPos pos) {
		if (world.provider.doesWaterVaporize()) {
			world.setBlockToAir(pos);
		} else {
			this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
			world.setBlockState(pos, ModBlocks.LIFE_CORE.getDefaultState());
			world.neighborChanged(pos, ModBlocks.LIFE_CORE, pos);
		}
	}

	@Override
	public EnumPushReaction getPushReaction(IBlockState state) {
		return EnumPushReaction.NORMAL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		if(Item.getItemFromBlock(this) != Items.AIR) {
			ModelHandler.registerInventoryVariant(this);
		}
	}

}
