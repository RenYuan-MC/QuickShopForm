package ltd.rymc.form.quickshop.shop;

import com.ghostchu.quickshop.api.QuickShopAPI;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopManager;
import com.ghostchu.quickshop.api.shop.ShopType;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class QuickShopHikari implements QuickShop {
    private static ShopManager manager = null;

    static {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("QuickShop-Hikari");
        if (plugin != null && plugin.isEnabled()) {
            manager = ((QuickShopAPI) plugin).getShopManager();
        }
    }
    private final Shop shop;
    public QuickShopHikari(Shop shop){
        this.shop = shop;
    }

    public UUID getOwner() {
        return shop.getOwner().getUniqueId();
    }

    public void openPreview(Player player) {
        shop.openPreview(player);
    }

    public boolean isEmpty() {
        if (shop.isUnlimited()) return false;
        Inventory shopInventory = ((Chest) shop.getLocation().getBlock().getState()).getBlockInventory();
        for (ItemStack item : shopInventory.getContents()) {
            if (item != null &&
                item.getType() == shop.getItem().getType() &&
                String.valueOf(item.getItemMeta()).equals(String.valueOf(shop.getItem().getItemMeta()))
            ) return false;
        }
        return true;
    }

    public double getPrice(){
        return shop.getPrice();
    }

    public void delete() {
        manager.deleteShop(shop);
    }

    public void setPrice(double price){
        shop.setPrice(price);
    }

    public void changeType(Player player) {
        if (!(player.isOp() || getOwner().equals(player.getUniqueId()))) return;
        shop.setShopType(shop.isBuying() ? ShopType.SELLING : ShopType.BUYING);
    }
}
