package com.enderdevelopment.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    private final Map<UUID, Long> explosionCooldowns = new HashMap<>();
    private final Map<UUID, Long> dashCooldowns = new HashMap<>();
    private final Map<UUID, Long> shieldCooldowns = new HashMap<>();
    private final Map<UUID, Boolean> shieldActive = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("rstrike").setExecutor(new ExplosiveStrikeCommand(this));
        getCommand("lstrike").setExecutor(new DashCommand(this));
        getCommand("kstrike").setExecutor(new ShieldCommand(this));
        startCooldownChecker();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createScoreboard(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        explosionCooldowns.remove(player.getUniqueId());
        dashCooldowns.remove(player.getUniqueId());
        shieldCooldowns.remove(player.getUniqueId());
        shieldActive.remove(player.getUniqueId());
    }

    private void createScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("abilities", "dummy", "Abilities");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score explosionScore = objective.getScore("Explosion");
        explosionScore.setScore(3);

        Score dashScore = objective.getScore("Dash");
        dashScore.setScore(2);

        Score shieldScore = objective.getScore("Shield");
        shieldScore.setScore(1);

        player.setScoreboard(scoreboard);
    }

    private void startCooldownChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkCooldowns();
            }
        }.runTaskTimer(this, 0L, 20L); // Check every second
    }

    private void checkCooldowns() {
        long currentTime = System.currentTimeMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();

            // Check explosion cooldown
            if (explosionCooldowns.containsKey(uuid)) {
                long endTime = explosionCooldowns.get(uuid);
                if (currentTime >= endTime) {
                    explosionCooldowns.remove(uuid);
                }
            }

            // Check dash cooldown
            if (dashCooldowns.containsKey(uuid)) {
                long endTime = dashCooldowns.get(uuid);
                if (currentTime >= endTime) {
                    dashCooldowns.remove(uuid);
                }
            }

            // Check shield cooldown
            if (shieldCooldowns.containsKey(uuid)) {
                long endTime = shieldCooldowns.get(uuid);
                if (currentTime >= endTime) {
                    shieldCooldowns.remove(uuid);
                    shieldActive.put(uuid, false);
                }
            }
        }
    }

    public Map<UUID, Long> getExplosionCooldowns() {
        return explosionCooldowns;
    }

    public Map<UUID, Long> getDashCooldowns() {
        return dashCooldowns;
    }

    public Map<UUID, Long> getShieldCooldowns() {
        return shieldCooldowns;
    }

    public Map<UUID, Boolean> getShieldActive() {
        return shieldActive;
    }
}