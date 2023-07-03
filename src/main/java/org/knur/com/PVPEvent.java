package org.knur.com;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class PVPEvent {

    private PVPEvent() { }
    private static PVPEvent instance = new PVPEvent();
    private Main serverInstance = null;

    private PVPEventPlayer arenaFighter1;
    private PVPEventPlayer arenaFighter2;
    public PVPEventPlayer getArenaFighter1() {
        return arenaFighter1;
    }

    public PVPEventPlayer getArenaFighter2() {
        return arenaFighter2;
    }

    public static PVPEvent getInstance() {
        return instance;
    }

    private int currentRound = 0;
    private int currentPair = 0;

    private HashMap<String, PVPEventPlayer> playersCache = new HashMap<>();

    public void bindServer(Main s)
    {
        serverInstance = s;
    }
    private HashMap<String, PVPEventPlayer> playersInEvent = new HashMap<>();
    private LinkedList<LinkedList<PVPEventPair>> rounds = new LinkedList<>();
    private PVPEventStates status = PVPEventStates.STATE_CLOSED;

    public void generatePairs(int roundNumber)
    {
        LinkedList<PVPEventPair> roundList =  new LinkedList<>();
        rounds.add(roundNumber, roundList);

        if(roundNumber == 0)
        {
            ArrayList<PVPEventPlayer> playerList = new ArrayList<>(playersInEvent.values());

            while(playerList.size() > 0)
            {
                if(playerList.size() == 1)
                {
                    roundList.add(new PVPEventPair(playerList.get(0), null));

                    return;
                }
                else
                {
                    Random rand = new Random();
                    int randomIndex = rand.nextInt(playerList.size());

                    PVPEventPlayer f1 = playerList.get(randomIndex);

                    playerList.remove(f1);

                    //playersInEvent.remove(f1.getPlayer().getDisplayName());

                    randomIndex = rand.nextInt(playerList.size());

                    PVPEventPlayer f2 = playerList.get(randomIndex);

                    playerList.remove(f2);

                   //playersInEvent.remove(f2.getPlayer().getDisplayName());

                    roundList.add(new PVPEventPair(f1, f2));
                }
            }
        }
        else
        {
            LinkedList<PVPEventPair> previousPairs = getRoundPairs(roundNumber - 1);
            LinkedList<PVPEventPair> newPairs = new LinkedList<>();

            for(int i = 0; i < previousPairs.size(); i += 2)
            {
                newPairs.add(new PVPEventPair(previousPairs.get(i).getWinner(), previousPairs.size() <= i + 1 ? null : previousPairs.get(i + 1).getWinner()));
            }

            rounds.add(roundNumber, newPairs);
        }
    }

    public LinkedList<PVPEventPair> getRoundPairs(int roundNumber)
    {
        return this.rounds.get(roundNumber);
    }

    public void handleNewRound()
    {
        generatePairs(++currentRound);
        currentPair = 0;


        LinkedList<PVPEventPair> newRoundPairs = rounds.get(currentRound);

        if(newRoundPairs.size() == 1)
        {
            if(newRoundPairs.get(0).getRightPlayer() == null)
            {
                serverInstance.broadCoUsT("Knurozwyciezca uWu " + newRoundPairs.get(0).getLeftPlayer().getPlayer().getDisplayName());
                changeState(PVPEventStates.STATE_CLOSED);
                newRoundPairs.get(0).getLeftPlayer().restoreFromEvent();
                return;
            }
        }

        serverInstance.broadCoUsT("Knurozapasy runda " + (currentRound + 1) + " rusza za chwile ...");

        newRoundPairs.forEach((PVPEventPair pair) -> {
            PVPEventPlayer p1 = pair.getLeftPlayer();
            PVPEventPlayer p2 = pair.getRightPlayer();

            if(p1 != null)
            {
                BosniakDrabinka.getInstance().otworzBosniaka(p1.getPlayer());
            }

            if(p2 != null)
            {
                BosniakDrabinka.getInstance().otworzBosniaka(p2.getPlayer());
            }
        });
        Bukkit.getScheduler().runTaskLater(serverInstance, this::handleFight, 20 * 30);
    }

    public void handleFight()
    {
        LinkedList<PVPEventPair> pairs = rounds.get(currentRound);

        if(pairs.isEmpty())
        {
            handleNewRound();
            return;
        }

        if(pairs.size() <= currentPair)
        {
            handleNewRound();
            return;
        }

        PVPEventPair pair = pairs.get(currentPair);

        if(pair.getRightPlayer() != null && pair.getLeftPlayer() != null)
        {
            arenaFighter1 = pair.getLeftPlayer();
            arenaFighter2 = pair.getRightPlayer();

            serverInstance.broadCoUsT( "Knur " + arenaFighter1.getPlayer().getDisplayName() + "vs " + arenaFighter2.getPlayer().getDisplayName() + " FIGHT!!!!!!");

            if(!arenaFighter1.getPlayer().isOnline())
            {
                handleFightResult(arenaFighter2.getPlayer());
                changeState(PVPEventStates.STATE_FIGHT);
                return;
            }
            if(!arenaFighter2.getPlayer().isOnline())
            {
                handleFightResult(arenaFighter1.getPlayer());
                changeState(PVPEventStates.STATE_FIGHT);
                return;
            }

            Location l1 = new Location(Bukkit.getWorld("knurrena"), 0, 64, 12);
            Location l2 = new Location(Bukkit.getWorld("knurrena"), 0, 64, -12);

            arenaFighter1.getPlayer().teleport(l1);
            arenaFighter2.getPlayer().teleport(l2);

            changeState(PVPEventStates.STATE_FIGHT);
        }
        else
        {
            serverInstance.broadCoUsT( "Knur " + pair.getLeftPlayer().getPlayer().getDisplayName() + " dostal awans z powodu braku pary churm!");
            pair.setWinner(pair.getLeftPlayer());
            handleNewRound();
            //changeState(PVPEventStates.STATE_ELIMINATION);
        }
    }

    public void handleFightResult(Player loser)
    {
        arenaFighter1.getPlayer().setHealth(20.0);
        arenaFighter1.getPlayer().setFoodLevel(20);

        arenaFighter2.getPlayer().setHealth(20.0);
        arenaFighter2.getPlayer().setFoodLevel(20);

        if(loser.getUniqueId().equals(arenaFighter1.getPlayer().getUniqueId()))
        {
            serverInstance.broadCoUsT(arenaFighter1.getPlayer().getDisplayName() +  " dostał bencki od " + arenaFighter2.getPlayer().getDisplayName());
            arenaFighter1.restoreFromEvent();
            arenaFighter2.returnToLobby();
            //playersInEvent.remove(arenaFighter1.getPlayer().getDisplayName());
            //playersInNextRound.put(arenaFighter2.getPlayer().getDisplayName(), arenaFighter2);
            rounds.get(currentRound).get(currentPair).setWinner(arenaFighter2);
        }
        else
        {
            serverInstance.broadCoUsT(arenaFighter2.getPlayer().getDisplayName() +  " dostał bencki od " + arenaFighter1.getPlayer().getDisplayName());
            arenaFighter2.restoreFromEvent();
            arenaFighter1.returnToLobby();
            //playersInEvent.remove(arenaFighter2.getPlayer().getDisplayName());
            //playersInNextRound.put(arenaFighter1.getPlayer().getDisplayName(), arenaFighter2);
            rounds.get(currentRound).get(currentPair).setWinner(arenaFighter1);
        }

        currentPair++;

        arenaFighter1 = null;
        arenaFighter2 = null;

        //currentRound++;

        changeState(PVPEventStates.STATE_ELIMINATION);
    }

    public void removePlayerFromTournament(Player p)
    {
        try {
            this.playersCache.get(p.getDisplayName()).restoreFromEvent();
        }
       catch (Exception e) {
            Bukkit.getLogger().info(e.getMessage());
       }
    }

    public void handleStart()
    {
        generatePairs(0);

        synchronized (playersInEvent)
        {
            playersInEvent.forEach((String name, PVPEventPlayer p) -> {
                try {
                    p.prepareForEvent();
                }
                catch (Exception e) {
                    playersInEvent.remove(name);

                    Bukkit.getLogger().info("Player " + name + " kicked out of the event because of exception.");
                    e.printStackTrace();

                    p.restoreFromEvent();
                }
            });
        }

        Bukkit.getScheduler().runTaskLater(serverInstance, () -> { changeState(PVPEventStates.STATE_ELIMINATION); }, 20 * 10);
    }

    public int getCurrentRound()
    {
        return currentRound;
    }

    public void changeState(PVPEventStates state)
    {
        status = state;

        switch (state) {
            case STATE_CLOSED -> {
                serverInstance.broadCoUsT("Event PVP zakończony");
            }
            case STATE_REGISTER -> {
                serverInstance.broadCoUsT("ZAPISY NA EVENT PVP OTWARTE - MOZNA SIE ZAPISAC NA KNURSKIE ZAPASY WPISUJĄC " + ChatColor.BOLD + ChatColor.RED + "\"knurarena rejestrancja\"");
                this.playersCache = new HashMap<>();
            }
            case STATE_PREPARE -> {
                serverInstance.broadCoUsT("ZAPISY NA EVENT PVP ZAMKNIETE - ARENA ROZPOCZNIE SIE ZA MINUTĘ");
                //PVPEvent.getInstance().generatePairs(0);
                Bukkit.getScheduler().runTaskLater(serverInstance, this::handleStart, 20 * 10);
            }
            case STATE_ELIMINATION -> {
                serverInstance.broadCoUsT("KOLEJNA TURA KNUROZAPASÓW RUSZA ...");
                Bukkit.getScheduler().runTaskLater(serverInstance, this::handleFight, 20 * 10);
            }
            case STATE_FIGHT -> {
                serverInstance.broadCoUsT("ZAPASY KNURSKIE " + arenaFighter1.getPlayer().getDisplayName() + " VS " + arenaFighter2.getPlayer().getDisplayName() + " START");
            }
        }
    }
    public void tryRegisterPlayerInEvent(Player p)
    {
        if(status == PVPEventStates.STATE_CLOSED)
        {
            p.sendMessage(ChatColor.DARK_PURPLE + "[KNURPVP] Knurze nie ma miejsca w chmurze");
            return;
        }

        if (status != PVPEventStates.STATE_REGISTER) {
            p.sendMessage(ChatColor.DARK_PURPLE + "[KNURPVP] Sory jusz nie moszna");
            return;
        }

        if(playersInEvent.containsKey(p.getDisplayName()))
        {
            p.sendMessage(ChatColor.DARK_PURPLE + "[KNURPVP] Knurze już jesteś w konfiturze");
            return;
        }

        PVPEventPlayer pl =  new PVPEventPlayer(p);

        playersInEvent.put(p.getDisplayName(), pl);
        playersCache.put(p.getDisplayName(), pl);

        p.sendMessage(ChatColor.DARK_PURPLE + "[KNURPVP] Knurze zarejestrowales sie w konfiturze");
    }
}
