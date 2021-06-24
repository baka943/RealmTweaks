package baka943.realmtweaks.common.world.gen.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStronghold;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MapGenStrongholdEmpty extends MapGenStronghold {

	@Override
	public void generate(@Nonnull World world, int x, int z, @Nullable ChunkPrimer primer) {}

	@Override
	public BlockPos getNearestStructurePos(@Nonnull World worldIn, @Nullable BlockPos pos, boolean findUnexplored) {
		return new BlockPos(0, 93, 0);
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return false;
	}

}
