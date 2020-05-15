package baka943.realmtweaks.common.block;

import baka943.realmtweaks.common.core.handler.EventHandler;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockEndPortal extends net.minecraft.block.BlockEndPortal {

	protected BlockEndPortal() {
		super(Material.PORTAL);
		this.setRegistryName(new ResourceLocation("end_portal"));
		this.setTranslationKey("end_portal");
	}

	@Override
	public void onEntityCollision(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entityIn) {
		if(!worldIn.isRemote && entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entityIn;

			player.sendStatusMessage(new TextComponentTranslation("chat.realmtweaks.end_sky"), true);

			NBTTagCompound playerData = player.getEntityData();
			NBTTagCompound data = Utils.getTagSafe(playerData);

			if(!data.hasKey(EventHandler.TAG_HAS_SACRIFICE_DAGGER)) {
				PotionEffect effect = new PotionEffect(MobEffects.INSTANT_HEALTH, 40, 0, false, true);

				player.addPotionEffect(effect);
			}
		}
	}

}
