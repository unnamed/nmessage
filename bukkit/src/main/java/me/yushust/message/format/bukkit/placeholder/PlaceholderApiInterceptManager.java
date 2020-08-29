package me.yushust.message.format.bukkit.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.yushust.message.core.intercept.DefaultInterceptManager;
import me.yushust.message.core.intercept.InterceptContext;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.placeholder.PlaceholderProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class PlaceholderApiInterceptManager extends DefaultInterceptManager<Player> {

    private final PluginPlaceholderExpansion expansion;

    public PlaceholderApiInterceptManager(Plugin plugin, String linkedMessagePrefix) {
        super(linkedMessagePrefix);
        this.expansion = new PluginPlaceholderExpansion(plugin, this);
    }

    @Override
    public InterceptManager<Player> add(PlaceholderProvider<Player> replacer) {

        if (!expansion.isRegistered()) {
            PlaceholderAPI.registerExpansion(expansion);
        }

        return super.add(replacer);
    }

    @Override
    public String convert(InterceptContext<Player> context, String text, Collection<String> linkedPaths) {
        // TODO: Add linked messages to this shit
        return PlaceholderAPI.setPlaceholders(context.getEntity(), text);
    }

}
