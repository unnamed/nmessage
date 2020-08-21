package me.yushust.message.format.bukkit.language;

import me.yushust.message.core.localization.LanguageProvider;
import org.bukkit.entity.Player;

public class SpigotLanguageProvider implements LanguageProvider<Player> {

    @Override
    public String getLanguage(Player languageHolder) {
        return languageHolder.spigot().getLocale().split("_")[0];
    }

}
