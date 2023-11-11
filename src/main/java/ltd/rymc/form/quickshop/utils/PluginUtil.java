package ltd.rymc.form.quickshop.utils;

import org.bukkit.Bukkit;

public class PluginUtil {
    public static boolean hasPlugin(String... plugins){
        for (String plugin : plugins) if (hasPlugin(plugin)) return false;
        return true;
    }

    public static boolean hasPlugin(String plugin){
        return Bukkit.getPluginManager().getPlugin(plugin) != null;
    }
}
