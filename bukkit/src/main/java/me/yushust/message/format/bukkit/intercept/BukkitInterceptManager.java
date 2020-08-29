package me.yushust.message.format.bukkit.intercept;

import me.yushust.message.core.intercept.DefaultInterceptManager;
import me.yushust.message.core.intercept.InterceptContext;
import me.yushust.message.core.placeholder.PlaceholderProvider;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

public class BukkitInterceptManager extends DefaultInterceptManager<Player> {

    @Override
    public Optional<PlaceholderProvider<Player>> findProvider(String placeholder) {
        return Optional.empty();
    }

    @Override
    public String convert(InterceptContext<Player> context, String text, Collection<String> linkedPaths) {
        return null;
    }
}
