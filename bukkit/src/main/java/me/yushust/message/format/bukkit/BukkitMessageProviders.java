package me.yushust.message.format.bukkit;

import me.yushust.message.core.LanguageProvider;
import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.SimpleMessageProvider;
import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.intercept.ReplacingMessageInterceptor;
import me.yushust.message.format.bukkit.language.SpigotLanguageProvider;
import me.yushust.message.format.bukkit.placeholder.DefaultPlaceholderApplier;
import me.yushust.message.format.bukkit.placeholder.PlaceholderApiApplier;
import me.yushust.message.format.bukkit.yaml.YamlFileLoader;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public final class BukkitMessageProviders {

    private BukkitMessageProviders() {
        throw new UnsupportedOperationException("This class couldn't be instantiated!");
    }

    public static MessageProvider<Player> getPlayerMessageProvider(Plugin plugin, NodeFileLoader nodeFileLoader,
                                                                   LanguageProvider<Player> languageProvider, String fileFormat) {
        MessageProvider<Player> messageProvider = new SimpleMessageProvider<>(
                new LoadSource(plugin.getClass().getClassLoader(), plugin.getDataFolder()),
                nodeFileLoader,
                fileFormat
        );
        messageProvider.useLanguageProvider(languageProvider);
        ReplacingMessageInterceptor<Player> interceptor = messageProvider.getInterceptor();
        interceptor.addPlaceholderApplier(DefaultPlaceholderApplier.INSTANCE);
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            interceptor.addPlaceholderApplier(PlaceholderApiApplier.INSTANCE);
        }
        return messageProvider;
    }

    public static MessageProvider<Player> getDefaultPlayerMessageProvider(Plugin plugin) {
        return getPlayerMessageProvider(
                plugin,
                new YamlFileLoader(plugin, new File(plugin.getDataFolder(), "lang")),
                new SpigotLanguageProvider(),
                "lang_%lang%.yml"
        );
    }

}
