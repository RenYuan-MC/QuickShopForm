package ltd.rymc.form.quickshop.listener;

import ltd.rymc.form.quickshop.QuickShopForm;
import ltd.rymc.form.quickshop.event.ShopClickEvent;
import ltd.rymc.form.quickshop.forms.QuickShopMainForm;
import ltd.rymc.form.quickshop.forms.QuickShopSettingForm;
import ltd.rymc.form.quickshop.shop.QuickShop;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.geysermc.floodgate.api.FloodgateApi;

public class ShopListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerInteractEvent(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) return;

        Block block = event.getClickedBlock();
        if (block == null) return;
        QuickShop shop = QuickShopForm.getShopHandler().getQuickShop(block);
        if (shop == null) return;

        Action action = event.getAction();

        if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (player.isSneaking()) shop.openPreview(player);
            return;
        }

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST) || block.getType().equals(Material.BARREL)) return;
        if (player.isOp() || shop.getOwner().equals(player.getUniqueId())) {
            new QuickShopSettingForm(player, null, shop).send();
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerAnimationEvent(PlayerAnimationEvent event) {

        Player player = event.getPlayer();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) return;

        if (!player.getGameMode().equals(GameMode.ADVENTURE)) return;

        Block block = player.getTargetBlock(null, 5);
        QuickShop shop = QuickShopForm.getShopHandler().getQuickShop(block);
        if (shop == null) return;

        if (!player.isSneaking()) return;
        if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)) return;
        shop.openPreview(player);

    }

    @EventHandler
    public void ShopClickEvent(ShopClickEvent event) {
        QuickShop shop = event.getShop();
        if (shop.isEmpty()) return;
        new QuickShopMainForm(event.getPlayer(), null, shop).send();
    }
}
