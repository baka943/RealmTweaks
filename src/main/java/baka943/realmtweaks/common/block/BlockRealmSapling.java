package baka943.realmtweaks.common.block;

import baka943.realmtweaks.client.core.handler.ModelHandler;
import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.item.misc.ItemSwampTalisman;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.common.world.gen.feature.structure.WorldGenWeedwoodPortalTree;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockRealmSapling extends BlockBush implements IModelRegister {

	private static final AxisAlignedBB REALM_SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

	private final int dim;

	public BlockRealmSapling(String name, int dim) {
		super();
		this.setRegistryName(Utils.getRL(name));
		this.setTranslationKey(LibMisc.MOD_ID + "." + name);
		this.setCreativeTab(BLCreativeTabs.PLANTS);
		this.setSoundType(SoundType.PLANT);

		this.dim = dim;
	}

	public int getDim() {
		return dim;
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
		return REALM_SAPLING_AABB;
	}

	@Override
	public boolean onBlockActivated(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);

		if(!worldIn.isRemote && (ItemSwampTalisman.EnumTalisman.SWAMP_TALISMAN_0.isItemOf(stack) || ItemSwampTalisman.EnumTalisman.SWAMP_TALISMAN_5.isItemOf(stack))) {
			WorldGenWeedwoodPortalTree gen;

			if(worldIn.provider.getDimension() == Utils.getDimId("betweenlands")) {
				gen = new WorldGenWeedwoodPortalTree(this.getDim());
			} else {
				gen = new WorldGenWeedwoodPortalTree(worldIn.provider.getDimension());
			}

			if(gen.generate(worldIn, worldIn.rand, pos)) {
				worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundRegistry.PORTAL_ACTIVATE, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.4F + 0.8F);
				playerIn.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 2D, pos.getZ() + 0.5D, playerIn.rotationYaw, playerIn.rotationPitch);

				if(playerIn instanceof EntityPlayerMP) {
					((EntityPlayerMP)playerIn).connection.setPlayerLocation(pos.getX() + 0.5D, pos.getY() + 2D, pos.getZ() + 0.5D, playerIn.rotationYaw, playerIn.rotationPitch);
				}
			} else {
				playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.noplace"), true);
			}
		}

		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(@Nonnull IBlockState stateIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
		for (int i = 0; i < 3; ++i) {
			int j = rand.nextInt(4) - 1, k = rand.nextInt(4) - 1;

			double posX = pos.getX() + 0.5D + 0.25D * j;
			double posY = pos.getY() + rand.nextFloat();
			double posZ = pos.getZ() + 0.5D + 0.25D * k;
			double xV = rand.nextFloat() * j;
			double yV = (rand.nextFloat() - 0.5D) * 0.125D;
			double zV = rand.nextFloat() * k;

			worldIn.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, xV, yV, zV);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		if(Item.getItemFromBlock(this) != Items.AIR) {
			ModelHandler.registerInventoryVariant(this);
		}
	}

}
