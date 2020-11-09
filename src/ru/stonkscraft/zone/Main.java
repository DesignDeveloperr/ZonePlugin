package ru.stonkscraft.zone;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

    Logger log = getLogger();

    public void onEnable() {
        log.info("Zone Enabled!");
    }

    public void onDisable() {
        log.info("Zone Disable!");
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin instanceof WorldEditPlugin) return (WorldEditPlugin) plugin;
        else return null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("zone")) {
            Player player = Bukkit.getPlayer(sender.getName());
            int one_zone_x = player.getLocation().getChunk().getX() * 16 - 16;
            int one_zone_z = player.getLocation().getChunk().getZ() * 16 - 16;
            int two_zone_x = player.getLocation().getChunk().getX() * 16 + 32;
            int two_zone_z = player.getLocation().getChunk().getZ() * 16 + 32;
            World world = player.getWorld();
            Location one_location = new Location(world, one_zone_x, 0, one_zone_z);
            Location two_location = new Location(world, two_zone_x, 255, two_zone_z);
            getWorldEdit().setSelection(player, new CuboidSelection(world, one_location, two_location));
            sender.sendMessage(ChatColor.GREEN + "Регион выделен");

            return true;
        }

        return false;
    }
}
