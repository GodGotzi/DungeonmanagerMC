package at.gotzi.dungeonmanager.listener.group;

import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.commands.group.commands.TeleCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.OresManager;
import at.gotzi.dungeonmanager.manager.player.InteractManager;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BreakBlockListener implements Listener {


    @EventHandler
    public void onBreak(BlockBreakEvent event) {
         Player player = event.getPlayer();
        InteractManager interactManager = new InteractManager(player.getUniqueId(), player.getLocation());
        if (!interactManager.isAllowedStructure()) {
            player.sendMessage(Messages.restricted);
            event.setCancelled(true);
            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (OresManager.isMaterial(event.getBlock().getType())) {
                if (OresManager.isEmpty(player.getUniqueId()))
                    OresManager.createNew(player.getUniqueId());
                if (OresManager.hasLeft(player.getUniqueId(), event.getBlock().getType()))
                    OresManager.addBreak(player.getUniqueId(), event.getBlock().getType());
                else {
                    player.sendMessage(Messages.tooMuchOresMined.replace("%MATERIAL%", event.getBlock().getType().toString()));
                    event.setCancelled(true);
                }
            }
        }

         if (TeleCommand.players.contains(player.getUniqueId())) {
             if (!SkillCommand.isDisabled(player, true)){
                 if (event.isDropItems()) {
                     event.setDropItems(false);
                     Material material = event.getBlock().getBlockData().getMaterial();
                     ItemStack block = new ItemStack(material);
                     player.getInventory().addItem(block);
                 }
             }
         }

        if (SkillCommand.isDisabled(player, true)) return;
        List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
        Skill skill = null;
        boolean bool = false;
        for (Skill sks : skills) {
            if (sks.getSkills() == Skills.SILKTOUCHSPAWNERS) {
                bool = true;
                skill = sks;
                break;
            }
        }

        if(player.hasPermission("groups.all") || bool) {
            if (event.getBlock().getBlockData().getMaterial() == Material.SPAWNER) {
                if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
                CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();
                EntityType entityType = creatureSpawner.getSpawnedType();
                ItemStack spawner = new ItemStack(Material.SPAWNER);
                ItemMeta itemMeta = spawner.getItemMeta();
                itemMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 1, true);
                List<String> lore = new ArrayList<>();
                lore.add("Entitytype: " + entityType);
                itemMeta.setLore(lore);
                spawner.setItemMeta(itemMeta);
                Location location = event.getBlock().getLocation();
                player.getWorld().dropItem(location, spawner);
            }
        }
    }
}
