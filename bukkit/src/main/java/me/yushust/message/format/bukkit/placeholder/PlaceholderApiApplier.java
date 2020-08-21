package me.yushust.message.format.bukkit.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.yushust.message.core.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;

public class PlaceholderApiApplier implements PlaceholderReplacer<Player> {

    public static final PlaceholderReplacer<Player> INSTANCE = new PlaceholderApiApplier();

    private PlaceholderApiApplier() {}

    @Override
    public String replace(Player propertyHolder, String text) {
        return PlaceholderAPI.setPlaceholders(propertyHolder, text);
    }

}
