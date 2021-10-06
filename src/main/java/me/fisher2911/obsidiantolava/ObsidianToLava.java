package me.fisher2911.obsidiantolava;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.Override;
import java.util.Map;

public class ObsidianToLava extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onObsidianClick(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (block == null || block.getType() != Material.OBSIDIAN) {
            return;
        }

        final Player player = event.getPlayer();
        final PlayerInventory inventory = player.getInventory();
        final ItemStack itemInHand = inventory.getItemInMainHand();

        if (itemInHand == null || itemInHand.getType() != Material.BUCKET) {
            return;
        }

        final int amount = itemInHand.getAmount();

        if (amount == 1) {
            Bukkit.getScheduler().runTaskLater(this, () -> itemInHand.setType(Material.LAVA_BUCKET), 1);
        } else {
            itemInHand.setAmount(amount - 1);
            final Map<Integer, ItemStack> notAddedItems = inventory.addItem(new ItemStack(Material.LAVA_BUCKET));
            final World world = player.getWorld();
            final Location location = player.getLocation();

            notAddedItems.forEach((index, itemStack) -> world.dropItem(location, itemStack));
        }

        block.setType(Material.AIR);
    }

}
