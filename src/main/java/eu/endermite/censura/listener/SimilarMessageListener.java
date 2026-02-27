package eu.endermite.censura.listener;

import eu.endermite.censura.Censura;
import eu.endermite.censura.PlayerChatHistory;
import eu.endermite.censura.filter.Filter;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class SimilarMessageListener implements Listener {
    private final JaroWinklerSimilarity similiarity = new JaroWinklerSimilarity();
    private final HashMap<UUID, PlayerChatHistory> chatHistory = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChatSimilarEvent(AsyncPlayerChatEvent event) {
        UUID playerUuid = event.getPlayer().getUniqueId();
        PlayerChatHistory messages = chatHistory.get(playerUuid);
        String currentMessage = event.getMessage();

        if (messages == null) {
            PlayerChatHistory newPlayerChatHistory = new PlayerChatHistory();
            newPlayerChatHistory.addMessage(currentMessage);
            chatHistory.put(playerUuid, newPlayerChatHistory);
            return;
        }

        for (String message : messages.getMessageHistory()) {
            int percent = (int) (similiarity.apply(message, currentMessage) * 100);

            if (percent > Censura.getCachedConfig().getSimilarMessageThreshold()) {
                event.setCancelled(true);
                Filter.doActions(Censura.getCachedConfig().getSimilarCheckActions(), event.getPlayer());
                return;
            }
        }
        messages.addMessage(currentMessage);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        chatHistory.remove(event.getPlayer().getUniqueId());
    }

}
