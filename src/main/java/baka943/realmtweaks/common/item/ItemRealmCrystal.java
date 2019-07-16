package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.block.ModBlocks;
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
import thebetweenlands.common.capability.circlegem.CircleGemType;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.SoundRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemRealmCrystal extends ItemMod {

	public final CircleGemType type;

	public ItemRealmCrystal(CircleGemType type) {
		super(type.name + "_crystal");
		this.maxStackSize = 1;
		this.type = type;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);

		if(!playerIn.canPlayerEdit(pos, facing, stack)) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState state = worldIn.getBlockState(pos);

			if(!worldIn.isRemote && state.getBlock() == BlockRegistry.SAPLING_WEEDWOOD) {
				if(this.type == CircleGemType.NONE) {
					playerIn.sendStatusMessage(new TextComponentTranslation("chat.realmtweaks.wrong"), true);
				} else {
					if(!playerIn.capabilities.isCreativeMode) stack.shrink(1);

					worldIn.setBlockToAir(pos);
					worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundRegistry.RIFT_OPEN, SoundCategory.PLAYERS, 0.5F, itemRand.nextFloat() * 0.4F + 0.8F);

					switch(this.type) {
						case AQUA:
							worldIn.setBlockState(pos,
									ModBlocks.ANOTHER_SAPLING.getDefaultState());
							CriterionHandler.REALM_ANOTHER.trigger((EntityPlayerMP) playerIn);
							break;

						case CRIMSON:
							worldIn.setBlockState(pos,
									ModBlocks.ALTERNATE_SAPLING.getDefaultState());
							CriterionHandler.REALM_ALTERNATE.trigger((EntityPlayerMP) playerIn);
							break;

						case GREEN:
							worldIn.setBlockState(pos,
									ModBlocks.NIGHTMARE_SAPLING.getDefaultState());
							CriterionHandler.REALM_NIGHTMARE.trigger((EntityPlayerMP) playerIn);
							break;
					}
				}
			}

			return EnumActionResult.SUCCESS;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
		list.addAll(ItemTooltipHandler.splitTooltip(I18n.format("tooltip." + LibMisc.MOD_ID + ".realm_crystal"), 0));
	}

}
