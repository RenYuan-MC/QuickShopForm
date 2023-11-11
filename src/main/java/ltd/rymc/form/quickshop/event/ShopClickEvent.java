package ltd.rymc.form.quickshop.event;

import ltd.rymc.form.quickshop.shop.QuickShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShopClickEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final QuickShop shop;
    private boolean cancelled;

    public ShopClickEvent(QuickShop shop, Player player) {
        this.shop = shop;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public QuickShop getShop() {
        return this.shop;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void callEvent(){
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
