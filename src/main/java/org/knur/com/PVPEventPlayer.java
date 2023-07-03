package org.knur.com;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;

public class PVPEventPlayer {
    private final Player p;
    private double originalHealth;
    private int originalHunger;
    private Location originalLocation;
    private ItemStack[] originalItemStack;
    private float originalExp;
    private Collection<PotionEffect> originalEffects;
    public PVPEventPlayer(Player p) {
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    public void prepareForEvent() {
        p.setGameMode(GameMode.SURVIVAL);

        World eventWorld = Bukkit.getWorld("knurrena");
        Location l = new Location(eventWorld, 0, 92, 0);

        Entity v = p.getVehicle();
        if (v != null) {
            v.eject();
        }

        originalLocation = p.getLocation().clone();

        originalHealth = p.getHealth();
        originalHunger = p.getFoodLevel();

        p.setHealth(20.0);
        p.setFoodLevel(20);

        originalItemStack = new ItemStack[p.getInventory().getSize()];

        for(int i=0; i<p.getInventory().getSize(); i++) {
            if(p.getInventory().getItem(i) != null)
            {
                originalItemStack[i] = new ItemStack(p.getInventory().getItem(i));
                originalItemStack[i].setAmount(p.getInventory().getItem(i).getAmount());
            }
            else
            {
                originalItemStack[i] = null;
            }
        }

        originalItemStack = p.getInventory().getContents().clone();

        originalEffects = new ArrayList<>(p.getActivePotionEffects());

        p.getInventory().clear();

        p.getActivePotionEffects().forEach((potionEffect -> {
            p.removePotionEffect(potionEffect.getType());
        }));

        originalExp = p.getExp();
        p.setExp(0);

        p.teleport(l);

        /*if (!p.teleport(l))
            throw new Error("Failed to teleport player");*/

        BosniakDrabinka.getInstance().otworzBosniaka(p);
    }

    public void returnToLobby() {
        World eventWorld = Bukkit.getWorld("knurrena");
        Location l = new Location(eventWorld, 0, 92, 0);

        if (!p.teleport(l))
            throw new Error("Failed to teleport player");
    }

    // klamra
    //          driven
    //                     development
    public void restoreFromEvent() {
        {

            {
                p.getPlayer().setHealth(originalHealth);
                p.getPlayer().setFoodLevel(originalHunger);

                {
                       p.getPlayer().getInventory().setContents(originalItemStack);
                       p.getPlayer().updateInventory();

                    {

                        p
                                .
                                setExp
                                        (
                                                originalExp
                                        );
                        {

                            p
                                    .
                                    teleport
                                            (
                                                    originalLocation
                                            );
                            {

                                originalEffects
                                        .
                                        forEach
                                                (
                                                        (
                                                                (
                                                                        potionEffect
                                                                )
                                                                        ->
                                                                        p
                                                                                .
                                                                                addPotionEffect
                                                                                        (
                                                                                                potionEffect
                                                                                        )
                                                        )
                                                );

                            }
                        }

                    }

                }
            }
        }
    }

}
