package at.gotzi.dungeonmanager.listener.playerlistener;

import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class PaymentListener implements Listener {

    @EventHandler
    public void onPay(JobsPaymentEvent event) {
        Player player = event.getPlayer().getPlayer();
        if (player == null) return;
        if (SkillCommand.isDisabled(player, false)) return;
        List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
        Skill skill = null;
        double boost = 0;
        boolean bool = false;
        for (Skill sks : skills) {
            if (sks.getSkills() == Skills.MONEYBOOST) {
                bool = true;
                boost += sks.getStrength();
                break;
            }
        }
        if (bool) {
            if (boost == 0) return;
            double amount = event.getPayment().get(CurrencyType.MONEY) - (event.getPayment().get(CurrencyType.MONEY) * (boost / 100.0f));
            event.set(CurrencyType.MONEY, amount);
        }
    }
}
