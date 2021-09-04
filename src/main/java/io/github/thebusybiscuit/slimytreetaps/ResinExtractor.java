package io.github.thebusybiscuit.slimytreetaps;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public abstract class ResinExtractor extends AContainer implements RecipeDisplayItem {

    public ResinExtractor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "Resin Extractor";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_HOE);
    }

    @Override
    public String getMachineIdentifier() {
        return "RESIN_EXTRACTOR";
    }

}
