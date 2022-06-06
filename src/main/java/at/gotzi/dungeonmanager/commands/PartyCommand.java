// This class was created by Wireless


package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.manager.PartyManager;
import at.gotzi.dungeonmanager.objects.party.Party;
import at.gotzi.dungeonmanager.objects.party.PartyMember;
import at.gotzi.dungeonmanager.objects.party.PartyRole;
import at.gotzi.dungeonmanager.utils.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PartyCommand implements CommandExecutor {

    private Main main;


    public PartyCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player player) {
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("create")) {
                    if(PartyManager.getParty(player) != null) {
                        player.sendMessage(Messages.alreadyInParty);
                        return false;
                    }
                    List<PartyMember> partyMembers = new ArrayList<>();
                    PartyMember partyMember = new PartyMember(player, PartyRole.LEADER);
                    partyMembers.add(partyMember);
                    Party party = new Party(partyMembers);
                    PartyManager.registerParty(party);
                    PartyManager.setLinkPlayerToParty(player, party);
                    player.sendMessage(Messages.createParty);
                } else if(args[0].equalsIgnoreCase("leave")) {
                    Party party = PartyManager.getParty(player);
                    if(party == null) {
                        player.sendMessage(Messages.noParty);
                        return false;
                    }
                    PartyMember member = null;
                    for (PartyMember partyMember : party.getMembers()) {
                        if(partyMember.getPlayer() == player) {
                            member = partyMember;
                            break;
                        }
                    }
                    if(party.getMembers().size() == 1) {
                        PartyManager.removeParty(party);
                        PartyManager.removeLink(player);
                        player.sendMessage(Messages.leftDelParty);
                    } else {
                        PartyManager.removeParty(party);
                        party.removeMember(member);
                        PartyManager.registerParty(party);
                        PartyManager.removeLink(player);
                        for (PartyMember partyMember : party.getMembers()) {
                            Player player1 = partyMember.getPlayer();
                            if(player1 != player) {
                                player1.sendMessage(Messages.pLeftParty.replace("%PLAYER%", player.getName()));
                            }
                        }
                        player.sendMessage(Messages.leftParty);
                    }
                } else if(args[0].equalsIgnoreCase("invite")) {
                    Party party = PartyManager.getParty(player);
                    if(party == null) {
                        player.sendMessage(Messages.noParty);
                        return false;
                    }
                    if(args.length == 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if(target == null) {
                            player.sendMessage(Messages.playerNotExist);
                            return false;
                        }
                        if(target == player) {
                            player.sendMessage(Messages.inviteSelf);
                            return false;
                        }
                        if(PartyManager.getParty(target) != null) {
                            player.sendMessage(Messages.pAlreadyInParty);
                            return false;
                        }
                        PartyMember member = null;
                        for (PartyMember partyMember : party.getMembers()) {
                            if(partyMember.getPlayer() == player) {
                                member = partyMember;
                                break;
                            }
                        }

                        if(member.getPartyRole() == PartyRole.LEADER) {
                            TextComponent msg = new TextComponent(Messages.inviteMsg.replace("%PLAYER%", player.getName()));
                            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept"));
                            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Messages.inviteHoverMsg)));
                            target.spigot().sendMessage(msg);
                            player.sendMessage(Messages.selfInviteMsg);
                            PartyManager.addInvite(target, party);
                        } else
                            player.sendMessage(Messages.notLeader);
                    } else
                        player.sendMessage(Messages.falseSyntaxCmd);
                } else if(args[0].equalsIgnoreCase("delete")) {
                    Party party = PartyManager.getParty(player);
                    if(party == null) {
                        player.sendMessage(Messages.falseSyntaxCmd);
                        return false;
                    }
                    for (PartyMember partyMember : party.getMembers()) {
                        Player players = partyMember.getPlayer();
                        PartyManager.removeLink(players);
                        players.sendMessage(Messages.delParty);
                    }
                    PartyManager.removeParty(party);
                } else if(args[0].equalsIgnoreCase("accept")) {
                    if(PartyManager.getParty(player) != null) {
                        player.sendMessage(Messages.alreadyInParty);
                    }
                    Party party = PartyManager.getInvite(player);
                    if(party == null) {
                        player.sendMessage(Messages.noInvites);
                        return false;
                    }
                    PartyMember partyMember = new PartyMember(player, PartyRole.MEMBER);
                    PartyManager.removeParty(party);
                    party.addMember(partyMember);
                    PartyManager.registerParty(party);
                    PartyManager.setLinkPlayerToParty(player, party);
                    for (PartyMember partyMember1 : party.getMembers()) {
                        Player player1 = partyMember1.getPlayer();
                        if(player1 != player) {
                            player1.sendMessage(Messages.joinParty.replace("%PLAYER%", player.getName()));
                        }
                    }
                    player.sendMessage(Messages.selfJoinParty);
                } else if(args[0].equalsIgnoreCase("kick")) {
                    Party party = PartyManager.getParty(player);
                    if(party == null) {
                        player.sendMessage(Messages.noParty);
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target == null) {
                        player.sendMessage(Messages.playerNotExist);
                        return false;
                    }
                    if(target == player) {
                        player.sendMessage(Messages.kickSelf);
                        return false;
                    }
                    if(PartyManager.getParty(target) != party) {
                        player.sendMessage(Messages.kickNotFound);
                        return false;
                    }
                    PartyMember member = null;
                    for (PartyMember partyMember : party.getMembers()) {
                        if(partyMember.getPlayer() == player) {
                            member = partyMember;
                            break;
                        }
                    }
                    PartyMember targetMember = null;
                    for (PartyMember partyMember : party.getMembers()) {
                        if(partyMember.getPlayer() == target) {
                            targetMember = partyMember;
                            break;
                        }
                    }
                    if(member.getPartyRole() == PartyRole.LEADER) {
                        PartyManager.removeParty(party);
                        PartyManager.removeLink(target);
                        party.removeMember(targetMember);
                        PartyManager.registerParty(party);
                        for (PartyMember partyMember : party.getMembers()) {
                            Player player1 = partyMember.getPlayer();
                            if(player1 != player && player1 != target) {
                                player1.sendMessage(Messages.kickParty.replace("%TARGET%", target.getName()).replace("%PLAYER%", player.getName()));
                            }
                        }
                        target.sendMessage(Messages.pKickParty);
                        player.sendMessage(Messages.selfKickParty.replace("%PLAYER%", target.getName()));
                    } else
                        player.sendMessage(Messages.notLeader);
                } else if(args[0].equalsIgnoreCase("list")) {
                    Party party = PartyManager.getParty(player);
                    if(party == null) {
                        player.sendMessage(Messages.noParty);
                        return false;
                    }
                    this.sendList(player, party.getMembers());
                } else if(args[0].equalsIgnoreCase("jump")) {
                    Party party = PartyManager.getParty(player);
                    if(party == null) {
                        player.sendMessage(Messages.noParty);
                        return false;
                    }
                    PartyMember member = null;
                    for (PartyMember partyMember : party.getMembers()) {
                        if(partyMember.getPlayer() == player) {
                            member = partyMember;
                            break;
                        }
                    }
                    if(member.getPartyRole() == PartyRole.LEADER) {
                        player.sendMessage(Messages.notLeader);
                        return false;
                    }
                    Location location = PartyManager.getJump(player);
                    if(location == null) {
                        player.sendMessage(Messages.noJumpLeader);
                        return false;
                    }
                    player.teleport(location);
                } else
                    player.sendMessage(Messages.falseSyntaxCmd);
            } else
                player.sendMessage(Messages.falseSyntaxCmd);
        } else
            sender.sendMessage(Messages.onlyPlayer);


        return false;
    }

    private void sendList(Player player, List<PartyMember> partyMembers) {
        for (String s : Messages.partyList) {
            if (s.contains("%PLAYERS%")) {
                for (PartyMember partyMember : partyMembers) {
                    player.sendMessage(s.replace("%PLAYERS%", partyMember.getPlayer().getName()));
                }
            } else {
                player.sendMessage(s);
            }
        }
    }

}
