package me.yushust.message.format.bukkit.placeholder;

import me.yushust.message.core.placeholder.PlaceholderApplier;
import org.bukkit.entity.Player;

public class DefaultPlaceholderApplier implements PlaceholderApplier<Player> {

    public static final PlaceholderApplier<Player> INSTANCE = new DefaultPlaceholderApplier();

    private DefaultPlaceholderApplier() {}

    @Override
    public String applyPlaceholders(Player player, String text) {
        return text
                .replace("%player_name%", player.getName())
                .replace("%player_displayname%", player.getDisplayName())
                .replace("%server_online_players%", String.valueOf(player.getServer().getOnlinePlayers().size())
                .replace("%server_max_players%", String.valueOf(player.getServer().getMaxPlayers())));
    }

}
