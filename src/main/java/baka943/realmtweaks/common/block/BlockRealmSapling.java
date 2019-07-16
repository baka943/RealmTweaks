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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.handler.ItemTooltipHandler;
import thebetweenlands.client.tab.BLCreativeTabs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

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

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return REALM_SAPLING_AABB;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
		list.addAll(ItemTooltipHandler.splitTooltip(I18n.format("tooltip." + LibMisc.MOD_ID + "." + this.name), 0));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		for (int i = 0; i < 3; ++i) {
			int j = rand.nextInt(4) - 1;
			int k = rand.nextInt(4) - 1;

			double posX = (double)pos.getX() + 0.5D + 0.25D * (double)j;
			double posY = (double)((float)pos.getY() + rand.nextFloat());
			double posZ = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
			double xV = (double)(rand.nextFloat() * (float)j);
			double yV = ((double)rand.nextFloat() - 0.5D) * 0.125D;
			double zV = (double)(rand.nextFloat() * (float)k);

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
