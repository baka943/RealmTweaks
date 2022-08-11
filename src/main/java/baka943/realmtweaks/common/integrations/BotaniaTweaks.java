package baka943.realmtweaks.common.integrations;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thebetweenlands.common.entity.mobs.EntitySwampHag;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityPixie;

import java.util.List;
import java.util.Random;

import static vazkii.botania.common.entity.EntityDoppleganger.isTruePlayer;

public class BotaniaTweaks {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(BotaniaTweaks.class);

		BotaniaAPI.registerPureDaisyRecipe(Blocks.MAGMA, Blocks.LAVA.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(BlockRegistry.SWAMP_WATER, BlockRegistry.BLACK_ICE.getDefaultState());
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;
		Random rand = world.rand;

		String LOONIUM = "botania:looniumItemStackToDrop";

		if(!world.isRemote) {
			if(entity.getEntityData().hasKey(LOONIUM) && !(entity instanceof EntitySwampHag)) {
				event.setCanceled(true);

				EntityMob swampHag = new EntitySwampHag(event.getWorld());
				swampHag.setPositionAndRotation(x, y, z, entity.rotationYaw, 0);
				swampHag.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
				swampHag.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, Integer.MAX_VALUE, 0));

				NBTTagCompound data = new ItemStack(ItemRegistry.ITEMS_MISC, world.rand.nextInt(2) + 1, 48).writeToNBT(new NBTTagCompound());
				swampHag.getEntityData().setTag(LOONIUM, data);

				swampHag.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
				world.spawnEntity(swampHag);
				swampHag.spawnExplosionParticle();
			}

			List<EntityDoppleganger> gaia = world.getEntitiesWithinAABB(EntityDoppleganger.class, new AxisAlignedBB(x - 15F, y - 15F, z - 15F, x + 15F, y + 15F, z + 15F));

			List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - 25F, y - 25F, z - 25F, x + 25F, y + 25F, z + 25F), player -> isTruePlayer(player) && !player.isSpectator());

			if(!players.isEmpty() && !gaia.isEmpty() && entity instanceof EntityMob) {
				event.setCanceled(true);

				EntityPixie pixie = new EntityPixie(world);
				pixie.setProps(players.get(rand.nextInt(players.size())), gaia.get(rand.nextInt(gaia.size())), 1, 8);
				pixie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pixie)), null);

				for(int count = 0; count < 3; count++) {
					pixie.setPosition(x + rand.nextDouble(), y + rand.nextDouble(), z + rand.nextDouble());
					world.spawnEntity(pixie);
				}
			}
		}
	}

}
