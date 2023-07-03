package org.knur.com;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.LinkedList;

public class BosniakDrabinka implements Listener {
    private Inventory inv;

    private BosniakDrabinka() {
        inv = Bukkit.createInventory(null, 54, "KNURZY TURNIEJ RUNDA " + PVPEvent.getInstance().getCurrentRound() + 1);
    }

    private static BosniakDrabinka instance = new BosniakDrabinka();

    public static BosniakDrabinka getInstance() {
        return instance;
    }

    protected ItemStack createGuiItem(final Material material, Player p, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final SkullMeta meta = (SkullMeta) item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);
        meta.setOwningPlayer(p);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
    public void otworzBosniaka(Player p) {
        inv = Bukkit.createInventory(null, 54, "KNURZY TURNIEJ RUNDA " + PVPEvent.getInstance().getCurrentRound() + 1);

        LinkedList<PVPEventPair> pairs = PVPEvent.getInstance().getRoundPairs(PVPEvent.getInstance().getCurrentRound());

        if(pairs == null)
            return;

        int pairIndex = 0;

        for (PVPEventPair pair : pairs) {
            int invSlotIndex1 = pairIndex * 3;
            int invSlotIndex2 = pairIndex * 3 + 1;

            if(pair.getLeftPlayer() != null)
            {
                inv.setItem(invSlotIndex1, createGuiItem(Material.PLAYER_HEAD, pair.getLeftPlayer().getPlayer(), pair.getLeftPlayer().getPlayer().getDisplayName()));
            }
            if(pair.getRightPlayer() != null)
            {
                inv.setItem(invSlotIndex2, createGuiItem(Material.PLAYER_HEAD, pair.getRightPlayer().getPlayer(), pair.getRightPlayer().getPlayer().getDisplayName()));
            }

            pairIndex++;
        }

        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }


}
