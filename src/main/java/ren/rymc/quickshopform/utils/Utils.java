package ren.rymc.quickshopform.utils;

import com.ghostchu.quickshop.api.QuickShopAPI;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopType;
import org.bukkit.plugin.Plugin;

public class Utils {
    private static QuickShopAPI quickShopAPI = null;

    static {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("QuickShop-Hikari");
        if (plugin != null && plugin.isEnabled()) quickShopAPI = (QuickShopAPI) plugin;
    }

    public static Boolean isQuickShopEmpty(Shop shop) {
        if (quickShopAPI == null) return false;
        if (shop == null) return false;
        if (shop.isUnlimited()) return false;
        Inventory shopInventory = ((Chest) shop.getLocation().getBlock().getState()).getBlockInventory();
        for (ItemStack item : shopInventory.getContents()) {
            if (item != null && item.getType() == shop.getItem().getType() && String.valueOf(item.getItemMeta()).equals(String.valueOf(shop.getItem().getItemMeta())))
                return false;
        }
        return true;
    }

    public static Shop getQuickShop(Block block) {
        if (quickShopAPI == null) return null;
        if (block == null) return null;
        Shop shop = quickShopAPI.getShopManager().getShop(block.getLocation());
        if (shop != null) return shop;
        BlockData Drops = block.getBlockData();
        if (!(Drops instanceof Directional)) return null;
        return quickShopAPI.getShopManager().getShop(block.getRelative(((Directional) Drops).getFacing(),-1).getLocation());
    }

    public static void changeShopType(Player player, Shop shop) {
        if (quickShopAPI == null) return;
        if (shop == null) return;
        if (!(player.isOp() || shop.getOwner().equals(player.getUniqueId()))) return;
        shop.setShopType(shop.isBuying() ? ShopType.SELLING : ShopType.BUYING);
    }
}
