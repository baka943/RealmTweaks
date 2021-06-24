package baka943.realmtweaks.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

	public static final Fluid IMPURE_LIFE_ESSENCE = new FluidMod("impure_life_essence");

	public static void registerFluids() {
		registerFluid(IMPURE_LIFE_ESSENCE);
	}

	private static void registerFluid(Fluid fluid) {
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
	}

}
