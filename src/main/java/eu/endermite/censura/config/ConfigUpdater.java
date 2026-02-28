package eu.endermite.censura.config;

import eu.endermite.censura.Censura;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public class ConfigUpdater {

    private final Censura plugin;

    public ConfigUpdater(Censura plugin) {
        this.plugin = plugin;
    }

    public void update() {
        boolean changed = migrateSimilarity(plugin.getConfig());

        if (changed) {
            plugin.saveConfig();
            plugin.getLogger().info("Config updated to latest version.");
        }
    }

    private boolean migrateSimilarity(FileConfiguration config) {
        boolean changed = false;

        if (config.contains("similarity.message-amount")) {
            int oldValue = config.getInt("similarity.message-amount", 3);
            config.set("similarity.messages-to-check", oldValue);
            String comment1 = "How many previous messages to check";
            config.setComments("similarity.messages-to-check", addComments(comment1));

            config.set("similarity.max-similar-messages", 1);
            String comment2 = "Minimum number of similar previous messages required to block";
            config.setComments("similarity.max-similar-messages", addComments(comment2));

            config.set("similarity.message-amount", null);
            changed = true;
        }

        return changed;
    }

    private static List<String> addComments(String... comments) {

        return Arrays.asList(comments);
    }
}
