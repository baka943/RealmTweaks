package baka943.realmtweaks.common.block;

import baka943.realmtweaks.client.core.handler.ModelHandler;
import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.handler.ItemTooltipHandler;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.item.misc.ItemSwampTalisman.EnumTalisman;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.common.world.WorldProviderBetweenlands;
import thebetweenlands.common.world.gen.feature.structure.WorldGenWeedwoodPortalTree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockRealmSapling extends BlockBush implements IModelRegister {

	private static final AxisAlignedBB REALM_SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

	private String name;
	private int dim;

	public BlockRealmSapling(String name, int dim) {
		super();
		this.setRegistryName(Utils.getRL(name));
		this.setTranslationKey(LibMisc.MOD_ID + "." + name);
		this.setCreativeTab(BLCreativeTabs.PLANTS);
		this.setSoundType(SoundType.PLANT);

		this.name = name;
		this.dim = dim;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);

		if(!worldIn.isRemote && (EnumTalisman.SWAMP_TALISMAN_0.isItemOf(stack)
				|| EnumTalisman.SWAMP_TALISMAN_5.isItemOf(stack))) {
			if(worldIn.provider instanceof WorldProviderBetweenlands) {
				WorldGenWeedwoodPortalTree gen = new WorldGenWeedwoodPortalTree(dim);

				if(gen.generate(worldIn, worldIn.rand, pos)) {
					double X = pos.getX();
					double Y = pos.getY() + 2;
					double Z = pos.getZ();

					worldIn.playSound(null, pos, SoundRegistry.PORTAL_ACTIVATE, SoundCategory.PLAYERS, 0.5F, 1.0F);
					playerIn.setLocationAndAngles(X, Y, Z, playerIn.rotationYaw, playerIn.rotationPitch);

					if(playerIn instanceof EntityPlayerMP) {
						((EntityPlayerMP) playerIn).connection.setPlayerLocation(X, Y, Z, playerIn.rotationYaw, playerIn.rotationPitch);
					}
				} else {
					playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.noplace"), true);
				}
			} else {
				playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.wrongdimension"), true);
			}
		}

		return false;
	}

	@Override
	@Nonnull
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return REALM_SAPLING_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
		list.addAll(ItemTooltipHandler.splitTooltip(I18n.format("tooltip." + LibMisc.MOD_ID + "." + this.name, new Object[0]), 0));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		if(Item.getItemFromBlock(this) != Items.AIR) {
			ModelHandler.registerInventoryVariant(this);
		}
	}

}
