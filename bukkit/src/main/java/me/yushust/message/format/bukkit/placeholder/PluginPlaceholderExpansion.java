package me.yushust.message.format.bukkit.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.yushust.message.core.intercept.InterceptContext;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.placeholder.PlaceholderProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class PluginPlaceholderExpansion extends PlaceholderExpansion {

    private final InterceptManager<Player> interceptManager;
    private final Plugin plugin;

    public PluginPlaceholderExpansion(Plugin plugin, InterceptManager<Player> interceptManager) {
        this.plugin = requireNonNull(plugin);
        this.interceptManager = requireNonNull(interceptManager);
    }

    @Override
    public String getIdentifier() {
        return plugin.getName();
    }

    @Override
    public String getAuthor() {
        List<String> authors = plugin.getDescription().getAuthors();
        if (authors == null || authors.isEmpty()) {
            return "unknown";
        }
        return String.join(", ", authors);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        Optional<PlaceholderProvider<Player>> optionalPlaceholderProvider = interceptManager.findProvider(params);

        return optionalPlaceholderProvider.map(playerPlaceholderReplacer -> playerPlaceholderReplacer.replace(
                new InterceptContext<>(interceptManager.getMessageProvider(), player),
                params
        )).orElse(null);

    }

}
