package ru.stonkscraft.zone;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;
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

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin instanceof WorldGuardPlugin) return (WorldGuardPlugin) plugin;
        else return null;
    }

    public String getRegionName(Player player) {
        int count = getWorldGuard().getRegionManager(player.getWorld()).getRegionCountOfPlayer(WorldGuardPlugin.inst().wrapPlayer(player));
        return count == 0 ? player.getName() : player.getName() + "_" + (count + 1);
    }

    public void createRegion(Player player, int x1, int z1, int x2, int z2, Location one, Location two) {
        BlockVector vector1 = new BlockVector(x1, 0, z1);
        BlockVector vector2 = new BlockVector(x2, 255, z2);
        String name = getRegionName(player);
        World world = player.getWorld();
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(name, vector1, vector2);
        if (getWorldGuard().getRegionManager(world).getApplicableRegions(region).size() == 0) {
            getWorldGuard().getRegionManager(world).addRegion(region);
            DefaultDomain owner = new DefaultDomain();
            owner.addPlayer(player.getName());
            Objects.requireNonNull(getWorldGuard().getRegionManager(world).getRegion(name)).setOwners(owner);
            getWorldEdit().setSelection(player, new CuboidSelection(world, one, two));
            player.sendMessage(ChatColor.GREEN + "Регион создан");
        } else {
            player.sendMessage(ChatColor.RED + "Регионы пересекаются");
        }
    }

    public int getMaxRegions(Player player) {
        int r = 1;
        if (player.hasPermission("warps.set.vip"))
            r = 3;
        if (player.hasPermission("warps.set.premium"))
            r = 5;
        if (player.hasPermission("warps.set.grand"))
            r = 10;
        if (player.hasPermission("warps.set.ultra"))
            r = 20;
        return r;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("/zone")) {
            Player player = (Player) sender;
            int one_zone_x = player.getLocation().getChunk().getX() * 16 - 16;
            int one_zone_z = player.getLocation().getChunk().getZ() * 16 - 16;
            int two_zone_x = player.getLocation().getChunk().getX() * 16 + 31;
            int two_zone_z = player.getLocation().getChunk().getZ() * 16 + 31;
            World world = player.getWorld();
            Location one_location = new Location(world, one_zone_x, 0, one_zone_z);
            Location two_location = new Location(world, two_zone_x, 255, two_zone_z);
            if (getWorldGuard().getRegionManager(player.getWorld()).getRegionCountOfPlayer(WorldGuardPlugin.inst().wrapPlayer(player)) < getMaxRegions(player))
                createRegion(player, one_zone_x, one_zone_z, two_zone_x, two_zone_z, one_location, two_location);
            else
                sender.sendMessage(ChatColor.RED + "Достигнуто максимальное количество приватов");
            return true;
        }
        return false;
    }
}
