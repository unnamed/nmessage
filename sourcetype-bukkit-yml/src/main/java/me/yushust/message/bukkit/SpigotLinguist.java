package me.yushust.message.bukkit;

import me.yushust.message.language.Linguist;
import org.bukkit.entity.Player;

/**
 * Implementation of {@link Linguist} for
 * Bukkit's {@link Player}. Gets its language
 * using {@link Player.Spigot#getLocale()}
 * and getting the language (because it returns
 * its locale in [language]_[country] format)
 *
 * <p>Only works in Spigot because uses the
 * {@link Player.Spigot} class</p>
 */
public class SpigotLinguist
  implements Linguist<Player> {

  @Override
  public String getLanguage(Player player) {
    return player.spigot().getLocale().split("_")[0];
  }

}
