// This class was created by Wireless


package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.player.TeleportManager;
import at.gotzi.dungeonmanager.manager.world.ClaimManager;
import at.gotzi.dungeonmanager.objects.Loc;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ClaimCommand implements CommandExecutor {

    public ClaimCommand(Main main) {
    }

    private File messagesFile = new File("plugins//DungeonManager//messages.yml");
    private YamlConfiguration messages = YamlConfiguration.loadConfiguration(messagesFile);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            if(!player.getWorld().getName().equals("openworld")) {
                player.sendMessage(Messages.noClaimableWorld);
                return false;
            }
            if(args.length > 0) {
                ClaimManager claimManager = new ClaimManager(player);
                if (args[0].equalsIgnoreCase("claim")) {
                    UUID uuid = claimManager.getClaimOwner();
                    if(uuid != null) {
                        player.sendMessage(Messages.claimedBy.replace("%PLAYER%", Bukkit.getOfflinePlayer(uuid).getName()));
                        return false;
                    }

                    int x = player.getChunk().getX();
                    int z = player.getChunk().getZ();

                    Chunk chunk1 = null;
                    Chunk chunk2 = null;
                    Chunk chunk3 = null;

                    if (Utils.isEven(x) && Utils.isEven(z)) {
                        chunk1 = player.getWorld().getChunkAt(x+1, z);
                        chunk2 = player.getWorld().getChunkAt(x, z-1);
                        chunk3 = player.getWorld().getChunkAt(x+1, z-1);
                    } else if (Utils.isEven(x) && !Utils.isEven(z)) {
                        chunk1 = player.getWorld().getChunkAt(x, z+1);
                        chunk2 = player.getWorld().getChunkAt(x+1, z);
                        chunk3 = player.getWorld().getChunkAt(x+1, z+1);
                    } else if (!Utils.isEven(x) && Utils.isEven(z)) {
                        chunk1 = player.getWorld().getChunkAt(x-1, z);
                        chunk2 = player.getWorld().getChunkAt(x, z-1);
                        chunk3 = player.getWorld().getChunkAt(x-1, z-1);
                    } else if (!Utils.isEven(x) && !Utils.isEven(z)) {
                        chunk1 = player.getWorld().getChunkAt(x-1, z);
                        chunk2 = player.getWorld().getChunkAt(x, z+1);
                        chunk3 = player.getWorld().getChunkAt(x-1, z+1);
                    }

                    if (this.inSpawnRange(chunk1) || this.inSpawnRange(chunk2) || this.inSpawnRange(chunk3) || this.inSpawnRange(player.getChunk())) {
                        sender.sendMessage(Messages.claimOnSpawn);
                        return false;
                    }

                    if (PlayerData.getClaimsRAM(player.getUniqueId()) != null) {
                        int size = PlayerData.getClaimsRAM(player.getUniqueId()).size();
                        int minutes = PlayerData.getPlayTimeRAM(player.getUniqueId())[0];
                        int hours = PlayerData.getPlayTimeRAM(player.getUniqueId())[1];
                        int period = ClaimManager.getPeriod();
                        size = size/4;

                        if (size == ClaimManager.getMaxPlots()) {
                            player.sendMessage(Messages.maxClaims);
                            return false;
                        }

                        if (!(size < (int)(hours / period)) && size != 0) {
                            int left = (period - (hours - size * period)) - (period + 1);
                            if(minutes == 0) {
                                left++;
                                player.sendMessage(Messages.waitClaim.replace("%TIME%", left + " hours"));
                            } else {
                                int leftMin = 60 - minutes;
                                player.sendMessage(Messages.waitClaim.replace("%TIME%", left + " hours " + leftMin + " mins"));
                            }
                            return false;
                        }
                    }

                    if (PlayerData.getClaimHomeRAM(player.getUniqueId()) == null) {
                        PlayerData.setClaimHome(player.getUniqueId(), new Loc(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                    }

                    claimManager.claim(chunk1.getChunkKey());
                    claimManager.claim(chunk2.getChunkKey());
                    claimManager.claim(chunk3.getChunkKey());
                    claimManager.claim(player.getChunk().getChunkKey());

                    player.sendMessage(Messages.claim);
                } else if (args[0].equalsIgnoreCase("unclaim")) {
                    UUID uuid = claimManager.getClaimOwner();
                    if(uuid == null) {
                        player.sendMessage(Messages.noOwner);
                        return false;
                    }
                    if(!Objects.equals(uuid.toString(), player.getUniqueId().toString())) {
                        player.sendMessage(Messages.claimedBy.replace("%PLAYER%", Bukkit.getPlayer(uuid).getName()));
                        return false;
                    }

                    int x = player.getChunk().getX();
                    int z = player.getChunk().getZ();

                    Chunk chunk1 = null;
                    Chunk chunk2 = null;
                    Chunk chunk3 = null;

                    if (Utils.isEven(x) && Utils.isEven(z)) {
                        chunk1 = player.getWorld().getChunkAt(x+1, z);
                        chunk2 = player.getWorld().getChunkAt(x, z-1);
                        chunk3 = player.getWorld().getChunkAt(x+1, z-1);
                    } else if (Utils.isEven(x) && !Utils.isEven(z)) {
                        chunk1 = player.getWorld().getChunkAt(x, z+1);
                        chunk2 = player.getWorld().getChunkAt(x+1, z);
                        chunk3 = player.getWorld().getChunkAt(x+1, z+1);
                    } else if (!Utils.isEven(x) && Utils.isEven(z)) {
                        chunk1 = player.getWorld().getChunkAt(x-1, z);
                        chunk2 = player.getWorld().getChunkAt(x, z-1);
                        chunk3 = player.getWorld().getChunkAt(x-1, z-1);
                    } else if (!Utils.isEven(x) && !Utils.isEven(z)) {
                        chunk1 = player.getWorld().getChunkAt(x-1, z);
                        chunk2 = player.getWorld().getChunkAt(x, z+1);
                        chunk3 = player.getWorld().getChunkAt(x-1, z+1);
                    }

                    Loc loc = PlayerData.getClaimHomeRAM(uuid);
                    if (loc != null) {
                        Location location = new Location(Bukkit.getWorld("openworld"), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                        if (chunk1 == location.getChunk() || chunk2 == location.getChunk() || chunk3 == location.getChunk() || player.getChunk() == location.getChunk()) {
                            PlayerData.removeClaimHome(uuid);
                        }
                    }

                    claimManager.unClaim(chunk1.getChunkKey());
                    claimManager.unClaim(chunk2.getChunkKey());
                    claimManager.unClaim(chunk3.getChunkKey());
                    claimManager.unClaim(player.getChunk().getChunkKey());
                    player.sendMessage(Messages.unClaim);
                } else if (args[0].equalsIgnoreCase("info")) {
                    UUID uuid = claimManager.getClaimOwner();
                    if(uuid == null) {
                        player.sendMessage(Messages.noOwner);
                        return false;
                    }
                    this.sendInfoMessages(player, uuid);
                } else if (args[0].equalsIgnoreCase("kick")) {
                    if(args.length == 2) {
                        UUID uuid = claimManager.getClaimOwner();
                        if (uuid == null) {
                            player.sendMessage(Messages.noOwner);
                            return false;
                        }

                        if(player.getName().equals(args[1])) {
                            player.sendMessage(Messages.noKickSelf);
                            return false;
                        }

                        if (!Objects.equals(uuid.toString(), player.getUniqueId().toString())) {
                            player.sendMessage(Messages.notSelf);
                            return false;
                        }

                        UUID target = PlayerData.getUUID(args[1]);

                        if (target == null) {
                            player.sendMessage(Messages.playerNotExist);
                            return false;
                        }
                        Player targetPlayer = Bukkit.getPlayer(target);
                        if (targetPlayer == null) {
                            player.sendMessage(Messages.playerNotExist);
                            return false;
                        }

                        boolean bool = claimManager.kick(targetPlayer);
                        if(bool) {
                            targetPlayer.teleport(Locations.openWorldSpawn);
                            targetPlayer.sendMessage(Messages.pKick.replace("%PLAYER%", player.getName()));
                            player.sendMessage(Messages.selfKick.replace("%PLAYER%", args[1]));
                        } else
                            player.sendMessage(Messages.kickPNotOnGround);
                    }
                } else if (args[0].equalsIgnoreCase("ban")) {
                    if(args.length == 2) {
                        UUID uuid = claimManager.getClaimOwner();
                        if (uuid == null) {
                            player.sendMessage(Messages.noOwner);
                            return false;
                        }

                        if (!Objects.equals(uuid.toString(), player.getUniqueId().toString())) {
                            player.sendMessage(Messages.notSelf);
                            return false;
                        }

                        if(player.getName().equals(args[1])) {
                            player.sendMessage(Messages.noBanSelf);
                            return false;
                        }

                        UUID target = PlayerData.getUUID(args[1]);

                        if (target == null) {
                            player.sendMessage(Messages.playerNotExist);
                            return false;
                        }

                        Player targetPlayer = Bukkit.getPlayer(target);

                        if(PlayerData.getBannedRAM(uuid).contains(target.toString())) {
                            player.sendMessage(Messages.alreadyBanned);
                            return false;
                        }

                        PlayerData.addBanned(player.getUniqueId(), target);
                        boolean bool = claimManager.kick(targetPlayer);
                        if(targetPlayer != null) {
                            if (bool) {
                                targetPlayer.teleport(Locations.openWorldSpawn);
                            }
                            targetPlayer.sendMessage(Messages.pBanned.replace("%PLAYER%", player.getName()));
                        }
                        if(PlayerData.getTrustedRAM(uuid).contains(target.toString())) {
                            PlayerData.removeTrust(player.getUniqueId(), target);
                        }
                        player.sendMessage(Messages.selfBanned.replace("%PLAYER%", args[1]));
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("trust")) {

                    if(args.length == 2) {
                        UUID uuid = claimManager.getClaimOwner();
                        if (uuid == null) {
                            player.sendMessage(Messages.noOwner);
                            return false;
                        }

                        if (!Objects.equals(uuid.toString(), player.getUniqueId().toString())) {
                            player.sendMessage(Messages.notSelf);
                            return false;
                        }

                        if(player.getName().equals(args[1])) {
                            player.sendMessage(Messages.noTrustSelf);
                            return false;
                        }

                        UUID target = PlayerData.getUUID(args[1]);

                        if (target == null) {
                            player.sendMessage(Messages.playerNotExist);
                            return false;
                        }

                        if(PlayerData.getTrustedRAM(uuid).contains(target.toString())) {
                            player.sendMessage(Messages.alreadyTrusted);
                            return false;
                        }

                        Player targetPlayer = Bukkit.getPlayer(target);

                        PlayerData.addTrusted(player.getUniqueId(), target);
                        if (targetPlayer != null) {
                            targetPlayer.sendMessage(Messages.pTrusted.replace("%PLAYER%", player.getName())); //player.getName()
                        }
                        player.sendMessage(Messages.selfTrusted.replace("%PLAYER%", args[1]));
                        if(PlayerData.getBannedRAM(uuid).contains(target.toString())) {
                            PlayerData.removeBanned(uuid, target);
                        }
                    }
                } else if (args[0].equalsIgnoreCase("untrust")) {
                    if(args.length == 2) {
                        UUID uuid = claimManager.getClaimOwner();
                        if (uuid == null) {
                            player.sendMessage(Messages.noOwner);
                            return false;
                        }

                        if (!Objects.equals(uuid.toString(), player.getUniqueId().toString())) {
                            player.sendMessage(Messages.notSelf);
                            return false;
                        }

                        UUID target = PlayerData.getUUID(args[1]);

                        if (target == null) {
                            player.sendMessage(Messages.playerNotExist);
                            return false;
                        }

                        if(!PlayerData.getTrustedRAM(uuid).contains(target.toString())) {
                            player.sendMessage(Messages.notTrusted);
                            return false;
                        }

                        Player targetPlayer = Bukkit.getPlayer(target);
                        PlayerData.removeTrust(player.getUniqueId(), target);
                        if (targetPlayer != null) {
                            targetPlayer.sendMessage(Messages.pUnTrust.replace("%PLAYER%", player.getName()));
                        }
                        player.sendMessage(Messages.pUnTrust.replace("%PLAYER%", args[1]));
                    }
                } else if (args[0].equalsIgnoreCase("unban")) {
                    if(args.length == 2) {
                        UUID uuid = claimManager.getClaimOwner();
                        if (uuid == null) {
                            player.sendMessage(Messages.noOwner);
                            return false;
                        }

                        if (!Objects.equals(uuid.toString(), player.getUniqueId().toString())) {
                            player.sendMessage(Messages.notSelf);
                            return false;
                        }

                        UUID target = PlayerData.getUUID(args[1]);

                        if (target == null) {
                            player.sendMessage(Messages.playerNotExist);
                            return false;
                        }

                        if(!PlayerData.getBannedRAM(uuid).contains(target.toString())) {
                            player.sendMessage(Messages.notBanned);
                            return false;
                        }

                        Player targetPlayer = Bukkit.getPlayer(target);

                        PlayerData.removeBanned(player.getUniqueId(), target);
                        if(targetPlayer != null) {
                            targetPlayer.sendMessage(Messages.pBanned.replace("%PLAYER%", player.getName()));
                        }
                        player.sendMessage(Messages.selfBanned.replace("%PLAYER%", args[1]));
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("home")) {
                    if (args.length == 1) {
                        if (PlayerData.getClaimHomeRAM(player.getUniqueId()) != null) {
                            Loc loc = PlayerData.getClaimHomeRAM(player.getUniqueId());
                            TeleportManager.registerTeleport(player, new Location(Bukkit.getWorld("openworld"), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
                        } else
                            sender.sendMessage(Messages.noClaimHome);
                    } else if (args.length == 2) {
                        UUID uuid = PlayerData.getUUID(args[1]);
                        if (uuid == null) {
                            sender.sendMessage(Messages.playerNotExist);
                            return false;
                        }
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                        if (offlinePlayer == null) {
                            sender.sendMessage(Messages.playerNotExist);
                            return false;
                        }
                        if (PlayerData.getBannedRAM(uuid).contains(player.getUniqueId().toString())) {
                            sender.sendMessage(Messages.banned);
                            return false;
                        }
                        Loc loc = PlayerData.getClaimHomeRAM(uuid);
                        if (loc == null) {
                            sender.sendMessage(Messages.pNoClaimHome);
                            return false;
                        }
                        TeleportManager.registerTeleport(player, new Location(Bukkit.getWorld("openworld"), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
                    } else
                        sender.sendMessage(Messages.falseSyntaxCmd);
                } else if (args[0].equalsIgnoreCase("sethome")) {
                    UUID uuid = claimManager.getClaimOwner();
                    if (uuid == null) {
                        player.sendMessage(Messages.noOwner);
                        return false;
                    }

                    if (!Objects.equals(uuid.toString(), player.getUniqueId().toString())) {
                        player.sendMessage(Messages.notSelf);
                        return false;
                    }

                    PlayerData.setClaimHome(player.getUniqueId(), new Loc(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                    sender.sendMessage(Messages.claimHomeSet);
                } else
                    sender.sendMessage(Messages.falseSyntaxCmd);
            } else
                player.sendMessage(Messages.falseSyntaxCmd);
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    private void sendInfoMessages(Player player, UUID uuid) {
        StringBuilder banned = new StringBuilder();
        StringBuilder trusted = new StringBuilder();


        for (String userB : PlayerData.getBannedRAM(uuid)) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(userB));
            banned.append(offlinePlayer.getName()).append(", ");
        }

        for (String userT : PlayerData.getTrustedRAM(uuid)) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(userT));
            trusted.append(offlinePlayer.getName()).append(", ");
        }

        for (String line : Messages.infoList) {
            player.sendMessage(line
                    .replace("%CORDS%", Messages.chunkCords.replace("%X%", String.valueOf(player.getChunk().getX())).replace("%Z%", String.valueOf(player.getChunk().getZ())))
                    .replace("%BANNED%", banned.toString())
                    .replace("%TRUSTED%", trusted.toString())
                    .replace("%OWNER%", Bukkit.getOfflinePlayer(uuid).getName()));
        }
    }

    private boolean inSpawnRange(Chunk chunk) {
        Chunk spawn = Locations.openWorldSpawn.getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        return Math.abs(spawn.getX() - x) <= (ClaimManager.spawnRange / 16) && Math.abs(spawn.getZ() - z) <= (ClaimManager.spawnRange / 16);
    }

}
