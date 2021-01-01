package me.yushust.message.format.bukkit.language;

import me.yushust.message.specific.Linguist;
import org.bukkit.entity.Player;

public class SpigotLanguageProvider implements Linguist<Player> {

  @Override
  public String getLanguage(Player languageHolder) {
    return languageHolder.spigot().getLocale().split("_")[0];
  }

}
