package baka943.realmtweaks.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOres extends BlockMod {

	public BlockOres(String name, int level) {
		super(Material.ROCK, name);
		this.setSoundType(SoundType.STONE);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setHarvestLevel("pickaxe", level);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

}
