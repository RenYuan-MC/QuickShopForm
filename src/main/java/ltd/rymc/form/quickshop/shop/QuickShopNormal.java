package ltd.rymc.form.quickshop.shop;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.api.shop.Shop;
import org.maxgamer.quickshop.api.shop.ShopType;

import java.util.UUID;

public class QuickShopNormal implements QuickShop {
    private final Shop shop;
    public QuickShopNormal(Shop shop){
        this.shop = shop;
    }

    public UUID getOwner(){
        return shop.getOwner();
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

    public void delete(){
        shop.delete();
    }

    public void setPrice(double price){
        shop.setPrice(price);
    }

    public void changeType(Player player) {
        if (!(player.isOp() || getOwner().equals(player.getUniqueId()))) return;
        shop.setShopType(shop.isBuying() ? ShopType.SELLING : ShopType.BUYING);
    }
}
