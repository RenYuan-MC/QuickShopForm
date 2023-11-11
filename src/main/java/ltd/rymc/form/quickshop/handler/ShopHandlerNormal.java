package ltd.rymc.form.quickshop.handler;

import ltd.rymc.form.quickshop.QuickShopForm;
import ltd.rymc.form.quickshop.shop.QuickShop;
import ltd.rymc.form.quickshop.shop.QuickShopNormal;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.maxgamer.quickshop.api.event.PlayerShopClickEvent;
import org.maxgamer.quickshop.api.shop.Shop;
import org.maxgamer.quickshop.api.shop.ShopManager;

public class ShopHandlerNormal implements ShopHandler {
    private static final ShopManager manager = org.maxgamer.quickshop.QuickShop.getInstance().getShopManager();
    public QuickShop getQuickShop(Block block) {
        return new QuickShopNormal(getQuickShop0(block));
    }

    private static Shop getQuickShop0(Block block){
        if (block == null) return null;
        Shop shop = manager.getShop(block.getLocation());
        if (shop != null) return shop;
        BlockData Drops = block.getBlockData();
        if (!(Drops instanceof Directional)) return null;
        return manager.getShop(block.getRelative(((Directional) Drops).getFacing(),-1).getLocation());
    }

    @EventHandler(ignoreCancelled = true)
    public void ShopClickEvent(PlayerShopClickEvent event) {
        QuickShop shop = new QuickShopNormal(event.getShop());
        boolean cancelled = QuickShopForm.callShopClickEvent(shop, event.getPlayer());
        event.setCancelled(cancelled);
    }
}
