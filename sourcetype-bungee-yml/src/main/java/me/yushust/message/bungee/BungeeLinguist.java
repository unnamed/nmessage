package me.yushust.message.bungee;

import me.yushust.message.language.Linguist;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link Linguist} for
 * Bungee {@link ProxiedPlayer}. Gets its language
 * using {@link ProxiedPlayer#getLocale()}
 */
public class BungeeLinguist implements Linguist<ProxiedPlayer> {

  @Override
  public @Nullable String getLanguage(ProxiedPlayer proxiedPlayer) {
    return proxiedPlayer.getLocale().getLanguage();
  }

}
