package me.yushust.message.format.bukkit.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.yushust.message.core.intercept.InterceptContext;
import me.yushust.message.core.intercept.MessageInterceptor;
import me.yushust.message.core.placeholder.PlaceholderProvider;

import org.bukkit.entity.Player;

public class PlaceholderApiInterceptor implements MessageInterceptor<Player> {

    public static final PlaceholderProvider<Player> INSTANCE = new PlaceholderApiInterceptor();

    private PlaceholderApiInterceptor() {
    }

    @Override
    public String replace(InterceptContext<Player> context, String text) {
        return PlaceholderAPI.setPlaceholders(context.getEntity(), text);
    }

}
