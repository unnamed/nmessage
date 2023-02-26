package me.yushust.message.bukkit;

import me.yushust.message.language.Linguist;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    private final static Method GET_LOCALE;

    static {
        Method getLocale = null;
        try {
            getLocale = Player.class.getMethod("getLocale");
        } catch (NoSuchMethodException ignored) {
            // Ignore
        }
        GET_LOCALE = getLocale;

    }

    @Override
    public String getLanguage(Player player) {

        if (GET_LOCALE == null) {
            return player.spigot().getLocale().split("_")[0];
        } else {
            try {
                return ((String) GET_LOCALE.invoke(player)).split("_")[0];
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Failed to get locale from player", e);
            }
        }


    }

}
