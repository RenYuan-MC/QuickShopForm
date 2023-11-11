package ltd.rymc.form.quickshop.shop;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface QuickShop {
    UUID getOwner();

    void openPreview(Player player);

    boolean isEmpty();

    double getPrice();

    void delete();

    void setPrice(double price);

    void changeType(Player player);

}
