package baka943.realmtweaks.common.item.armor;

import baka943.realmtweaks.client.model.armor.ModelSnowneee;
import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.item.ModItems;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ItemSnowneeeArmor extends ItemArmor implements IModelRegister {

	public static final ArmorMaterial snowneeeArmorMaterial = EnumHelper.addArmorMaterial("SNOWNEEE", "snowneee", 9, new int[]{1, 1, 2, 1}, 2, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	protected Map<EntityEquipmentSlot, ModelBiped> models = null;
	public final EntityEquipmentSlot slot;

	public ItemSnowneeeArmor(EntityEquipmentSlot slot, String name) {
		this(snowneeeArmorMaterial, slot, name);
	}

	public ItemSnowneeeArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name) {
		super(material, 0, slot);
		this.setRegistryName(Utils.getRL(name));
		this.setTranslationKey(LibMisc.MOD_ID + "." + name);
		this.slot = slot;
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.setMaxDamage(0);
	}

	@Nullable
	@Override
	public String getArmorTexture(@Nonnull ItemStack stack, @Nonnull Entity entity, @Nonnull EntityEquipmentSlot slot, @Nonnull String type) {
		return "realmtweaks:textures/model/armor_snowneee.png";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(@Nonnull EntityLivingBase entityLiving, @Nonnull ItemStack itemStack, @Nonnull EntityEquipmentSlot armorSlot, @Nonnull ModelBiped original) {
		ModelBiped model = getArmorModelForSlot(entityLiving, itemStack, armorSlot);

		if(model == null) model = provideArmorModelForSlot(itemStack, armorSlot);

		if(model != null) {
			model.setModelAttributes(original);

			return model;
		}

		return super.getArmorModel(entityLiving, itemStack, armorSlot, original);
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModelForSlot(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot) {
		if(models == null) models = new EnumMap<>(EntityEquipmentSlot.class);

		return models.get(slot);
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped provideArmorModelForSlot(ItemStack stack, EntityEquipmentSlot slot) {
		models.put(slot, new ModelSnowneee(slot));

		return models.get(slot);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flags) {
		if(GuiScreen.isShiftKeyDown()) {
			addInformationAfterShift(stack, world, list, flags);
		} else list.add(I18n.format("misc.realmtweaks.shiftinfo"));
	}

	@SideOnly(Side.CLIENT)
	public void addInformationAfterShift(ItemStack stack, World world, List<String> list, ITooltipFlag flags) {
		EntityPlayer player = Minecraft.getMinecraft().player;

		list.add(I18n.format("misc.realmtweaks.armorset") + " " + I18n.format("realmtweaks.armorset.snowneee.name") + " (" + getSetPiecesEquipped(player) + "/" + getArmorSetStacks().length + ")");
		list.add(I18n.format("realmtweaks.armorset.snowneee.desc"));

		ItemStack[] stacks = getArmorSetStacks();

		for(int i = 0; i < stacks.length; i++) {
			list.add((hasArmorSetItem(player, i) ? TextFormatting.GREEN : "") + " - " + stacks[i].getDisplayName());
		}
	}

	static ItemStack[] armorset;

	public ItemStack[] getArmorSetStacks() {
		if(armorset == null) {
			armorset = new ItemStack[]{new ItemStack(ModItems.SNOWNEEE_CHEST), new ItemStack(ModItems.SNOWNEEE_LEGS), new ItemStack(ModItems.SNOWNEEE_BOOTS)};
		}

		return armorset;
	}

	public boolean hasArmorSet(EntityPlayer player) {
		return hasArmorSetItem(player, 0) && hasArmorSetItem(player, 1) && hasArmorSetItem(player, 2);
	}

	public boolean hasArmorSetItem(EntityPlayer player, int i) {
		if(player == null || player.inventory == null || player.inventory.armorInventory == null) return false;

		ItemStack stack = player.inventory.armorInventory.get(2 - i);
		Item item = stack.getItem();

		if(stack.isEmpty()) return false;

		switch(i) {
			case 0: return item == ModItems.SNOWNEEE_CHEST;
			case 1: return item == ModItems.SNOWNEEE_LEGS;
			case 2: return item == ModItems.SNOWNEEE_BOOTS;
			default: return false;
		}
	}

	private int getSetPiecesEquipped(EntityPlayer player) {
		int pieces = 0;

		for(int i = 0; i < 3; i++) {
			if(hasArmorSetItem(player, i)) pieces++;
		}

		return pieces;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
