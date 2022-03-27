package baka943.realmtweaks.common.integrations;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerTools;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class TinkersTweaks {

	public static void init() {
		if(TConstruct.pulseManager.isPulseLoaded(TinkerSmeltery.PulseId)) {
			TinkerRegistry.registerBasinCasting(new ItemStack(Blocks.OBSIDIAN), ItemStack.EMPTY, FluidRegistry.LAVA, 1000);
		}
	}

	@SubscribeEvent
	public static void tableCastingRecipe(TinkerRegisterEvent.TableCastingRegisterEvent event) {
		if(event.getRecipe() instanceof CastingRecipe) {
			CastingRecipe recipe = (CastingRecipe) event.getRecipe();
			ItemStack result = recipe.getResult();
			FluidStack fluid = recipe.getFluid();

			if(!result.isEmpty() && result.getItem() == TinkerSmeltery.cast && fluid.getFluid() != TinkerFluids.obsidian && fluid.getFluid() != FluidRegistry.LAVA) {
				TinkerRegistry.registerTableCasting(new CastingRecipe(result, recipe.cast, TinkerFluids.obsidian, 144, true, false));
				TinkerRegistry.registerTableCasting(new CastingRecipe(result, recipe.cast, FluidRegistry.LAVA, 500, true, false));

				event.setCanceled(true);
			}

			if(!result.isEmpty() && result.getItem() instanceof IToolPart && result.getItem() != TinkerTools.boltCore && fluid.getFluid() == TinkerFluids.obsidian) {
				if(recipe.cast.getInputs().get(0).getItem() == TinkerSmeltery.clayCast) {
					TinkerRegistry.registerTableCasting(new CastingRecipe(result, recipe.cast, FluidRegistry.LAVA, fluid.amount * 1000 / 288, true, false));
				} else {
					TinkerRegistry.registerTableCasting(new CastingRecipe(result, recipe.cast, FluidRegistry.LAVA, fluid.amount * 1000 / 288, false, false));
				}
			}
		}
	}

}
