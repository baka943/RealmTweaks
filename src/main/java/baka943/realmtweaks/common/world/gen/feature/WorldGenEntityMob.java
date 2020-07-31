package baka943.realmtweaks.common.world.gen.feature;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.entity.monster.EntityEnderSkeleton;
import baka943.realmtweaks.common.entity.monster.EntityEnderZombie;
import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import thebetweenlands.common.registries.BiomeRegistry;
import thebetweenlands.common.world.biome.BiomeBetweenlands;

public class WorldGenEntityMob {

	public static void init() {
		Biomes.DEEP_OCEAN.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityGuardian.class, 100, 2, 5));

		if(RealmTweaks.isBetterNetherLoaded) {
			Biomes.HELL.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityBlaze.class, 50, 2, 5));
			Biomes.HELL.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityWitherSkeleton.class, 20, 2, 5));
		}

		if(RealmTweaks.isBloodMagicLoaded) {
			Biomes.SKY.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityEnderman.class, 90, 4, 4));
			Biomes.SKY.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityEnderSkeleton.class, 5, 1, 1));
			Biomes.SKY.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityEnderZombie.class, 5, 1, 2));
		}

		if(RealmTweaks.isBetweenlandsLoaded) {
			for(BiomeBetweenlands biome : BiomeRegistry.REGISTERED_BIOMES) {
				biome.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntitySwampSpider.class, 500, 2, 5));
			}
		}
	}

}
