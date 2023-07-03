package org.knur.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

public class KnurSluchacz implements Listener {
    private Main selfInstance;
    private BosniakDrabinka jakubowaDrabina = BosniakDrabinka.getInstance();
    public KnurSluchacz(Main self) {
        this.selfInstance = self;
    }

    private PVPEventPlayer knurskiKnur;

    @EventHandler
    public void login_loginCommandListener(final AsyncPlayerChatEvent event)
    {
        if(event.getPlayer().getWorld().getName().equals("login"))
        {
            event.setCancelled(true);

            Location l = Bosniak.minekraftLoadSavedPosition(event.getPlayer().getName());
            Bukkit.getScheduler().runTask(selfInstance, () -> {
                if(l == null)
                {
                    event.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
                }
                else
                {
                    event.getPlayer().teleport(l);
                }
            });
        }
    }

    @EventHandler
    public void login_playerLogin(final AsyncPlayerPreLoginEvent e)
    {
        e.allow();
    }

    @EventHandler
    public void login_playerLogin(final PlayerLoginEvent e)
    {
        e.allow();
    }
    @EventHandler
    public void login_leaveWorld(final PlayerQuitEvent e)
    {
        if(!e.getPlayer().getWorld().getName().equals("login"))
        {
            Bosniak.minekraftSaveLocation(e.getPlayer().getName(), e.getPlayer().getLocation());
        }

        e.getPlayer().teleport(new Location(Bukkit.getWorld("login"), 0, 64, 0));
    }

    @EventHandler
    public void login_joinWorld(final PlayerJoinEvent event)
    {
        if(event.getPlayer().getWorld().getName().equals("login"))
        {
            event.getPlayer().sendMessage("Wpisz knurski aby sie zalogowac uwu");
        }
    }

    @EventHandler
    public void login_inventoryClickCancel(final InventoryDragEvent e)
    {
        if(e.getWhoClicked().getWorld().getName().equals("login"))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void login_inventoryClick(final InventoryClickEvent e)
    {
        if(e.getWhoClicked().getWorld().getName().equals("login"))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void login_inventoryOpen(final InventoryOpenEvent e)
    {
        if(e.getPlayer().getWorld().getName().equals("login"))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void login_inventoryInteract(final InventoryInteractEvent e)
    {
        if(e.getWhoClicked().getWorld().getName().equals("login"))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void login_hungerDecay(final FoodLevelChangeEvent e)
    {
        if(e.getEntity().getWorld().getName().equals("login"))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void login_damageEvent(final EntityDamageEvent e)
    {
        if(e.getEntity().getWorld().getName().equals("login"))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void login_build(final BlockCanBuildEvent e)
    {
        if(e.getPlayer().getWorld().getName().equals("login"))
        {
            if(!e.getPlayer().isOp()) e.setBuildable(false);
        }
    }
    @EventHandler
    private void login_destroy(final BlockDamageEvent e) {
        if (e.getPlayer().getWorld().getName().equals("login")) {
            if (!e.getPlayer().isOp()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        if(event.getPlayer().getName().equals("fvlvte"))
        {
            event.setFormat("[" + ChatColor.DARK_RED +   "KRUL KNURUW" + ChatColor.RESET + "] " + event.getFormat());
        }
        else if(event.getPlayer().getName().toLowerCase().equals("kiniauwu69"))
        {
            event.setFormat("[" + ChatColor.LIGHT_PURPLE +   "KRULOWA KNURUW" + ChatColor.RESET + "] " + event.getFormat());
        }
        else {
            event.setFormat("[" + ChatColor.YELLOW +   "WARCHLAK" + ChatColor.RESET + "] " + event.getFormat());
        }


        if(event.getMessage().contains("knurload"))
        {
            event.setCancelled(true);
            if(!event.getPlayer().isOp())
            {
                event.getPlayer().sendMessage("Knur nieupowazniony ok sry");
                return;
            }

            Bukkit.getScheduler().runTask(this.selfInstance, () -> {
                this.selfInstance.doReloadSelf();
            });
        }
        else if(event.getMessage().contains("switchworld knurori69"))
        {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(this.selfInstance, () -> {
                event.getPlayer().getInventory().clear();
                event.getPlayer().updateInventory();

                World w = Bukkit.getWorld("knurori69");

                event.getPlayer().teleport(w.getSpawnLocation());
            });
        }
        else if(event.getMessage().contains("switchworld world"))
        {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(this.selfInstance, () -> {
                event.getPlayer().getInventory().clear();
                event.getPlayer().updateInventory();

                World w = Bukkit.getWorld("world");

                event.getPlayer().teleport(w.getSpawnLocation());
            });
        }
        else if(event.getMessage().contains("knurarena sztart"))
        {
            event.setCancelled(true);
            if(!event.getPlayer().isOp())
            {
                event.getPlayer().sendMessage("Knur nieupowazniony ok sry");
                return;
            }

            PVPEvent.getInstance().changeState(PVPEventStates.STATE_REGISTER);
            event.getPlayer().sendMessage("OK");
        }
        else if(event.getMessage().contains("knurski loginTp"))
        {
            event.setCancelled(true);
            if(!event.getPlayer().isOp())
            {
                event.getPlayer().sendMessage("Knur nieupowazniony ok sry");
                return;
            }

           Bukkit.getScheduler().runTask(selfInstance, () -> {
               Location l = new Location(Bukkit.getWorld("login"), 0, 64, 0);
               event.getPlayer().teleport(l);
           });
        }
        else if(event.getMessage().contains("knurarena bosniak"))
        {
            event.setCancelled(true);

            Bukkit.getScheduler().runTask(this.selfInstance, () -> {
                jakubowaDrabina.otworzBosniaka(event.getPlayer());
            });
        }
        else if(event.getMessage().contains("knurarena testTp"))
        {
            event.setCancelled(true);
            if(!event.getPlayer().isOp())
            {
                event.getPlayer().sendMessage("Knur nieupowazniony ok sry");
                return;
            }

            Bukkit.getScheduler().runTask(selfInstance, () -> {
                knurskiKnur = new PVPEventPlayer(event.getPlayer());
                knurskiKnur.prepareForEvent();
            });
        }
        else if(event.getMessage().contains("knurarena testExit"))
        {
            event.setCancelled(true);
            if(!event.getPlayer().isOp())
            {
                event.getPlayer().sendMessage("Knur nieupowazniony ok sry");
                return;
            }

            Bukkit.getScheduler().runTask(selfInstance, () -> {
                knurskiKnur.restoreFromEvent();
            });
        }
        else if(event.getMessage().contains("knurarena jazda"))
        {
            event.setCancelled(true);
            if(!event.getPlayer().isOp())
            {
                event.getPlayer().sendMessage("Knur nieupowazniony ok sry");
                return;
            }

            PVPEvent.getInstance().changeState(PVPEventStates.STATE_PREPARE);
            event.getPlayer().sendMessage("OK");
        }
        else if(event.getMessage().contains("knurarena sztop"))
        {
            event.setCancelled(true);
            if(!event.getPlayer().isOp())
            {
                event.getPlayer().sendMessage("Knur nieupowazniony ok sry");
                return;
            }

            PVPEvent.getInstance().changeState(PVPEventStates.STATE_CLOSED);
            event.getPlayer().sendMessage("OK");
        }
        else if(event.getMessage().contains("knurarena rejestrancja"))
        {
            event.setCancelled(true);
            PVPEvent.getInstance().tryRegisterPlayerInEvent(event.getPlayer());
            selfInstance.broadCoUsT(event.getPlayer().getDisplayName() + " zapisał sie na knurskie zapasy");
        }
    }

    private boolean canPostawicKloc(World world, Player p)
    {
        if(world.getName().equals("knurrena"))
        {
            if(p.isOp()) return true;
            else if(p.getDisplayName().equals("FritteX")) return true;
            else return false;
        }
        return true;
    }
    private boolean canRozjebac(World world, Player p)
    {
        if(world.getName().equals("knurrena"))
        {
            if(p.isOp()) return true;
            else if(p.getDisplayName().equals("FritteX")) return true;
            else return false;
        }
        return true;
    }

    @EventHandler
    private  void knurSluchaMoby(EntitySpawnEvent e)
    {
        if(e.getEntity().getWorld().getName().equals("knurrena") && e.getEntityType() != EntityType.PLAYER)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void knurSluchaOszusta(PlayerQuitEvent event)
    {
        Player p = event.getPlayer();

        if(!p.getWorld().getName().equals("knurrena"))
        {
            return;
        }

        try {
            PVPEventPlayer player1 =  PVPEvent.getInstance().getArenaFighter1();
            PVPEventPlayer player2 = PVPEvent.getInstance().getArenaFighter2();

            if(player1 == null || player2 == null || (!player1.getPlayer().getUniqueId().equals(p.getUniqueId()) &&
                    !player2.getPlayer().getUniqueId().equals(p.getUniqueId())))
            {
                PVPEvent.getInstance().removePlayerFromTournament(p);
            }
            else
            {
                PVPEvent.getInstance().handleFightResult(p);
            }
        }
        catch (Exception e) {
            Bukkit.getLogger().info(e.getMessage());
        }
    }

    @EventHandler
    private void canKnurHitOtherKnur(EntityDamageEvent e)
    {
        if(e.getEntity().getWorld().getName().equals("knurrena"))
        {
            if (e.getEntity() instanceof Player p)
            {
                PVPEventPlayer f1 = PVPEvent.getInstance().getArenaFighter1();
                PVPEventPlayer f2 = PVPEvent.getInstance().getArenaFighter2();

                if(f1 == null || f2 == null)
                {
                    e.setCancelled(true);
                    return;
                }

                if(!f1.getPlayer().getUniqueId().equals(p.getUniqueId()) && !f2.getPlayer().getUniqueId().equals(p.getUniqueId()))
                {
                    e.setCancelled(true);
                }
                else
                {
                    if(p.getHealth() - e.getDamage() <= 0)
                    {
                        e.setCancelled(true);
                        PVPEvent.getInstance().handleFightResult(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void knurskiSluchaczBlokow(BlockDamageEvent event)
    {
        if(!canRozjebac(event.getPlayer().getWorld(), event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void knurskiSluchaczStawianiaKloca(BlockCanBuildEvent event)
    {
        Player p = event.getPlayer();
        if(p == null) return;

        if(!canPostawicKloc(event.getPlayer().getWorld(), event.getPlayer()))
            event.setBuildable(false);
    }
}
