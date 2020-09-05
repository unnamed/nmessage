package me.yushust.message.format.bukkit;

import me.yushust.message.holder.LoadSource;
import me.yushust.message.holder.NodeFileLoader;
import me.yushust.message.format.bukkit.yaml.YamlFileLoader;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public final class BukkitMessageAdapt {

    private BukkitMessageAdapt() {
        throw new UnsupportedOperationException("This class couldn't be instantiated!");
    }

    public static LoadSource getPluginLoadSource(Plugin plugin, @Nullable String languagesFolder) {
        return new LoadSource(plugin.getClass().getClassLoader(), getFolder(plugin, languagesFolder));
    }

    public static LoadSource getPluginLoadSource(Plugin plugin) {
        return getPluginLoadSource(plugin, null);
    }

    public static NodeFileLoader getYamlFileLoader(Plugin plugin) {
        return new YamlFileLoader(plugin);
    }

    private static File getFolder(Plugin plugin, @Nullable String languagesFolder) {
        if (languagesFolder == null) {
            return plugin.getDataFolder();
        } else {
            File folder = new File(plugin.getDataFolder(), languagesFolder);
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    throw new IllegalStateException("Cannot create languages folder");
                }
            }
            return folder;
        }
    }

}
