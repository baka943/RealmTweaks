package baka943.realmtweaks.common.block;

import baka943.realmtweaks.client.core.handler.ModelHandler;
import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockMod extends Block implements IModelRegister {

	public BlockMod(Material material, String name) {
		super(material);
		this.setRegistryName(Utils.getRL(name));
		this.setTranslationKey(LibMisc.MOD_ID + "." + name);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		if(Item.getItemFromBlock(this) != Items.AIR) {
			ModelHandler.registerInventoryVariant(this);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean eventReceived(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, int id, int param) {
		TileEntity tileentity = world.getTileEntity(pos);
		super.eventReceived(state, world, pos, id, param);

		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}

}
