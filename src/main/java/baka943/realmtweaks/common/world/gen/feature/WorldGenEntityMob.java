package baka943.realmtweaks.common.world.gen.feature;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import thebetweenlands.common.registries.BiomeRegistry;
import thebetweenlands.common.world.biome.BiomeBetweenlands;

public class WorldGenEntityMob {

	public static void init() {
		Biomes.DEEP_OCEAN.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityGuardian.class, 100, 2, 5));

		Biomes.HELL.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityBlaze.class, 100, 2, 5));

		for(BiomeBetweenlands biome : BiomeRegistry.REGISTERED_BIOMES) {
			biome.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityCaveSpider.class, 280, 2, 5));
		}
	}

}
