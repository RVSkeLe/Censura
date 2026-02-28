package eu.endermite.censura.command;

import eu.endermite.censura.Censura;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CensuraCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendCredits(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            handleReload(sender);
        }
        else if (args[0].equalsIgnoreCase("toggle")) {
            handleToggle(sender);
        } else {
            sender.sendMessage(Censura.getCachedConfig().getNoSuchCommand());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        HashMap<String,String> allSubCommands = new HashMap<>();
        List<String> result = new ArrayList<>();

        allSubCommands.put("reload", "censura.reload");
        allSubCommands.put("toggle", "censura.toggle");

        if (args.length == 1) {
            for (Map.Entry<String,String> sub : allSubCommands.entrySet()) {
                if (sub.getKey().startsWith(args[0].toLowerCase()) && sender.hasPermission(sub.getValue()))
                    result.add(sub.getKey());
            }
        }
        return result;
    }

    public void sendCredits(CommandSender sender) {
        PluginDescriptionFile desc = Censura.getPlugin().getDescription();
        sender.sendMessage("Censura " + desc.getVersion() + " by YouHaveTrouble");
        assert desc.getDescription() != null;
        sender.sendMessage(desc.getDescription());
    }

    private void handleReload(CommandSender sender) {
        if (sender.hasPermission("censura.reload")) {
            Censura.getPlugin().asyncReloadConfigCache(sender);
        } else {
            sender.sendMessage(Censura.getCachedConfig().getNoPermission());
        }
    }

    private void handleToggle(CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (!player.hasPermission("censura.toggle")) {
            player.sendMessage(Censura.getCachedConfig().getNoPermission());
            return;
        }

        if (!player.hasPermission("censura.notify")) {
            player.sendMessage(Censura.getCachedConfig().getNoPermission());
            return;
        }

        if (Censura.getStaffNotification().contains(player)) {
            Censura.getStaffNotification().removeStaff(player);
            player.sendMessage(Censura.getCachedConfig().getNotificationDisabled());
        } else {
            Censura.getStaffNotification().addStaff(player);
            player.sendMessage(Censura.getCachedConfig().getNotificationEnabled());
        }
    }
}
