/**
 * Sorry, I used some codes from the Botania Mod. Thanks <Vazkii> created the Botania Mod.
 * Get the Botania Mod Source Code in github: https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * Thanks again!
 */

package baka943.realmtweaks.common.block.tile;

import epicsquid.roots.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.common.registries.FluidRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.handler.ModSounds;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class TileBetweenAltar extends TileSimpleInventory implements IPetalApothecary, ITickable {

	private static final Pattern SEED_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)wildroot)|(?:(?:[a-z-_.:]|^)Wildroot))(?:[sA-Z-_.:]|$)");
	private static final int CRAFT_EFFECT_EVENT = 1;

	public static final String TAG_HAS_WATER = "hasWater";

	private static final String ITEM_TAG_APOTHECARY_SPAWNED = "ApothecarySpawned";

	public boolean hasWater = false;

	public boolean collideEntityItem(EntityItem item) {
		ItemStack stack = item.getItem();

		if(world.isRemote || stack.isEmpty() || item.isDead) return false;

		boolean hasFluidCapability = stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

		if(!hasWater()) {
			if(hasFluidCapability) {
				IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

				FluidStack drainWater = fluidHandler.drain(new FluidStack(FluidRegistry.SWAMP_WATER, Fluid.BUCKET_VOLUME), false);

				if(drainWater != null && drainWater.getFluid() == FluidRegistry.SWAMP_WATER && drainWater.amount == Fluid.BUCKET_VOLUME) {
					setWater(true);
					world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
					fluidHandler.drain(new FluidStack(FluidRegistry.SWAMP_WATER, Fluid.BUCKET_VOLUME), true);
					item.setItem(fluidHandler.getContainer());

					return true;
				}
			}

			return false;
		}

		if(SEED_PATTERN.matcher(stack.getTranslationKey()).find()) {
			for(RecipePetals recipe : BotaniaAPI.petalRecipes) {
				if(recipe.matches(itemHandler)) {
					for(int i = 0; i < getSizeInventory(); i++)
						itemHandler.setStackInSlot(i, ItemStack.EMPTY);

					stack.shrink(1);

					ItemStack output = recipe.getOutput().copy();
					EntityItem outputItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
					outputItem.addTag(ITEM_TAG_APOTHECARY_SPAWNED);
					world.spawnEntity(outputItem);
					world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.altarCraft, SoundCategory.BLOCKS, 1F, 1F, false);
					setWater(false);
					world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());

					world.addBlockEvent(getPos(), getBlockType(), CRAFT_EFFECT_EVENT, 0);

					return true;
				}
			}
		} else if(!hasFluidCapability && !item.getTags().contains(ITEM_TAG_APOTHECARY_SPAWNED)) {
			if(!itemHandler.getStackInSlot(getSizeInventory() - 1).isEmpty())
				return false;

			for(int i = 0; i < getSizeInventory(); i++) {
				if(itemHandler.getStackInSlot(i).isEmpty()) {
					itemHandler.setStackInSlot(i, stack.splitStack(1));
					world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 0.1F, 10F);
					return true;
				}
			}
		}

		return false;
	}

	private IFlowerComponent getFlowerComponent(ItemStack stack) {
		IFlowerComponent c = null;

		if(stack.getItem() instanceof IFlowerComponent)
			c = (IFlowerComponent) stack.getItem();
		else if(stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof IFlowerComponent)
			c = (IFlowerComponent) ((ItemBlock) stack.getItem()).getBlock();

		return c;
	}

	public boolean isEmpty() {
		for(int i = 0; i < getSizeInventory(); i++)
			if(!itemHandler.getStackInSlot(i).isEmpty()) return false;

		return true;
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(0, 1D / 16D * 20D, 0), pos.add(1, 1D / 16D * 32D, 1)));

			boolean didChange = false;
			for(EntityItem item : items)
				didChange = collideEntityItem(item) || didChange;

			if(didChange) VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
		} else {
			for(int i = 0; i < getSizeInventory(); i++) {
				ItemStack stackAt = itemHandler.getStackInSlot(i);

				if(stackAt.isEmpty()) break;

				if(Math.random() >= 0.97) {
					IFlowerComponent comp = getFlowerComponent(stackAt);
					Color color = new Color(comp == null ? 0x888888 : comp.getParticleColor(stackAt));
					float red = color.getRed() / 255F;
					float green = color.getGreen() / 255F;
					float blue = color.getBlue() / 255F;

					if(Math.random() >= 0.75F)
						world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 0.1F, 10F);
					Botania.proxy.sparkleFX(pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1.2, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float)Math.random(), 10);
				}
			}
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.merge(this.itemHandler.serializeNBT());
		cmp.setBoolean(TAG_HAS_WATER, hasWater());
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		this.itemHandler = this.createItemHandler();
		this.itemHandler.deserializeNBT(cmp);
		hasWater = cmp.getBoolean(TAG_HAS_WATER);
	}

	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		this.readPacketNBT(packet.getNbtCompound());
	}

	@Override
	public boolean receiveClientEvent(int id, int param) {
		if(id == CRAFT_EFFECT_EVENT) {
			if(world.isRemote) {
				for(int i = 0; i < 25; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();
					Botania.proxy.sparkleFX(pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
				}

				world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.altarCraft, SoundCategory.BLOCKS, 1F, 1F, false);
			}

			return true;
		}

		return super.receiveClientEvent(id, param);
	}

	@Override
	public int getSizeInventory() {
		return 16;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, false) {
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 1;
			}
		};
	}

	@Override
	public void setWater(boolean water) {
		hasWater = water;
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
	}

	@Override
	public boolean hasWater() {
		return hasWater;
	}

	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		int xc = res.getScaledWidth() / 2;
		int yc = res.getScaledHeight() / 2;

		float angle = -90;
		int radius = 24;
		int amt = 0;

		for(int i = 0; i < getSizeInventory(); i++) {
			if(itemHandler.getStackInSlot(i).isEmpty())
				break;
			amt++;
		}

		if(amt > 0) {
			float anglePer = 360F / amt;

			for(RecipePetals recipe : BotaniaAPI.petalRecipes)
				if(recipe.matches(itemHandler)) {
					GlStateManager.color(1F, 1F, 1F, 1F);
					mc.renderEngine.bindTexture(HUDHandler.manaBar);
					RenderHelper.drawTexturedModalRect(xc + radius + 9, yc - 8, 0, 0, 8, 22, 15);

					ItemStack stack = recipe.getOutput();

					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					mc.getRenderItem().renderItemIntoGUI(stack, xc + radius + 32, yc - 8);
					mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.wildroot), xc + radius + 16, yc + 6);
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
					mc.fontRenderer.drawStringWithShadow("+", xc + radius + 14, yc + 10, 0xFFFFFF);
				}

			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

			for(int i = 0; i < amt; i++) {
				double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
				GlStateManager.translate(xPos, yPos, 0);
				mc.getRenderItem().renderItemIntoGUI(itemHandler.getStackInSlot(i), 0, 0);
				GlStateManager.translate(-xPos, -yPos, 0);

				angle += anglePer;
			}

			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		}
	}

}
