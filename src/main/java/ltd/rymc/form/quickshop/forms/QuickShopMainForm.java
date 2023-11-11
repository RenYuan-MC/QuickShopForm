package ltd.rymc.form.quickshop.forms;

import ltd.rymc.form.quickshop.form.RCustomForm;
import ltd.rymc.form.quickshop.form.RForm;
import ltd.rymc.form.quickshop.shop.QuickShop;
import ltd.rymc.form.quickshop.utils.InputUtil;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;

public class QuickShopMainForm extends RCustomForm {

    public QuickShopMainForm(Player player, RForm previousForm, QuickShop shop) {
        super(player, previousForm);
        title("商店菜单");
        input("请输入你要购买/出售的物品数量\n物品价格:" + shop.getPrice() + "金币", "§7整数");
    }

    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        String input = response.asInput(0);
        if (!InputUtil.checkInput(input)) {
            return;
        }
        bukkitPlayer.chat(input);
    }
}
