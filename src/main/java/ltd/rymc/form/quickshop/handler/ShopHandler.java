package ltd.rymc.form.quickshop.handler;

import ltd.rymc.form.quickshop.shop.QuickShop;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;

public interface ShopHandler extends Listener {
    QuickShop getQuickShop(Block block);

}
