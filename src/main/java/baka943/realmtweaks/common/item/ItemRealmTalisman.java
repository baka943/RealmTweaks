package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.block.BlockRealmSapling;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.Constants;
import thebetweenlands.api.storage.LocalRegion;
import thebetweenlands.api.storage.StorageUUID;
import thebetweenlands.common.block.structure.BlockTreePortal;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.item.IGenericItem;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.common.world.gen.feature.structure.WorldGenWeedwoodPortalTree;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;
import thebetweenlands.common.world.storage.location.LocationPortal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ItemRealmTalisman extends ItemMod {

	public ItemRealmTalisman() {
		super("realm_talisman");
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);

		if(!playerIn.canPlayerEdit(pos, facing, stack)) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState state = worldIn.getBlockState(pos);

			if(this.isPortalWood(state)) {
				BlockPos offsetPos = pos.offset(facing);

				for(int yo = 3; yo > 0; yo--) {
					BlockPos portalPos = offsetPos.down(yo);
					EnumFacing.Axis frameAxis = this.getPortalWoodFrameAxis(worldIn, portalPos);

					if(frameAxis != null) {
						if(!worldIn.isRemote) {
							EnumFacing closestDir = null;

							for(EnumFacing dir : EnumFacing.VALUES) {
								if(dir.getAxis() == frameAxis) {
									if(closestDir == null || pos.offset(dir).distanceSq(playerIn.posX, playerIn.posY, playerIn.posZ) <= pos.offset(closestDir).distanceSq(playerIn.posX, playerIn.posY, playerIn.posZ)) {
										closestDir = dir;
									}
								}
							}

							if(frameAxis == EnumFacing.Axis.X) {
								BlockTreePortal.makePortalX(worldIn, portalPos.up());
							} else if(frameAxis == EnumFacing.Axis.Z) {
								BlockTreePortal.makePortalZ(worldIn, portalPos.up());
							}

							if(frameAxis == EnumFacing.Axis.X && BlockTreePortal.isPatternValidX(worldIn, portalPos.up())
									|| frameAxis == EnumFacing.Axis.Z && BlockTreePortal.isPatternValidZ(worldIn, portalPos.up())) {
								//Only create new location is none exists
								if(this.getPortalAt(worldIn, portalPos.up()) == null) {
									BetweenlandsWorldStorage worldStorage = BetweenlandsWorldStorage.forWorld(worldIn);
									LocationPortal location = new LocationPortal(worldStorage, new StorageUUID(UUID.randomUUID()), LocalRegion.getFromBlockPos(pos), portalPos.offset(closestDir).down());
									location.addBounds(new AxisAlignedBB(portalPos.up()).grow(1, 2, 1).expand(0, -0.5D, 0));
									location.setSeed(worldIn.rand.nextLong());
									location.setDirty(true);
									location.setVisible(false);
									location.linkChunks();
									worldStorage.getLocalStorageHandler().addLocalStorage(location);
								}

								worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundRegistry.PORTAL_ACTIVATE, SoundCategory.PLAYERS, 0.5F, itemRand.nextFloat() * 0.4F + 0.8F);
							}
						}

						return EnumActionResult.SUCCESS;
					}
				}
			}

			if(this.isBlockSapling(state) && (EnumTalisman.REALM_TALISMAN_0.isItemOf(stack) || EnumTalisman.REALM_TALISMAN_1.isItemOf(stack))) {
				if(!worldIn.isRemote) {
					if(!BetweenlandsConfig.WORLD_AND_DIMENSION.portalDimensionWhitelistSet.isListed(playerIn.world.provider.getDimension())) {
						playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.wrongdimension"), true);
					} else {
						WorldGenWeedwoodPortalTree gen;
						Block block = state.getBlock();

						int targetDim = ((BlockRealmSapling) block).getDim();

						if(targetDim == playerIn.world.provider.getDimension()) {
							gen = new WorldGenWeedwoodPortalTree();
						} else {
							gen = new WorldGenWeedwoodPortalTree(targetDim);
						}

						if(gen.generate(worldIn, itemRand, pos)) {
							worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundRegistry.PORTAL_ACTIVATE, SoundCategory.PLAYERS, 0.5F, itemRand.nextFloat() * 0.4F + 0.8F);
							playerIn.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 2D, pos.getZ() + 0.5D, playerIn.rotationYaw, playerIn.rotationPitch);

							if(playerIn instanceof EntityPlayerMP) {
								((EntityPlayerMP)playerIn).connection.setPlayerLocation(pos.getX() + 0.5D, pos.getY() + 2D, pos.getZ() + 0.5D, playerIn.rotationYaw, playerIn.rotationPitch);
							}
						} else {
							playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.noplace"), true);
						}
					}
				}

				return EnumActionResult.SUCCESS;
			} else if(EnumTalisman.REALM_TALISMAN_0.isItemOf(stack) && !(playerIn instanceof FakePlayer)) {
				LocationPortal portal = this.getPortalAt(worldIn, pos);

				if(portal != null) {
					if(!worldIn.isRemote) {
						stack = stack.copy();
						stack.setItemDamage(EnumTalisman.REALM_TALISMAN_1.getID());
						playerIn.setHeldItem(hand, stack);

						stack.setTagInfo("link", new NBTTagLong(portal.getPortalPosition().toLong()));
						stack.setTagInfo("linkDim", new NBTTagInt(worldIn.provider.getDimension()));

						playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.linked"), true);

						worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 0.8F, 0.7F);
					}

					return EnumActionResult.SUCCESS;
				}
			} else if(EnumTalisman.REALM_TALISMAN_1.isItemOf(stack) && !(playerIn instanceof FakePlayer)) {
				if(!worldIn.isRemote) {
					stack = stack.copy();
					stack.setItemDamage(EnumTalisman.REALM_TALISMAN_0.getID());
					playerIn.setHeldItem(hand, stack);

					worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 0.8F, 0.7F);
				}

				if(stack.hasTagCompound() && stack.getTagCompound().hasKey("link", Constants.NBT.TAG_LONG) && stack.getTagCompound().hasKey("linkDim", Constants.NBT.TAG_INT)) {
					BlockPos otherPortalPos = BlockPos.fromLong(stack.getTagCompound().getLong("link"));
					LocationPortal portal = this.getPortalAt(worldIn, pos);

					if(portal != null) {
						if(worldIn instanceof WorldServer) {
							int linkDim = stack.getTagCompound().getInteger("linkDim");

							if(linkDim != worldIn.provider.getDimension() &&
									(linkDim == BetweenlandsConfig.WORLD_AND_DIMENSION.dimensionId || worldIn.provider.getDimension() == BetweenlandsConfig.WORLD_AND_DIMENSION.dimensionId)) {
								WorldServer otherWorld = ((WorldServer) worldIn).getMinecraftServer().getWorld(linkDim);
								if(otherWorld != null) {
									double moveFactor = otherWorld.provider.getMovementFactor() / worldIn.provider.getMovementFactor();

									if(new Vec3d(portal.getPortalPosition()).distanceTo(new Vec3d(otherPortalPos.getX() * moveFactor, portal.getPortalPosition().getY(), otherPortalPos.getZ() * moveFactor)) <= BetweenlandsConfig.WORLD_AND_DIMENSION.portalMaxLinkDist) {
										LocationPortal linkPortal = this.getLinkPortal(otherWorld, otherPortalPos);
										if(linkPortal != null) {
											linkPortal.setOtherPortalPosition(worldIn.provider.getDimension(), portal.getPortalPosition());
											portal.setOtherPortalPosition(linkDim, linkPortal.getPortalPosition());
											playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.portal_linked"), true);
										} else {
											playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.cant_link"), true);
										}
									} else {
										playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.too_far", new TextComponentString(String.valueOf(BetweenlandsConfig.WORLD_AND_DIMENSION.portalMaxLinkDist))), true);
									}
								}
							} else {
								playerIn.sendStatusMessage(new TextComponentTranslation("chat.talisman.cant_link"), true);
							}
						}
					}
				}

				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.FAIL;
	}

	private LocationPortal getPortalAt(World world, BlockPos pos) {
		BetweenlandsWorldStorage worldStorage = BetweenlandsWorldStorage.forWorld(world);
		List<LocationPortal> portals = worldStorage.getLocalStorageHandler().getLocalStorages(LocationPortal.class, pos.getX() + 0.5D, pos.getZ() + 0.5D, location -> location.isInside(new Vec3d(pos).add(0.5D, 0.5D, 0.5D)));

		if(!portals.isEmpty()) {
			return portals.get(0);
		}

		return null;
	}

	private LocationPortal getLinkPortal(WorldServer world, BlockPos pos) {
		BetweenlandsWorldStorage worldStorage = BetweenlandsWorldStorage.forWorld(world);
		List<LocationPortal> portals = worldStorage.getLocalStorageHandler().getLocalStorages(LocationPortal.class, pos.getX() + 0.5D, pos.getZ() + 0.5D, location -> location.isInside(new Vec3d(pos).add(0.5D, 0.5D, 0.5D)) && pos.equals(location.getPortalPosition()));

		if(!portals.isEmpty()) {
			return portals.get(0);
		}

		return null;
	}

	private boolean isBlockSapling(IBlockState state) {
		Block block = state.getBlock();

		return block instanceof BlockRealmSapling;
	}

	@Nullable
	private EnumFacing.Axis getPortalWoodFrameAxis(World world, BlockPos pos) {
		EnumFacing north = EnumFacing.NORTH;
		EnumFacing south = EnumFacing.SOUTH;

		if(this.isPortalWood(world.getBlockState(pos)) && this.isPortalWood(world.getBlockState(pos.offset(north))) && this.isPortalWood(world.getBlockState(pos.offset(south)))
				&& this.isPortalWood(world.getBlockState(pos.up().offset(north))) && this.isPortalWood(world.getBlockState(pos.up().offset(south)))
				&& this.isPortalWood(world.getBlockState(pos.up(2).offset(north))) && this.isPortalWood(world.getBlockState(pos.up(2).offset(south)))
				&& this.isPortalWood(world.getBlockState(pos.up(3))) && this.isPortalWood(world.getBlockState(pos.up(3).offset(north))) && this.isPortalWood(world.getBlockState(pos.up(3).offset(south)))) {
			return EnumFacing.Axis.X;
		}

		EnumFacing east = EnumFacing.EAST;
		EnumFacing west = EnumFacing.WEST;

		if(this.isPortalWood(world.getBlockState(pos)) && this.isPortalWood(world.getBlockState(pos.offset(east))) && this.isPortalWood(world.getBlockState(pos.offset(west)))
				&& this.isPortalWood(world.getBlockState(pos.up().offset(east))) && this.isPortalWood(world.getBlockState(pos.up().offset(west)))
				&& this.isPortalWood(world.getBlockState(pos.up(2).offset(east))) && this.isPortalWood(world.getBlockState(pos.up(2).offset(west)))
				&& this.isPortalWood(world.getBlockState(pos.up(3))) && this.isPortalWood(world.getBlockState(pos.up(3).offset(east))) && this.isPortalWood(world.getBlockState(pos.up(3).offset(west)))) {
			return EnumFacing.Axis.Z;
		}

		return null;
	}

	private boolean isPortalWood(IBlockState state) {
		Block block = state.getBlock();

		return block == BlockRegistry.PORTAL_FRAME || block == BlockRegistry.LOG_PORTAL;
	}

	private enum EnumTalisman implements IGenericItem {
		REALM_TALISMAN_0,
		REALM_TALISMAN_1;

		private final String unlocalizedName;
		private final String modelName;

		EnumTalisman() {
			this.modelName = this.name().toLowerCase(Locale.ENGLISH);
			this.unlocalizedName = this.modelName;
		}

		@Override
		public String getTranslationKey() {
			return this.unlocalizedName;
		}

		@Override
		public String getModelName() {
			return this.modelName;
		}

		@Override
		public int getID() {
			return this.ordinal();
		}

		@Override
		public Item getItem() {
			return ModItems.REALM_TALISMAN;
		}
	}

}
