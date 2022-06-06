package at.gotzi.dungeonmanager.manager;

import com.magmaguy.elitemobs.config.custombosses.CustomBossesConfig;
import com.magmaguy.elitemobs.config.customtreasurechests.CustomTreasureChestsConfig;
import com.magmaguy.elitemobs.config.enchantments.EnchantmentsConfig;
import com.magmaguy.elitemobs.config.enchantments.EnchantmentsConfigFields;
import com.magmaguy.elitemobs.items.customitems.CustomItem;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnchantmentManager {

    public EnchantmentManager() {
        new EnchantmentsConfig();
        List<EnchantmentsConfigFields> fields = new ArrayList<>();
        File file = new File("plugins//EliteMobs//extraEnchantments");
        for (File eee : file.listFiles()) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(new File("plugins//EliteMobs//extraEnchantments//" + eee.getName()));
            EnchantmentsConfigFields enchantmentsConfigFields = new EnchantmentsConfigFields(eee.getName(), yamlConfiguration.getBoolean("isEnabled"), yamlConfiguration.getString("name"), yamlConfiguration.getInt("maxLevel"), yamlConfiguration.getDouble("value"));
            enchantmentsConfigFields.setFileConfiguration(yamlConfiguration);
            enchantmentsConfigFields.processConfigFields();
            try {
                Field enchantmentSwitchField = enchantmentsConfigFields.getClass().getDeclaredField("enchantment");
                enchantmentSwitchField.setAccessible(true);
                enchantmentSwitchField.set(enchantmentsConfigFields, Enchantment.getByKey(NamespacedKey.minecraft("BOMBER".toLowerCase())));
                enchantmentSwitchField.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            fields.add(enchantmentsConfigFields);
        }

        try {
            Field enchantmentField = EnchantmentsConfig.class.getDeclaredField("enchantments");
            enchantmentField.setAccessible(true);
            HashMap<String, EnchantmentsConfigFields> enchantmentsConfigFieldsHashMap = (HashMap<String, EnchantmentsConfigFields>) enchantmentField.get(null);
            for (EnchantmentsConfigFields enchantmentsConfigFields : fields) {
                enchantmentsConfigFieldsHashMap.put(enchantmentsConfigFields.getFilename(), enchantmentsConfigFields);
            }
            enchantmentField.set(null, enchantmentsConfigFieldsHashMap);
            enchantmentField.setAccessible(false);
            CustomItem.initializeCustomItems();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        new CustomBossesConfig();
        new CustomTreasureChestsConfig();
    }

}
