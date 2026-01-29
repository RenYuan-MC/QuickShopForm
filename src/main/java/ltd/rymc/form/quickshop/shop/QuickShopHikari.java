package ltd.rymc.form.quickshop.shop;

import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopManager;
import com.ghostchu.quickshop.api.shop.IShopType;

import org.bukkit.entity.Player;


import java.util.UUID;

public class QuickShopHikari implements QuickShop {
    private static final ShopManager manager = com.ghostchu.quickshop.QuickShop.getInstance().getShopManager();

    private final Shop shop;
    public QuickShopHikari(Shop shop){
        this.shop = shop;
    }

    public static QuickShopHikari of(Shop shop){
        return shop == null ? null : new QuickShopHikari(shop);
    }

    public UUID getOwner() {
        return shop.getOwner().getUniqueId();
    }

    public void openPreview(Player player) {
        shop.openPreview(player);
    }

    public boolean isEmpty() {
        if (shop.isUnlimited()) return false;
        // 修复：使用Shop的库存方法而不是直接访问容器
        try {
            if (shop.isBuying()) {
                // 对于购买型商店，检查剩余空间
                return shop.getRemainingSpace() <= 0;
            } else {
                // 对于销售型商店，检查剩余库存
                return shop.getRemainingStock() <= 0;
            }
        } catch (Exception e) {
            // 如果出现异常，记录错误并返回true作为安全默认值
            e.printStackTrace();
            return true;
        }
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
        // 使用正确的API方式设置商店类型，处理类型转换
        IShopType currentType = shop.shopType();
        if (currentType.isBuying()) {
            shop.shopType("SELLING");
        } else {
            shop.shopType("BUYING");
        }
    }
}
