package com.enderdevelopment.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.UUID;

public class DashCommand implements CommandExecutor {

    private final Main plugin;

    public DashCommand(Main plugin) {
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

        if (plugin.getDashCooldowns().containsKey(uuid)) {
            long remainingTime = (plugin.getDashCooldowns().get(uuid) - System.currentTimeMillis()) / 1000;
            player.sendMessage("Dash is on cooldown. Please wait " + remainingTime + " seconds.");
            return true;
        }

        // Get the player's current direction vector
        Vector direction = player.getLocation().getDirection().normalize();

        // Multiply the vector to increase the dash distance
        direction.multiply(2.0);

        // Set the player's velocity to the dash direction
        player.setVelocity(direction);

        // Set cooldown
        plugin.getDashCooldowns().put(uuid, System.currentTimeMillis() + 15000); // 15 seconds

        player.sendMessage("Dash activated!");
        return true;
    }
}