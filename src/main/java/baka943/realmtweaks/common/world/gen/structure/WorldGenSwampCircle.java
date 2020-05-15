package baka943.realmtweaks.common.world.gen.structure;

import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import thebetweenlands.common.block.plant.BlockPoisonIvy;
import thebetweenlands.common.block.structure.BlockDruidStone;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.tile.spawner.MobSpawnerLogicBetweenlands;
import thebetweenlands.common.tile.spawner.TileEntityMobSpawnerBetweenlands;
import thebetweenlands.common.world.gen.feature.structure.WorldGenDruidCircle;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenSwampCircle extends WorldGenDruidCircle {

	private static final IBlockState[] RUNE_STONES;

	static {
		RUNE_STONES = new IBlockState[]{
				BlockRegistry.DRUID_STONE_1.getDefaultState(),
				BlockRegistry.DRUID_STONE_2.getDefaultState(),
				BlockRegistry.DRUID_STONE_3.getDefaultState(),
				BlockRegistry.DRUID_STONE_4.getDefaultState(),
				BlockRegistry.DRUID_STONE_5.getDefaultState()
		};
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == Utils.getDimensionId("betweenlands")) {
			this.generate(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generate(World world, Random random, int startX, int startZ) {
		BlockPos genPos = null;
		MutableBlockPos pos = new MutableBlockPos();

		check:
		for(int xo = 7; xo <= 25; xo++) {
			for(int zo = 7; zo <= 25; zo++) {
				int x = startX + xo;
				int z = startZ + zo;

				if(world.isAreaLoaded(new BlockPos(x - 8, 64, z - 8), new BlockPos(x + 9, 64, z + 9))) {
					pos.setPos(x, 0, z);
					Biome biome = world.getBiome(pos);

					if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
						int newY = world.getHeight(pos).getY() - 1;
						pos.setY(newY);
						IBlockState block = world.getBlockState(pos);

						if (block == biome.topBlock) {
							if(this.canGenerateAt(world, pos.up())) {
								genPos = pos.up();

								break check;
							}
						}
					}
				}
			}
		}

		if(genPos != null && random.nextInt(BetweenlandsConfig.WORLD_AND_DIMENSION.druidCircleFrequency) == 0) {
			generateStructure(world, random, genPos);
		}
	}

	@Override
	public void generateStructure(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos altar) {
		MutableBlockPos pos = new MutableBlockPos();
		IBlockState ground = world.getBiome(altar).topBlock;
		IBlockState filler = world.getBiome(altar).fillerBlock;
		int altarX = altar.getX(), altarY = altar.getY(), altarZ = altar.getZ();

		for(int x = -6; x <= 6; x++) {
			for(int z = -6; z <= 6; z++) {
				pos.setPos(altarX + x, altarY, altarZ + z);
				int dSq = (int)Math.round(Math.sqrt(x * x + z * z));

				if(dSq == 6) {
					if(x % 2 == 0 && z % 2 == 0) {
						placePillar(world, pos, rand);
					} else {
						placeAir(world, pos);
					}
				}

				if(dSq <= 6) {
					for(int yo = 0; yo < 16; yo++) {
						Biome biome = world.getBiomeForCoordsBody(pos);
						IBlockState state = world.getBlockState(pos);

						if(state == biome.topBlock || state == biome.fillerBlock || state.getMaterial() == Material.ROCK || state.getMaterial() == Material.GROUND) {
							world.setBlockToAir(pos.toImmutable());
						}

						pos.setY(pos.getY() + 1);
					}

					pos.setY(altarY - 1);
					world.setBlockState(pos.toImmutable(), ground);

					int offset = world.rand.nextInt(2);

					if(world.isAirBlock(pos.down(2)) || world.getBlockState(pos.down(2)).getMaterial().isLiquid()) {
						offset -= 1;
					}

					for(int yo = 0; yo < 10; yo++) {
						if(dSq <= 6 / 10.0F * (10 - yo) + offset) {
							pos.setY(altarY - 2 -yo);
							world.setBlockState(pos.toImmutable(), filler);
						}
					}
				}
			}
		}

		world.setBlockState(altar, BlockRegistry.DRUID_ALTAR.getDefaultState());
		world.setBlockState(altar.down(), BlockRegistry.MOB_SPAWNER.getDefaultState());
		TileEntity entity = world.getTileEntity(altar.down());

		if(entity instanceof TileEntityMobSpawnerBetweenlands) {
			MobSpawnerLogicBetweenlands logic = ((TileEntityMobSpawnerBetweenlands)entity).getSpawnerLogic();

			logic.setNextEntityName("thebetweenlands:dark_druid").setCheckRange(32.0D).setSpawnRange(6).setSpawnInAir(false).setMaxEntities(1 + world.rand.nextInt(3));
		}
	}

	private void placeAir(@Nonnull World world, @Nonnull MutableBlockPos pos) {
		Biome biome = world.getBiome(pos);
		int y = pos.getY();

		for(int k = 0; k < 4; k++, pos.setY(y + k)) {
			IBlockState state = world.getBlockState(pos);

			if(state == biome.fillerBlock || state == biome.topBlock || state.getMaterial() == Material.ROCK || state.getMaterial() == Material.GROUND) {
				world.setBlockToAir(pos.toImmutable());
			}
		}
	}

	private void placePillar(@Nonnull World world, @Nonnull MutableBlockPos pos, @Nonnull Random rand) {
		int height = rand.nextInt(3) + 3;
		int y = pos.getY();

		for(int k = 0; k <= height; k++, pos.setY(y + k)) {
			EnumFacing facing = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)];

			if(rand.nextBoolean()) {
				world.setBlockState(pos.toImmutable(), getRandomRuneBlock(rand).withProperty(BlockDruidStone.FACING, facing), 3);
			} else {
				world.setBlockState(pos.toImmutable(), BlockRegistry.DRUID_STONE_6.getDefaultState().withProperty(BlockDruidStone.FACING, facing));

				for(int ivyCount = 0; ivyCount < 4; ivyCount++) {
					setRandomFoliage(world, pos, rand);
				}
			}
		}
	}

	private void setRandomFoliage(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
		EnumFacing facing = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)];
		BlockPos side = pos.toImmutable().offset(facing);

		if(world.isAirBlock(side)) {
			world.setBlockState(side, BlockRegistry.POISON_IVY.getDefaultState().withProperty(BlockPoisonIvy.getPropertyFor(facing.getOpposite()), true));
		}
	}

	private IBlockState getRandomRuneBlock(@Nonnull Random rand) {
		return RUNE_STONES[rand.nextInt(RUNE_STONES.length)];
	}
}
