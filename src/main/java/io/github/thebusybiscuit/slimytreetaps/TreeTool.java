package io.github.thebusybiscuit.slimytreetaps;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;

public class TreeTool extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable, DamageableItem {

    private final int chance;
    private final SlimefunItemStack output;

    public TreeTool(Category category, SlimefunItemStack item, int chance, SlimefunItemStack output, ItemStack[] recipe) {
        super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);

        this.chance = chance;
        this.output = output;
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            if (e.getClickedBlock().isPresent()) {
                Player p = e.getPlayer();
                Block b = e.getClickedBlock().get();

                if (isLog(b) && SlimefunPlugin.getProtectionManager().hasPermission(p, b, ProtectableAction.BREAK_BLOCK)) {
                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

                    if (ThreadLocalRandom.current().nextInt(100) < chance) {
                        b.setType(Material.valueOf("STRIPPED_" + b.getType().name()));

                        Location l = b.getRelative(e.getClickedFace()).getLocation().add(0.5, 0.5, 0.5);
                        b.getWorld().dropItem(l, output.clone());
                    }

                    damageItem(p, e.getItem());
                }
            }
        };
    }

    private boolean isLog(Block b) {
        return b != null && Tag.LOGS.isTagged(b.getType()) && !b.getType().name().startsWith("STRIPPED_") && !BlockStorage.hasBlockInfo(b.getLocation());
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

}
