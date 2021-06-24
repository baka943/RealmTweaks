package baka943.realmtweaks.common.entity;

import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.common.entity.mobs.EntityAngler;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.List;

public class EntityBoneHook extends EntityFishHook {

	private static final ResourceLocation BETWEENLANDS_FISHING = Utils.getRL("betweenlands/fishings/fishing");

	private static final DataParameter<Integer> PLAYER_ID = EntityDataManager.createKey(EntityBoneHook.class, DataSerializers.VARINT);
	private static final DataParameter<BlockPos> POS = EntityDataManager.createKey(EntityBoneHook.class, DataSerializers.BLOCK_POS);

	public EntityBoneHook(World world) {
		this(world, world.getPlayerEntityByUUID(Minecraft.getMinecraft().getSession().getProfile().getId()));
	}

	@SideOnly(Side.CLIENT)
	public EntityBoneHook(World world, EntityPlayer player, double x, double y, double z) {
		super(world, player, x, y, z);
	}

	public EntityBoneHook(World world, EntityPlayer player) {
		super(world, player);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(PLAYER_ID, this.angler == null ? 0 : this.angler.getEntityId());
		this.dataManager.register(POS, this.getPosition());
	}

	@Override
	public int handleHookRetraction() {
		if(!this.world.isRemote && this.angler != null) {
			int i = 0;
			ItemFishedEvent event = null;

			if(this.caughtEntity != null) {
				this.bringInHookedEntity();
				this.world.setEntityState(this, (byte) 13);

				i = this.caughtEntity instanceof EntityItem ? 3 : 5;
			} else if(this.ticksCatchable > 0) {
				LootContext.Builder builder = new LootContext.Builder((WorldServer) this.world);

				builder.withLuck((float) this.luck + this.angler.getLuck()).withPlayer(this.angler).withLootedEntity(this);

				List<ItemStack> result = this.world.getLootTableManager().getLootTableFromLocation(BETWEENLANDS_FISHING).generateLootForPools(this.rand, builder.build());

				event = new ItemFishedEvent(result, this.inGround ? 2 : 1, this);

				MinecraftForge.EVENT_BUS.post(event);

				if(event.isCanceled()) {
					this.setDead();

					return event.getRodDamage();
				}

				for(ItemStack stack : result) {
					EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, stack);
					double posX = this.angler.posX - this.posX;
					double posY = this.angler.posY - this.posY;
					double posZ = this.angler.posZ - this.posZ;
					double pos = MathHelper.sqrt(posX * posX + posY * posY + posZ * posZ);

					entityItem.motionX = posX * 0.1D;
					entityItem.motionY = posY * 0.1D + (double) MathHelper.sqrt(pos) * 0.08D;
					entityItem.motionZ = posZ * 0.1D;

					this.world.spawnEntity(entityItem);
					this.angler.world.spawnEntity(new EntityXPOrb(this.angler.world, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));

					if(stack.isItemEqual(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 21))) {
						entityItem.setDead();

						EntityAngler angler = new EntityAngler(this.world);
						angler.copyLocationAndAnglesFrom(entityItem);

						angler.motionX = entityItem.motionX;
						angler.motionY = entityItem.motionY;
						angler.motionZ = entityItem.motionZ;

						this.world.spawnEntity(angler);
					}
				}

				i = 1;
			}

			if(this.inGround) i = 2;

			this.setDead();

			return event == null ? i : event.getRodDamage();
		} else return 0;
	}

}
