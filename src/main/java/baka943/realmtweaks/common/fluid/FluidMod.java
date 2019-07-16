package baka943.realmtweaks.common.fluid;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;

public class FluidMod extends Fluid {

	public FluidMod(String name) {
		super(name, new ResourceLocation(LibMisc.MOD_ID + ":fluids/" + name + "_still"), new ResourceLocation(LibMisc.MOD_ID + ":fluids/" + name + "_flowing"));
		this.setUnlocalizedName(LibMisc.MOD_ID + "." + name);
		this.setDensity(4000);
		this.setViscosity(3000);
		this.setTemperature(550);
		this.setLuminosity(15);
	}

	@Override
	public int getColor() {
		return Color.WHITE.getRGB();
	}

}
