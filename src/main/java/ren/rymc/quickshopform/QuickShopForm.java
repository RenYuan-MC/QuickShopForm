package ren.rymc.quickshopform;

import org.bukkit.Bukkit;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.maxgamer.quickshop.api.event.ShopClickEvent;
import org.maxgamer.quickshop.api.shop.Shop;
import ren.rymc.quickshopform.metrics.Metrics;
import ren.rymc.quickshopform.utils.Utils;

import java.util.UUID;

public final class QuickShopForm extends JavaPlugin implements Listener {

    private Boolean shopClickStatus = false;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getClickedBlock();
        if (block == null) return;
        Shop shop = Utils.getQuickShop(block);
        if (shop == null) return;
        Action action = event.getAction();
        if (action.equals(Action.LEFT_CLICK_BLOCK) && player.isSneaking()) {
            shop.openPreview(player);
            return;
        }
        if (!shopClickStatus) {
            if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
            if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)) return;
                if (player.isOp() || shop.getOwner().equals(player.getUniqueId())) {
                    sendQuickShopSettingForm(player, shop);
                }

            }
            return;
        }
        shopClickStatus = false;
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (player.isSneaking()) {
                shop.openPreview(player);
                return;
            }
            if (Utils.isQuickShopEmpty(shop)) return;
            sendQuickShopMainForm(player, shop.getPrice());

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerAnimationEvent(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        if (!player.getGameMode().equals(GameMode.ADVENTURE)) return;
        UUID uuid = player.getUniqueId();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        Block block = player.getTargetBlock(null, 5);
        Shop shop = Utils.getQuickShop(block);
        if (shop == null) return;
        if (!shopClickStatus) {
            if (player.isSneaking() && (!block.getType().equals(Material.CHEST) || !block.getType().equals(Material.TRAPPED_CHEST))) {
                shop.openPreview(player);
            }
            return;
        }
        shopClickStatus = false;
        sendQuickShopMainForm(player, shop.getPrice());

    }

    @EventHandler
    public void ShopClickEvent(ShopClickEvent event) {
        shopClickStatus = true;
    }

    private void sendQuickShopSettingForm(Player player, Shop shop) {
        UUID uuid = player.getUniqueId();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        FloodgateApi.getInstance().getPlayer(uuid).sendForm(
                SimpleForm.builder()
                        .title("????????????")
                        .content("????????????")
                        .button("??????????????????", FormImage.Type.PATH, "textures/ui/arrow_dark_right_stretch.png")
                        .button("??????????????????", FormImage.Type.PATH, "textures/ui/store_home_icon.png")
                        .button("??c????????????", FormImage.Type.PATH, "textures/ui/realms_red_x.png")
                        .responseHandler((f, r) -> {
                            SimpleFormResponse response = f.parseResponse(r);
                            if (response.isCorrect()) {
                                int id = response.getClickedButtonId();
                                if (id == 0) Utils.changeShopType(player, shop);
                                else if (id == 1) sendQuickShopPriceSetForm(player, shop);
                                else if (id == 2) sendQuickShopConfirmForm(player, shop);
                            }
                        })
        );
    }

    private void sendQuickShopMainForm(Player player, double price) {
        UUID uuid = player.getUniqueId();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        FloodgateApi.getInstance().getPlayer(uuid).sendForm(
                CustomForm.builder()
                        .title("????????????")
                        .input("?????????????????????/?????????????????????\n????????????:" + price + "??????", "??7??????")
                        .responseHandler((f, r) -> {
                            CustomFormResponse response = f.parseResponse(r);
                            if (response.isCorrect()) {
                                String input = response.getInput(0);
                                if (input != null && !input.trim().equals("")) {
                                    player.chat(input);
                                }
                            }
                        })
        );
    }

    private void sendQuickShopConfirmForm(Player player, Shop shop) {
        if (shop == null) return;
        UUID uuid = player.getUniqueId();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        FloodgateApi.getInstance().getPlayer(uuid).sendForm(
                SimpleForm.builder()
                        .title("??l??????????")
                        .content("????????????")
                        .button("??????", FormImage.Type.PATH, "textures/ui/realms_green_check.png")
                        .responseHandler((f, r) -> {
                            SimpleFormResponse response = f.parseResponse(r);
                            if (response.isCorrect()) {
                                if (player.isOp() || shop.getOwner().equals(player.getUniqueId())) {
                                    shop.delete();
                                }
                            }
                        })
        );

    }

    private void sendQuickShopPriceSetForm(Player player, Shop shop) {
        if (shop == null) return;
        UUID uuid = player.getUniqueId();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        FloodgateApi.getInstance().getPlayer(uuid).sendForm(
                CustomForm.builder()
                        .title("??8??????????????????")
                        .input("????????????????????????????????????", "??7??????,???????????????")
                        .responseHandler((f, r) -> {
                            CustomFormResponse response = f.parseResponse(r);
                            if (response.isCorrect()) {
                                String input = response.getInput(0);
                                if (input != null && !input.trim().equals("")) {
                                    if (player.isOp() || shop.getOwner().equals(player.getUniqueId())) {
                                        try {
                                            shop.setPrice(Double.parseDouble(input));
                                        } catch (NumberFormatException ignored) {
                                        }
                                    }
                                }
                            }
                        })
        );
    }


    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        new Metrics(this, 16801);
    }

}
