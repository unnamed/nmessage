package me.yushust.message.format.bukkit.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.yushust.message.core.placeholder.PlaceholderApplier;
import org.bukkit.entity.Player;

public class PlaceholderApiApplier implements PlaceholderApplier<Player> {

    public static final PlaceholderApplier<Player> INSTANCE = new PlaceholderApiApplier();

    private PlaceholderApiApplier() {}

    @Override
    public String applyPlaceholders(Player propertyHolder, String text) {
        return PlaceholderAPI.setPlaceholders(propertyHolder, text);
    }

}
