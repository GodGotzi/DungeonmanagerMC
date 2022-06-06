package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.objects.enums.Error;
import at.gotzi.dungeonmanager.objects.skills.SkillObj;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.objects.enchantments.Glow;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SkillManager extends FileManager {


    public static HashMap<Skills, SkillObj> skillItems = new HashMap<>();

    private static YamlConfiguration skillConfig;

    public SkillManager() {
        super(YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//skills.yml")));
        skillConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//skills.yml"));
    }

    @Override
    public void initialize() {
        for (String skill : skillConfig.getKeys(false)) {
            Skills sk = Skills.getSkill(skill);
            if(sk == null) Utils.callError(skillConfig.getName() + " " + sk, Error.FileSyntax);
            String name = getString(sk, "displayName");
            if(name == null) return;
            Material material = Material.getMaterial(getStringNoColor(sk, "item.material"));
            String itemName = getString(sk, "item.itemName");
            boolean glow = getBoolean(sk, "item.glow");
            List<String> lore = new ArrayList<>();
            for (String l : getStringList(sk, "item.lore")) {
                lore.add(Utils.color(l));
            }

            int modelDataId = getInt(sk, "item.modelDataId");
            assert material != null;
            ItemStack itemStack = new ItemStack(material);
            if(material == Material.POTION) {
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                potionMeta.setBasePotionData(new PotionData(PotionType.valueOf(getStringNoColor(sk, "item.type"))));
                itemStack.setItemMeta(potionMeta);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(itemName);
            itemMeta.setCustomModelData(modelDataId);

            if(glow)
                itemMeta.addEnchant(Glow.generateEnchant(), 1, true);
            for (String enchant : getStringList(sk, "item.enchantments")) {
                Enchantment enchantment = Enchantment.getByName(enchant);
                if(enchantment == null) {
                    lore.add("§7" + enchant);
                } else
                    itemMeta.addEnchant(enchantment, 1, false);
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            skillItems.put(sk, new SkillObj(name, sk, itemStack));
        }
    }
}
