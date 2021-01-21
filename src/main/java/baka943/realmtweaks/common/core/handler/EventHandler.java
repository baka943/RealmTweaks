package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.item.ModItems;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.ItemHandlerHelper;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class EventHandler {

	@SubscribeEvent
	public static void onStartingItems(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;

		NBTTagCompound playerData = player.getEntityData();
		NBTTagCompound data = Utils.getTagSafe(playerData);

		if(!data.getBoolean(LibMisc.TAG_HAS_STARTING_ITEM)) {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.ADVANCEMENT_BOOK));

			data.setBoolean(LibMisc.TAG_HAS_STARTING_ITEM, true);
			playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
		}
	}

	@SubscribeEvent
	public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		EnumHand hand = event.getHand();
		ItemStack stack = player.getHeldItem(hand);

		Block block = Block.getBlockFromItem(stack.getItem());

		if(!stack.isEmpty() && block instanceof BlockLadder) {
			World world = event.getWorld();
			BlockPos pos = event.getPos();

			while(world.getBlockState(pos).getBlock().equals(block)) {
				event.setCanceled(true);

				BlockPos posDown = pos.down();
				IBlockState stateDown = world.getBlockState(posDown);

				if(world.isOutsideBuildHeight(posDown)) {
					break;
				}

				if(stateDown.getBlock() == block) {
					pos = posDown;
				} else {
					if(stateDown.getBlock().isAir(stateDown, world, posDown)) {
						IBlockState state = world.getBlockState(pos);
						EnumFacing facing = state.getValue(BlockLadder.FACING);
						BlockPos attachPos = posDown.offset(facing, -1);

						if(((BlockLadder)block).canAttachTo(world, attachPos, facing)) {
							world.setBlockState(posDown, state);
							world.playSound(null, posDown.getX(), posDown.getY(), posDown.getZ(), SoundEvents.BLOCK_LADDER_PLACE, SoundCategory.BLOCKS, 1F, 1F);

							if(world.isRemote) {
								player.swingArm(hand);
							}

							if(!player.capabilities.isCreativeMode) {
								stack.shrink(1);

								if(stack.getCount() <= 0) {
									player.setHeldItem(hand, ItemStack.EMPTY);
								}
							}
						}
					}

					break;
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void chargeDimension(EntityTravelToDimensionEvent event) {
		if(event.isCanceled()) {
			return;
		}

		if(event.getEntity() instanceof EntityPlayer && !(event.getEntity() instanceof FakePlayer)) {
			EntityPlayer player = (EntityPlayer)event.getEntity();
			int toDimension = event.getDimension();
			int fromDimension = player.dimension;

			if(player != null) {
				if(toDimension == Utils.getDimId("overworld") && fromDimension != Utils.getDimId("the_nether")) {
					event.setCanceled(true);
				}
			}

		} else {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void playerHurt(LivingHurtEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getEntity();

			if(!player.world.isRemote && event.getSource().getTrueSource() instanceof EntityMob) {
				float currentHealth = player.getHealth();
				float maxHealth = player.getMaxHealth();
				float damage = event.getAmount();

				if(currentHealth < maxHealth) {
					event.setAmount(Math.max(1.0F, damage * (currentHealth / maxHealth)));
				}
			}
		}
	}

}
