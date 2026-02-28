package eu.endermite.censura.notification;

import eu.endermite.censura.Censura;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StaffNotification {
    private final Set<Player> staffNotify;

    public StaffNotification(Censura plugin) {
        staffNotify = ConcurrentHashMap.newKeySet();
        findStaff();

        plugin.getServer().getPluginManager().registerEvents(new NotificationListener(this), plugin);
    }

    public void addStaff(Player player) {
        staffNotify.add(player);
    }

    public void removeStaff(Player player) {
        staffNotify.remove(player);
    }

    public void clearStaff() {
        staffNotify.clear();
    }

    public boolean contains(Player player) {
        return staffNotify.contains(player);
    }

    public void findStaff() {
        staffNotify.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("censura.notify")) {
                staffNotify.add(player);
            }
        }
    }

    public void sendNotification(String message) {
        if (!Censura.getCachedConfig().isNotifyDetections()) return;

        for (Player player : staffNotify) {
            player.sendMessage(message);
        }
    }

}
