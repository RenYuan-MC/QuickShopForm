package ltd.rymc.form.quickshop;

import ltd.rymc.form.quickshop.event.ShopClickEvent;
import ltd.rymc.form.quickshop.handler.ShopHandler;
import ltd.rymc.form.quickshop.handler.ShopHandlerHikari;
import ltd.rymc.form.quickshop.handler.ShopHandlerNormal;
import ltd.rymc.form.quickshop.listener.ShopListener;
import ltd.rymc.form.quickshop.metrics.Metrics;
import ltd.rymc.form.quickshop.shop.QuickShop;
import ltd.rymc.form.quickshop.utils.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuickShopForm extends JavaPlugin {


    private static ShopHandler shopHandler;

    @Override
    public void onEnable() {
        // Here's a new plugin I'm working on that will bring together all the features of this plugin.
        if (PluginUtil.hasPlugin("BedrockSupporter") || PluginUtil.hasPlugin("BedrockSupporter-Bukkit")){
            getLogger().warning("BedrockSupporter already includes all the features of this plugin!");
            getLogger().warning("You don't need to install this plugin, it will be disabled later.");
            Bukkit.getPluginManager().disablePlugin(this);
        }


        if (PluginUtil.hasPlugin("QuickShop")){
            getLogger().info("Detect: QuickShop");
            shopHandler = new ShopHandlerNormal();
        } else if (PluginUtil.hasPlugin("QuickShop-Hikari")){
            getLogger().info("Detect: QuickShop-Hikari");
            shopHandler = new ShopHandlerHikari();
        } else {
            getLogger().severe("QuickShop is not detected, the plugin will be disabled!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        ShopListener shopListener = new ShopListener();

        // For ShopClickEvent
        Bukkit.getPluginManager().registerEvents(shopHandler, this);

        // Main listener
        Bukkit.getPluginManager().registerEvents(shopListener, this);

        new Metrics(this, 16801);
    }

    public static ShopHandler getShopHandler(){
        return shopHandler;
    }

    public static boolean callShopClickEvent(QuickShop shop, Player player) {
        ShopClickEvent event = new ShopClickEvent(shop, player);
        event.callEvent();
        return event.isCancelled();
    }

}
