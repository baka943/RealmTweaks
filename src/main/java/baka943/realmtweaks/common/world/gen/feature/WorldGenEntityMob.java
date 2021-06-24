package baka943.realmtweaks.common.world.gen.feature;

import baka943.realmtweaks.common.entity.monster.EntityEnderSkeleton;
import baka943.realmtweaks.common.entity.monster.EntityEnderZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome.SpawnListEntry;

public class WorldGenEntityMob {

	public static void init() {
		Biomes.DEEP_OCEAN.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityGuardian.class, 100, 2, 5));

		Biomes.HELL.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityBlaze.class, 50, 2, 5));
		Biomes.HELL.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityWitherSkeleton.class, 20, 2, 5));

		Biomes.SKY.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityEnderSkeleton.class, 5, 1, 1));
		Biomes.SKY.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityEnderZombie.class, 20, 1, 2));
	}

}
