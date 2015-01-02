package elec332.core.util.items;

import elec332.core.helper.registerHelper;
import elec332.core.util.blocks.baseCrop;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;

/**
 * Created by Elec332 on 29-12-2014.
 */
public class baseSeed extends ItemSeeds {
    public baseSeed(String name, String modID, Item cropItem){
        this(name, modID, cropItem, new baseCrop(name, modID));
    }

    private baseSeed(String name, String modID, Item cropItem, baseCrop block) {
        super(block, Blocks.farmland);
        block.seed(this);
        block.crop(cropItem);
        this.setUnlocalizedName(modID + "." + name + "seed");
        this.setTextureName(modID + ":" + name + ".seed");
        //this.setMaxDamage(0);
        //this.setHasSubtypes(true);
        registerHelper.registerItem(this, name + "seed");
    }
}