package io.github.thebusybiscuit.slimytreetaps;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.DamageableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.materials.MaterialCollections;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;

public class TreeTap extends SimpleSlimefunItem<ItemInteractionHandler> implements NotPlaceable, DamageableItem {

	private final int chance;
	private final SlimefunItemStack output;
	
	public TreeTap(Category category, SlimefunItemStack item, int chance, SlimefunItemStack output, ItemStack[] recipe) {
		super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		
		this.chance = chance;
		this.output = output;
	}
	
	@Override
	protected boolean areItemHandlersPrivate() {
		return true;
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (isItem(item)) {
				Block b = e.getClickedBlock();
				
				if (isLog(b) && SlimefunPlugin.getProtectionManager().hasPermission(p, b, ProtectableAction.BREAK_BLOCK)) {
					p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
					
					if (ThreadLocalRandom.current().nextInt(100) < chance) {
						b.setType(Material.valueOf("STRIPPED_" + b.getType().name()));
						
						Location l = b.getRelative(e.getParentEvent().getBlockFace()).getLocation().add(0.5, 0.5, 0.5);
						b.getWorld().dropItem(l, output.clone());
					}
					
					damageItem(p, item);
				}
				
				return true;
			}
			
			return false;
		};
	}

	private boolean isLog(Block b) {
		return b != null 
				&& MaterialCollections.getAllLogs().contains(b.getType()) 
				&& !b.getType().name().startsWith("STRIPPED_")
				&& !BlockStorage.hasBlockInfo(b.getLocation());
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

}
