package baka943.realmtweaks.common.world;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldTypeAtlantis extends WorldType {

	public WorldTypeAtlantis() {
		super("atlantis");
	}

	@Nonnull
	@Override
	public BiomeProvider getBiomeProvider(@Nonnull World world) {
		return new BiomeProviderSingle(Biomes.DEEP_OCEAN);
	}

	@Nonnull
	@Override
	public String getTranslationKey() {
		return "generator." + LibMisc.MOD_ID + "_" + this.getName();
	}

	@Override
	public boolean handleSlimeSpawnReduction(@Nonnull Random rand, World world) {
		return false;
	}

}
