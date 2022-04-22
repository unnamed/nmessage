package me.yushust.message.bungee;

import me.yushust.message.language.Linguist;
import me.yushust.message.source.MessageSource;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

/**
 * Facade that adds static factory
 * methods for creating {@link Linguist}
 * and {@link YamlMessageSource}
 */
public class BungeeMessageAdapt {

  private BungeeMessageAdapt() {
  }

  /**
   * Creates a new {@link YamlMessageSource} for the
   * specified {@code plugin}, loading the files from the
   * specified {@code folder} and using the provided {@code fileFormat}
   * to get the filenames using its language
   */
  public static MessageSource newYamlSource(
    Plugin plugin, File folder, String fileFormat
  ) {
    return new YamlMessageSource(plugin, folder, fileFormat);
  }

  /**
   * Creates a new {@link YamlMessageSource} for the
   * specified {@code plugin}, loading the files from the
   * specified {@code folder} and using the default file format
   * ("lang_%lang%.yml") to get the filenames using its language
   */
  public static MessageSource newYamlSource(Plugin plugin, File folder) {
    return newYamlSource(plugin, folder, "lang_%lang%.yml");
  }

  /**
   * Creates a new {@link YamlMessageSource} for the
   * specified {@code plugin}, loading the files from the
   * plugin data folder and using the provided {@code fileFormat}
   * to get the filenames using its language
   */
  public static MessageSource newYamlSource(Plugin plugin, String fileFormat) {
    return newYamlSource(plugin, plugin.getDataFolder(), fileFormat);
  }

  /**
   * Creates a new {@link YamlMessageSource} for the
   * specified {@code plugin}, loading the files from the
   * plugin data folder and using the default file format
   * ("lang_%lang%.yml") to get the filenames using its language
   */
  public static MessageSource newYamlSource(Plugin plugin) {
    return newYamlSource(plugin, plugin.getDataFolder());
  }

  /**
   * Creates a new {@link BungeeLinguist}. It obtains the
   * player language by using {@link ProxiedPlayer#getLocale}
   */
  public static Linguist<ProxiedPlayer> newBungeeLinguist() {
    return new BungeeLinguist();
  }

}
