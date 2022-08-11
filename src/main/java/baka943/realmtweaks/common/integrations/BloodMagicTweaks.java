package baka943.realmtweaks.common.integrations;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.soul.IDemonWill;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import com.brandon3055.draconicevolution.DEFeatures;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import sonar.fluxnetworks.common.registry.RegistryItems;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.world.teleporter.TeleporterHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static thebetweenlands.common.handler.PlayerJoinWorldHandler.NOT_FIRST_SPAWN_NBT;

public class BloodMagicTweaks {

	private static final Map<Double, Double> podium_location = new HashMap<>();

	public static void init() {
		MinecraftForge.EVENT_BUS.register(BloodMagicTweaks.class);
	}

	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent event) {
		World world = event.world;
		AtomicBoolean hasDragon = new AtomicBoolean(false);

		if(!world.isRemote && event.phase == TickEvent.Phase.END && world.provider instanceof WorldProviderEnd && world.getTotalWorldTime() % 192000 == 0) {
			world.loadedEntityList.forEach(entity -> {
				if(entity instanceof EntityDragon) hasDragon.set(true);
			});

			if(!hasDragon.get()) {
				double podY = world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION).getY() - 3;

				podium_location.forEach((x, z) -> {
					EntityEnderCrystal crystal = new EntityEnderCrystal(world, x, podY, z);
					crystal.setShowBottom(false);
					crystal.setEntityInvulnerable(true);
					world.spawnEntity(crystal);
				});

				DragonFightManager dragonfightmanager = ((WorldProviderEnd) world.provider).getDragonFightManager();
				dragonfightmanager.respawnDragon();

				//todo 公告末影龙复活
			}
		}
	}

	@SubscribeEvent
	public static void willDrop(LivingDropsEvent event) {
		EntityLivingBase attackedEntity = event.getEntityLiving();
		Entity entity = event.getSource().getTrueSource();
		Random rand = new Random();

		if(entity != null && !entity.getEntityWorld().isRemote && entity instanceof EntityPlayer) {
			double souls = rand.nextDouble() * 20;
			ItemStack stack = ((IDemonWill) RegistrarBloodMagicItems.MONSTER_SOUL).createWill(0, souls);
            EntityPlayer player = (EntityPlayer) entity;
            SoulNetwork network = NetworkHelper.getSoulNetwork(player);

			if(attackedEntity instanceof EntityEnderman && network.getOrbTier() == 0
                    && rand.nextInt(5) == 0) {
				event.getDrops().add(new EntityItem(attackedEntity.getEntityWorld(), attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, stack));
			}
		}
	}

	@SubscribeEvent(receiveCanceled = true)
	public static void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState crusher = world.getBlockState(pos);
		IBlockState base = world.getBlockState(pos.down(2));

		if(!world.isRemote) {
			if(crusher.getBlock() == Blocks.OBSIDIAN && base.getBlock() == Blocks.BEDROCK) {
				List<EntityItem> entites = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.down()));

				if(entites.isEmpty()) return;

				List<EntityItem> validEntites = new ArrayList<>();
				AtomicInteger count = new AtomicInteger();

				entites.forEach(entity -> {
					if(entity.getItem().getItem() == DEFeatures.draconiumDust) {
						validEntites.add(entity);
						count.addAndGet(entity.getItem().getCount());
					}
				});

				if(validEntites.isEmpty()) return;

				ItemStack stack = new ItemStack(RegistryItems.FLUX, count.getAndIncrement());

				validEntites.forEach(Entity::setDead);
				world.setBlockToAir(pos);
				world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack));
				world.setBlockState(pos.down(), Blocks.OBSIDIAN.getDefaultState());
				world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if(!event.player.world.isRemote && BetweenlandsConfig.WORLD_AND_DIMENSION.startInBetweenlands && event.player.world.provider.getDimension() != BetweenlandsConfig.WORLD_AND_DIMENSION.dimensionId && event.player.world instanceof WorldServer) {
			NBTTagCompound dataNbt = event.player.getEntityData();
			NBTTagCompound persistentNbt = dataNbt.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

			boolean isFirstTimeSpawning = !(persistentNbt.hasKey(NOT_FIRST_SPAWN_NBT, Constants.NBT.TAG_BYTE) && persistentNbt.getBoolean(NOT_FIRST_SPAWN_NBT));

			if(isFirstTimeSpawning) {
				//Set before teleporting because recursion
				persistentNbt.setBoolean(NOT_FIRST_SPAWN_NBT, true);
				dataNbt.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistentNbt);

				WorldServer blWorld = event.player.world.getMinecraftServer().getWorld(BetweenlandsConfig.WORLD_AND_DIMENSION.dimensionId);

				TeleporterHandler.transferToDim(event.player, blWorld, BetweenlandsConfig.WORLD_AND_DIMENSION.startInPortal, true);
			}
		}
	}

	static {
		podium_location.put(-0.5D, -2.5D);
		podium_location.put(3.5D, -0.5D);
		podium_location.put(-2.5D, 1.5D);
		podium_location.put(1.5D, 3.5D);
	}

}
