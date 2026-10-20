package com.enderdevelopment.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.UUID;

public class ExplosiveStrikeCommand implements CommandExecutor {

    private final Main plugin;

    public ExplosiveStrikeCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (plugin.getExplosionCooldowns().containsKey(uuid)) {
            long remainingTime = (plugin.getExplosionCooldowns().get(uuid) - System.currentTimeMillis()) / 1000;
            player.sendMessage("Explosive strike is on cooldown. Please wait " + remainingTime + " seconds.");
            return true;
        }

        Location playerLocation = player.getLocation();
        World world = player.getWorld();

        // Create explosion at player's location
        world.createExplosion(playerLocation, 3.0f, false, false, player);

        // Set cooldown
        plugin.getExplosionCooldowns().put(uuid, System.currentTimeMillis() + 30000); // 30 seconds

        player.sendMessage("Explosive strike activated!");
        return true;
    }
}