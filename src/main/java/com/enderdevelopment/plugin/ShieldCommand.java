package com.enderdevelopment.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public class ShieldCommand implements CommandExecutor {

    private final Main plugin;

    public ShieldCommand(Main plugin) {
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

        if (plugin.getShieldCooldowns().containsKey(uuid)) {
            long remainingTime = (plugin.getShieldCooldowns().get(uuid) - System.currentTimeMillis()) / 1000;
            player.sendMessage("Shield is on cooldown. Please wait " + remainingTime + " seconds.");
            return true;
        }

        // Activate shield
        plugin.getShieldActive().put(uuid, true);

        // Set cooldown
        plugin.getShieldCooldowns().put(uuid, System.currentTimeMillis() + 40000); // 40 seconds

        player.sendMessage("Shield activated!");
        return true;
    }
}