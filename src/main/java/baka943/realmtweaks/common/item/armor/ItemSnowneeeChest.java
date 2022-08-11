package baka943.realmtweaks.common.item.armor;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSnowneeeChest extends ItemSnowneeeArmor {

	public ItemSnowneeeChest() {
		super(EntityEquipmentSlot.CHEST, "snowneee_snowball_i");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flags) {
		list.add(I18n.format("tooltip.realmtweaks.snowneee_chest.desc"));
		super.addInformation(stack, world, list, flags);
	}

}
