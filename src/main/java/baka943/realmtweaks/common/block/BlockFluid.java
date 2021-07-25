package baka943.realmtweaks.common.block;

import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockFluid extends BlockFluidClassic implements IModelRegister {

	public BlockFluid(Fluid fluid, Material material, int quantaPerBlock) {
		super(fluid, material);

		this.setRegistryName(fluid.getName());
		this.setTranslationKey(LibMisc.MOD_ID + "." + fluid.getName());
		fluid.setBlock(this);
		this.setQuantaPerBlock(quantaPerBlock);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(LibMisc.MOD_ID + ":fluid", stack.getFluid().getName());

		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {

			@Override
			@Nonnull
			protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
				return modelResourceLocation;
			}

		});
	}

}
