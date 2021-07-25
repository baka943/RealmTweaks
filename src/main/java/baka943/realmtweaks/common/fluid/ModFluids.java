package baka943.realmtweaks.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

	public static final Fluid ENDER_ESSENCE = new FluidMod("ender_essence");

	public static void registerFluids() {
		registerFluid(ENDER_ESSENCE);
	}

	private static void registerFluid(Fluid fluid) {
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
	}

}
