package io.github.thebusybiscuit.slimytreetaps;

import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class MagicalMirrorListener implements Listener {

    private final MagicalMirror mirror;

    public MagicalMirrorListener(TreeTaps plugin, MagicalMirror mirror) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.mirror = mirror;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ItemFrame) {
            ItemFrame frame = (ItemFrame) e.getRightClicked();
            ItemStack item = frame.getItem();

            if (mirror.isItem(item)) {
                e.setCancelled(true);

                if (mirror.canUse(e.getPlayer(), true)) {
                    mirror.teleport(e.getPlayer(), item);
                }
            }
        }
    }

}
