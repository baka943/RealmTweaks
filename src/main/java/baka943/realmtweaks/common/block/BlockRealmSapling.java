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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.handler.ItemTooltipHandler;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.capability.circlegem.CircleGemType;
import thebetweenlands.common.item.misc.ItemGem;
import thebetweenlands.common.world.WorldProviderBetweenlands;

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

	public int getDim() {
		return dim;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		Item item = stack.getItem();

		if(!world.isRemote && item instanceof ItemGem) {
			if(world.provider instanceof WorldProviderBetweenlands) {
				CircleGemType type = ((ItemGem) item).type;

				if(type == CircleGemType.NONE) {
					player.sendStatusMessage(new TextComponentTranslation("chat.realmtweaks.wrong"), true);
				} else {
					if(!player.capabilities.isCreativeMode) stack.shrink(1);

					switch(type) {
						case AQUA:
							if(this != ModBlocks.NIGHTMARE_SAPLING) {
								world.setBlockToAir(pos);
								world.setBlockState(pos,
										ModBlocks.NIGHTMARE_SAPLING.getDefaultState());
								Utils.worldSaplingText(player, ModBlocks.NIGHTMARE_SAPLING);
							}

							break;

						case CRIMSON:
							if(this != ModBlocks.ALTERNATE_SAPLING) {
								world.setBlockToAir(pos);
								world.setBlockState(pos,
										ModBlocks.ALTERNATE_SAPLING.getDefaultState());
								Utils.worldSaplingText(player, ModBlocks.ALTERNATE_SAPLING);
							}

							break;

						case GREEN:
							if(this != ModBlocks.ANOTHER_SAPLING) {
								world.setBlockToAir(pos);
								world.setBlockState(pos,
										ModBlocks.ANOTHER_SAPLING.getDefaultState());
								Utils.worldSaplingText(player, ModBlocks.ANOTHER_SAPLING);
							}

							break;
					}
				}
			} else {
				player.sendStatusMessage(new TextComponentTranslation("chat.realmtweaks.wrong"), true);
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
		list.addAll(ItemTooltipHandler.splitTooltip(I18n.format("tooltip." + LibMisc.MOD_ID + "." + this.name), 0));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		if(Item.getItemFromBlock(this) != Items.AIR) {
			ModelHandler.registerInventoryVariant(this);
		}
	}

}
