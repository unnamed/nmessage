package me.yushust.message.format.bukkit.placeholder;

import me.yushust.message.core.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;

public class DefaultPlaceholderApplier implements PlaceholderReplacer<Player> {

    public static final PlaceholderReplacer<Player> INSTANCE = new DefaultPlaceholderApplier();

    private DefaultPlaceholderApplier() {}

    @Override
    public String replace(Player player, String text) {
        return text
                .replace("%player_name%", player.getName())
                .replace("%player_displayname%", player.getDisplayName())
                .replace("%server_online_players%", String.valueOf(player.getServer().getOnlinePlayers().size())
                .replace("%server_max_players%", String.valueOf(player.getServer().getMaxPlayers())));
    }

}
