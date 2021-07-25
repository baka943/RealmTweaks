package baka943.realmtweaks.common.world.gen.feature;

import baka943.realmtweaks.common.entity.monster.EntityEnderSkeleton;
import baka943.realmtweaks.common.entity.monster.EntityEnderZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome.SpawnListEntry;

public class WorldGenEntityMob {

	public static void init() {
		Biomes.SKY.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityEnderSkeleton.class, 5, 1, 1));
		Biomes.SKY.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityEnderZombie.class, 20, 1, 2));
	}

}
