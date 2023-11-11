package ltd.rymc.form.quickshop.forms;

import ltd.rymc.form.quickshop.form.RCustomForm;
import ltd.rymc.form.quickshop.form.RForm;
import ltd.rymc.form.quickshop.shop.QuickShop;
import ltd.rymc.form.quickshop.utils.InputUtil;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

public class QuickShopPriceSetForm extends RCustomForm {
    private final QuickShop shop;

    public QuickShopPriceSetForm(Player player, RForm previousForm, QuickShop shop) {
        super(player, previousForm);
        this.shop = shop;
        title("§8商店价格修改");
        input("请输入你要修改的商店价格", "§7数字,可以是小数");
    }

    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        String input = response.asInput(0);

        if (!InputUtil.checkInput(input)) {
            return;
        }

        if (!bukkitPlayer.isOp() && !shop.getOwner().equals(bukkitPlayer.getUniqueId())) {
            return;
        }

        try {
            shop.setPrice(Double.parseDouble(input));
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public void onClosedOrInvalidResult(CustomForm form, FormResponseResult<CustomFormResponse> response) {
        sendPrevious();
    }
}
