package baka943.realmtweaks.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

	public static final Fluid OCTINE = new FluidMod("octine");
	public static final Fluid SYRMORITE = new FluidMod("syrmorite");
	public static final Fluid LIFE_CORE = new FluidMod("lifecore");

	public static void registerFluids() {
		registerFluid(OCTINE);
		registerFluid(SYRMORITE);
		registerFluid(LIFE_CORE);
	}

	private static void registerFluid(Fluid fluid) {
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
	}

}
