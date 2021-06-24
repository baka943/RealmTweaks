package baka943.realmtweaks.common.world.gen.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenMineshaft;

import javax.annotation.Nonnull;

public class MapGenMineshaftEmpty extends MapGenMineshaft {

	@Override
	public void generate(@Nonnull World worldIn, int x, int z, @Nonnull ChunkPrimer primer) {}

	@Override
	public BlockPos getNearestStructurePos(@Nonnull World worldIn, @Nonnull BlockPos pos, boolean findUnexplored) {
		return null;
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return false;
	}

}
