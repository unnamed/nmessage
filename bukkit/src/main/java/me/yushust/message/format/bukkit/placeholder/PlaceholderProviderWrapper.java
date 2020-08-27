package me.yushust.message.format.bukkit.placeholder;

import me.clip.placeholderapi.PlaceholderHook;
import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.intercept.InterceptContext;
import me.yushust.message.core.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;

import static java.util.Objects.requireNonNull;

public class PlaceholderProviderWrapper extends PlaceholderHook {

    private final MessageProvider<Player> messageProvider;
    private final PlaceholderReplacer<Player> placeholderProvider;

    public PlaceholderProviderWrapper(MessageProvider<Player> messageProvider, PlaceholderReplacer<Player> placeholderProvider) {
        requireNonNull(messageProvider);
        requireNonNull(placeholderProvider);
        this.messageProvider = messageProvider;
        this.placeholderProvider = placeholderProvider;
    }

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
        return placeholderProvider.replace(
                new InterceptContext<>(messageProvider, player),
                placeholder
        );
    }

}
