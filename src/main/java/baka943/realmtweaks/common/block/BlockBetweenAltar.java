package baka943.realmtweaks.common.block;

import baka943.realmtweaks.common.block.tile.TileBetweenAltar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.common.registries.FluidRegistry;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockBetweenAltar extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.125, 0.125, 0.125, 0.875, 20.0/16, 0.875);

	protected BlockBetweenAltar() {
		super(Material.ROCK, "between_altar");
		setHardness(3.5F);
		setSoundType(SoundType.STONE);
//		setDefaultState(blockState.getBaseState()
//				.withProperty(BotaniaStateProps.ALTAR_VARIANT, AltarVariant.DEFAULT));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return AABB;
	}

//	@Nonnull
//	@Override
//	public BlockStateContainer createBlockState() {
//		return new BlockStateContainer(this, BotaniaStateProps.ALTAR_VARIANT);
//	}

//	@Override
//	public int getMetaFromState(IBlockState state) {
//		return state.getValue(BotaniaStateProps.ALTAR_VARIANT).ordinal();
//	}

//	@Nonnull
//	@Override
//	public IBlockState getStateFromMeta(int meta) {
//		if (meta < 0 || meta >= AltarVariant.values().length ) {
//			meta = 0;
//		}
//		return getDefaultState().withProperty(BotaniaStateProps.ALTAR_VARIANT, AltarVariant.values()[meta]);
//	}

	@Nonnull
	@Override
	public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
		TileEntity te = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);

		return state;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

//	@Override
//	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
//		for(int i = 0; i < 9; i++)
//			list.add(new ItemStack(this, 1, i));
//	}

	@Override
	public void onEntityCollision(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entity) {
		if(!world.isRemote && entity instanceof EntityItem) {
			TileBetweenAltar tile = (TileBetweenAltar) world.getTileEntity(pos);
			if(tile.collideEntityItem((EntityItem) entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		}
	}

	@Override
	public int getLightValue(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return 0;
	}

	@Override
	public boolean onBlockActivated(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing par6, float par7, float par8, float par9) {
		TileBetweenAltar tile = (TileBetweenAltar)world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);

		if(player.isSneaking()) {
			InventoryHelper.withdrawFromInventory(tile, player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			return true;
		} else {
			if(!stack.isEmpty() && isValidWaterContainer(stack)) {
				if(!tile.hasWater) {
					if(!player.capabilities.isCreativeMode)
						player.setHeldItem(hand, drain(FluidRegistry.SWAMP_WATER, stack));

					tile.setWater(true);
					world.updateComparatorOutputLevel(pos, this);
					world.checkLight(pos);
				}

				return true;
//			} else if(!stack.isEmpty() && stack.getItem() == Items.BUCKET && tile.hasWater && !Botania.gardenOfGlassLoaded) {
//				ItemStack bucket = new ItemStack(Items.WATER_BUCKET);
//				if(stack.getCount() == 1)
//					player.setHeldItem(hand, bucket);
//				else {
//					ItemHandlerHelper.giveItemToPlayer(player, bucket);
//					stack.shrink(1);
//				}
//
//				tile.setWater(false);
//				world.updateComparatorOutputLevel(pos, this);
//				world.checkLight(pos);
//
//				return true;
			}
		}

		return false;
	}

	@Override
	public void fillWithRain(World world, BlockPos pos) {
		if(world.rand.nextInt(20) == 1) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileBetweenAltar) {
				TileBetweenAltar altar = (TileBetweenAltar) tile;
				if(!altar.hasWater)
					altar.setWater(true);
				world.updateComparatorOutputLevel(pos, this);
			}
		}
	}

//	@Override
//	public int damageDropped(IBlockState state) {
//		return getMetaFromState(state);
//	}

	private boolean isValidWaterContainer(ItemStack stack) {
		if(stack.isEmpty() || stack.getCount() != 1)
			return false;

		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			FluidStack simulate = handler.drain(new FluidStack(FluidRegistry.SWAMP_WATER, Fluid.BUCKET_VOLUME), false);
			if(simulate != null && simulate.getFluid() == FluidRegistry.SWAMP_WATER && simulate.amount == Fluid.BUCKET_VOLUME)
				return true;
		}

		return false;
	}

	private ItemStack drain(Fluid fluid, ItemStack stack) {
		IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		handler.drain(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true);
		return handler.getContainer();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileBetweenAltar();
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileBetweenAltar altar = (TileBetweenAltar) world.getTileEntity(pos);
		return altar.hasWater ? 15 : 0;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.apothecary;
	}

//	@SideOnly(Side.CLIENT)
//	@Override
//	public void registerModels() {
//		ModelHandler.registerBlockToState(this, AltarVariant.values().length - 1);
//	}

}
