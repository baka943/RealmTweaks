package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.block.BlockRealmSapling;
import baka943.realmtweaks.common.core.handler.CriterionHandler;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.handler.ItemTooltipHandler;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.common.world.gen.feature.structure.WorldGenWeedwoodPortalTree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ItemRealmKey extends ItemMod {

	public ItemRealmKey() {
		super("realm_key");
		this.maxStackSize = 1;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);

		if(!playerIn.canPlayerEdit(pos, facing, stack)) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState state = worldIn.getBlockState(pos);

			if(!worldIn.isRemote && this.isRealmSapling(state)) {
				if(!BetweenlandsConfig.WORLD_AND_DIMENSION.portalDimensionWhitelistSet.isListed(playerIn.world.provider.getDimension())) {
					playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.wrongdimension"), true);
				} else {
					WorldGenWeedwoodPortalTree gen;
					int targetDim = ((BlockRealmSapling) state.getBlock()).getDim();

					if(targetDim == playerIn.world.provider.getDimension()) {
						gen = new WorldGenWeedwoodPortalTree();
					} else gen = new WorldGenWeedwoodPortalTree(targetDim);

					if(gen.generate(worldIn, itemRand, pos)) {
						worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundRegistry.PORTAL_ACTIVATE, SoundCategory.PLAYERS, 0.5F, itemRand.nextFloat() * 0.4F + 0.8F);
						playerIn.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 2D, pos.getZ() + 0.5D, playerIn.rotationYaw, playerIn.rotationPitch);

						if(playerIn instanceof EntityPlayerMP) {
							((EntityPlayerMP)playerIn).connection.setPlayerLocation(pos.getX() + 0.5D, pos.getY() + 2D, pos.getZ() + 0.5D, playerIn.rotationYaw, playerIn.rotationPitch);

							CriterionHandler.REALM_KEY.trigger((EntityPlayerMP) playerIn);
						}
					} else {
						playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.noplace"), true);
					}
				}
			}

			return EnumActionResult.SUCCESS;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flagIn) {
		list.addAll(ItemTooltipHandler.splitTooltip(I18n.format("tooltip." + LibMisc.MOD_ID + ".realm_key"), 0));
	}

	private boolean isRealmSapling(IBlockState state) {
		return state.getBlock() instanceof BlockRealmSapling;
	}

}
