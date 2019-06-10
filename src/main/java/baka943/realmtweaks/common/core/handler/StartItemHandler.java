package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.item.ModItems;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import thebetweenlands.common.registries.ItemRegistry;

public class StartItemHandler {

	private static final String TAG_HAS_STARTING_ITEM = LibMisc.MOD_ID + ".starting_item";

	@SubscribeEvent
	public void spawnWithBook(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;

		NBTTagCompound playerData = player.getEntityData();
		NBTTagCompound data = getTagSafe(playerData);

		if(!data.getBoolean(TAG_HAS_STARTING_ITEM)) {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.ADVANCEMENT_BOOK));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ItemRegistry.EMPTY_AMATE_MAP));

			data.setBoolean(TAG_HAS_STARTING_ITEM, true);
			playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
		}
	}

	private static NBTTagCompound getTagSafe(NBTTagCompound tag) {
		if(tag == null) {
			return new NBTTagCompound();
		}

		return tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
	}

}
