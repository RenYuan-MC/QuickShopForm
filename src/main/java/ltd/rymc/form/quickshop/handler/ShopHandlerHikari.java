package ltd.rymc.form.quickshop.handler;

import com.ghostchu.quickshop.api.QuickShopAPI;
import com.ghostchu.quickshop.api.event.ShopClickEvent;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopManager;
import ltd.rymc.form.quickshop.QuickShopForm;
import ltd.rymc.form.quickshop.shop.QuickShop;
import ltd.rymc.form.quickshop.shop.QuickShopHikari;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

public class ShopHandlerHikari implements ShopHandler {

    private static QuickShopAPI api = null;
    private static ShopManager manager = null;

    static {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("QuickShop-Hikari");
        if (plugin != null && plugin.isEnabled()) {
            api = (QuickShopAPI) plugin;
            manager = api.getShopManager();
        }
    }

    public QuickShop getQuickShop(Block block) {
        return new QuickShopHikari(getQuickShop0(block));
    }

    private static Shop getQuickShop0(Block block) {
        if (api == null) return null;
        if (block == null) return null;
        Shop shop = manager.getShop(block.getLocation());
        if (shop != null) return shop;
        BlockData Drops = block.getBlockData();
        if (!(Drops instanceof Directional)) return null;
        return manager.getShop(block.getRelative(((Directional) Drops).getFacing(),-1).getLocation());
    }

    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void ShopClickEvent(ShopClickEvent event) {
        QuickShop shop = new QuickShopHikari(event.getShop());
        boolean cancelled = QuickShopForm.callShopClickEvent(shop, event.getClicker());
        event.setCancelled(cancelled);
    }

}
