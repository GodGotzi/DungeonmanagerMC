package at.gotzi.dungeonmanager.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static String noPermission;
    public static String falseSyntaxCmd;
    public static String onlyPlayer;
    public static String setCmd;
    public static String noBlockTargeting;
    public static String playerNotExist;

    public static String deathMsg;
    public static String joinMsg;
    public static String leaveMsg;
    public static String locationNotExist;

    public static String alreadyInParty;
    public static String createParty;
    public static String noParty;
    public static String leftDelParty;
    public static String pLeftParty;
    public static String leftParty;
    public static String inviteSelf;
    public static String pAlreadyInParty;
    public static String inviteMsg;
    public static String inviteHoverMsg;
    public static String selfInviteMsg;
    public static String notLeader;
    public static String delParty;
    public static String noInvites;
    public static String joinParty;
    public static String selfJoinParty;
    public static String kickSelf;
    public static String kickParty;
    public static String selfKickParty;
    public static String pKickParty;
    public static List<String> partyList;
    public static String noJumpLeader;
    public static String jumpLeaderMsg;
    public static String askJumpLeader;
    public static String jumpHoverMsg;

    public static String leaveHome;
    public static String leavePlot;
    public static String enterHome;
    public static String enterPlot;
    public static String waitClaim;
    public static String noClaimableWorld;
    public static String chunkCords;
    public static String alreadyClaimed;
    public static String claim;
    public static String unClaim;
    public static String noOwner;
    public static String notSelf;
    public static String claimedBy;
    public static String pKick;
    public static String selfKick;
    public static String kickPNotOnGround;
    public static String pBanned;
    public static String alreadyBanned;
    public static String selfBanned;
    public static String pTrusted;
    public static String alreadyTrusted;
    public static String selfTrusted;
    public static String pUnTrust;
    public static String notTrusted;
    public static String selfUnTrust;
    public static String pUnBanned;
    public static String notBanned;
    public static String selfUnBanned;
    public static List<String> infoList;
    public static String noTrustSelf;
    public static String noBanSelf;
    public static String noKickSelf;
    public static String claimOnSpawn;
    public static String cantUseWildCmd;

    public static String tooMuchOresMined;
    public static String tooMuchItemsInChunk;

    public static String noClaimHome;
    public static String pNoClaimHome;
    public static String claimHomeSet;

    public static String maxClaims;

    public static String restricted;
    public static String banned;

    public static String groupEnd;

    public static String pTpaRequestMsg;
    public static String pTpahereRequestMsg;
    public static String selfTpaRequestMsg;
    public static String selfTpahereRequestMsg;
    public static String hoverTpaMsg;
    public static String hoverTpahereMsg;
    public static String pTpaAccept;
    public static String pTpahereAccept;
    public static String selfTpaAccept;
    public static String selfTpahereAccept;
    public static String coolDownTpa;
    
    public static String needGroup;

    public static String kickNotFound;

    public static String nearMsg;
    public static String nearNoEntity;

    public static String sitPlayerHeadNotRange;

    public static String repairMsg;
    public static String notRepairable;

    public static String expWithDrawNoLevel;
    public static String expWithDrawMsg;

    public static String healMsg;
    public static String healCoolDown;

    public static String flyEnabled;
    public static String flyDisabled;
    public static String flyNotAllowed;

    public static String backMsg;
    public static String noBackMsg;

    public static String feedMsg;

    public static String nvMsg;

    public static String pTimeMsg;

    public static String pWeatherMsg;

    public static String teleOffMsg;
    public static String teleOnMsg;

    public static String itemFilterNoMaterial;
    public static String itemFilterNoMaterials;
    public static String itemFilterMaterialExists;
    public static String itemFilterMaxAmount;
    public static String itemFilterRemove;
    public static String itemFilterAdd;
    public static String itemFilterMenu;

    public static String homeNotExist;
    public static String homeAlreadyExist;
    public static String reachedMaxHomes;
    public static String homeRemove;
    public static String homeAdd;

    public static String noPetAllowedMsg;
    public static String petSummoned;
    public static String petRemove;

    public static String ecoNotEnough;
    public static String ecoBuy;
    public static String ecoGet;

    public static String invFull;
    public static String noItem;

    public static String inProtectionArea;

    public static String teleportCount;
    public static String teleportMsg;
    public static String teleportCancel;

    public static String noDungeonWorld;
    public static String huntAdd;
    public static String huntRemove;
    public static String notHunted;
    public static String playerNotInWorld;

    public static final String operationComplete = "§1Operation complete";

    private final File file = new File("plugins//DungeonManager//messages.yml");
    private final YamlConfiguration messagesConfig = YamlConfiguration.loadConfiguration(file);

    public Messages() {
        this.basic();
        this.party();
        this.claim();
        this.claim2();
        this.groupCommands();
        this.group();
        this.eco();
        this.shop();
        this.resource();
    }

    public void basic() {
        noPermission = Utils.color(messagesConfig.getString("basic." + "noPerm"));
        falseSyntaxCmd = Utils.color(messagesConfig.getString("basic." + "falseSyntaxCmd"));
        onlyPlayer = Utils.color(messagesConfig.getString("basic." + "onlyPlayer"));
        setCmd = Utils.color(messagesConfig.getString("basic." + "setSomethingMsg"));
        noBlockTargeting = Utils.color(messagesConfig.getString("basic." + "noBlockTargeting"));
        playerNotExist = Utils.color(messagesConfig.getString("basic." + "playerNotExist"));

        teleportCount = Utils.color(messagesConfig.getString("basic." + "teleportCount"));
        teleportMsg = Utils.color(messagesConfig.getString("basic." + "teleportMsg"));
        teleportCancel = Utils.color(messagesConfig.getString("basic." + "teleportCancel"));
        restricted = Utils.color(messagesConfig.getString("basic." + "restricted"));

        deathMsg = Utils.color(messagesConfig.getString("basic." + "deathMsg"));
        joinMsg = Utils.color(messagesConfig.getString("basic." + "joinMsg"));
        leaveMsg = Utils.color(messagesConfig.getString("basic." + "leaveMsg"));

        noDungeonWorld = Utils.color(messagesConfig.getString("basic." + "noDungeonWorld"));
        tooMuchOresMined = Utils.color(messagesConfig.getString("basic." + "tooMuchOresMined"));
        tooMuchItemsInChunk = Utils.color(messagesConfig.getString("basic." + "tooMuchItemsInChunk"));

        this.registerNewMessage("basic.", "cantUseWildCmd", "&6You cannot use &7/wild&6 or &7/rtp&6 in this world!");
        this.registerNewMessage("basic", "locationNotExist", "&6Location does not exist anymore!");
        this.registerNewMessage("hunterFish", "huntAdd", "&6You are getting hunted by &7Hunterfish&6 watch out!");
        this.registerNewMessage("hunterFish", "huntRemove", "&7Hunterfish&6 canceled hunt for you!");
        this.registerNewMessage("hunterFish", "notHunted", "&6You are not getting hunted right now!");
        this.registerNewMessage("hunterFish", "playerNotInWorld", "&6Player must be in Openworld!");
    }

    public void party() {
        alreadyInParty = Utils.color(messagesConfig.getString("party." + "alreadyInParty"));
        createParty = Utils.color(messagesConfig.getString("party." + "createParty"));
        noParty = Utils.color(messagesConfig.getString("party." + "noParty"));
        leftDelParty = Utils.color(messagesConfig.getString("party." + "leftDelParty"));
        pLeftParty = Utils.color(messagesConfig.getString("party." + "pLeftParty"));
        leftParty = Utils.color(messagesConfig.getString("party." + "leftParty"));
        inviteSelf = Utils.color(messagesConfig.getString("party." + "inviteSelf"));
        pAlreadyInParty = Utils.color(messagesConfig.getString("party." + "pAlreadyInParty"));
        inviteMsg = Utils.color(messagesConfig.getString("party." + "inviteMsg"));
        inviteHoverMsg = Utils.color(messagesConfig.getString("party." + "inviteHoverMsg"));
        selfInviteMsg = Utils.color(messagesConfig.getString("party." + "selfInviteMsg"));
        notLeader = Utils.color(messagesConfig.getString("party." + "notLeader"));
        delParty = Utils.color(messagesConfig.getString("party." + "delParty"));
        noInvites = Utils.color(messagesConfig.getString("party." + "noInvites"));
        joinParty = Utils.color(messagesConfig.getString("party." + "joinParty"));
        selfJoinParty = Utils.color(messagesConfig.getString("party." + "selfJoinParty"));
        inviteSelf = Utils.color(messagesConfig.getString("party." + "inviteSelf"));
        pAlreadyInParty = Utils.color(messagesConfig.getString("party." + "pAlreadyInParty"));
        inviteMsg = Utils.color(messagesConfig.getString("party." + "inviteMsg"));
        inviteHoverMsg = Utils.color(messagesConfig.getString("party." + "inviteHoverMsg"));
        kickSelf = Utils.color(messagesConfig.getString("party." + "kickSelf"));
        kickParty = Utils.color(messagesConfig.getString("party." + "kickParty"));
        selfKickParty = Utils.color(messagesConfig.getString("party." + "selfKickParty"));
        pKickParty = Utils.color(messagesConfig.getString("party." + "pKickParty"));
        List<String> partyStringList1 = messagesConfig.getStringList("party." + ".partyList");
        List<String> partyStringList2 = new ArrayList<>();
        for (String line : partyStringList1) {
            partyStringList2.add(Utils.color(line));
        }
        partyList = partyStringList2;
        noJumpLeader = Utils.color(messagesConfig.getString("party." + "noJumpLeader"));
        jumpLeaderMsg = Utils.color(messagesConfig.getString("party." + "jumpLeaderMsg"));
        askJumpLeader = Utils.color(messagesConfig.getString("party." + "askJumpLeader"));
        jumpHoverMsg = Utils.color(messagesConfig.getString("party." + "jumpHoverMsg"));
        kickNotFound = Utils.color(messagesConfig.getString("party." + "kickNotFound"));
    }

    public void claim() {
        leavePlot = Utils.color(messagesConfig.getString("claim." + "leavePlot"));
        leaveHome = Utils.color(messagesConfig.getString("claim." + "leaveHome"));
        enterHome = Utils.color(messagesConfig.getString("claim." + "enterHome"));
        enterPlot = Utils.color(messagesConfig.getString("claim." + "enterPlot"));
        waitClaim = Utils.color(messagesConfig.getString("claim." + "waitClaim"));
        noClaimableWorld = Utils.color(messagesConfig.getString("claim." + "noClaimableWorld"));
        chunkCords = Utils.color(messagesConfig.getString("claim." + "chunkCords"));
        alreadyClaimed = Utils.color(messagesConfig.getString("claim." + "alreadyClaimed"));
        claim = Utils.color(messagesConfig.getString("claim." + "claim"));
        unClaim = Utils.color(messagesConfig.getString("claim." + "unClaim"));
        noOwner = Utils.color(messagesConfig.getString("claim." + "noOwner"));
        notSelf = Utils.color(messagesConfig.getString("claim." + "notSelf"));
        claimedBy = Utils.color(messagesConfig.getString("claim." + "claimedBy"));
        pKick = Utils.color(messagesConfig.getString("claim." + "pKick"));
        selfKick = Utils.color(messagesConfig.getString("claim." + "selfKick"));
        kickPNotOnGround = Utils.color(messagesConfig.getString("claim." + "kickPNotOnGround"));
        pBanned = Utils.color(messagesConfig.getString("claim." + "pBanned"));
        alreadyBanned = Utils.color(messagesConfig.getString("claim." + "alreadyBanned"));
        selfBanned = Utils.color(messagesConfig.getString("claim." + "selfBanned"));
        pTrusted = Utils.color(messagesConfig.getString("claim." + "pTrusted"));
        alreadyTrusted = Utils.color(messagesConfig.getString("claim." + "alreadyTrusted"));
        selfTrusted = Utils.color(messagesConfig.getString("claim." + "selfTrusted"));
        pUnTrust = Utils.color(messagesConfig.getString("claim." + "pUnTrust"));
        notTrusted = Utils.color(messagesConfig.getString("claim." + "notTrusted"));
        selfUnTrust = Utils.color(messagesConfig.getString("claim." + "selfUnTrust"));
        pUnBanned = Utils.color(messagesConfig.getString("claim." + "pUnBanned"));
        notBanned = Utils.color(messagesConfig.getString("claim." + "notBanned"));
        selfUnBanned = Utils.color(messagesConfig.getString("claim." + "selfUnBanned"));
        List<String> infoList1 = messagesConfig.getStringList("claim." + "infoList");
        List<String> infoList2 = new ArrayList<>();
        for (String line : infoList1) {
            infoList2.add(Utils.color(line));
        }
        infoList = infoList2;
        noTrustSelf = Utils.color(messagesConfig.getString("claim." + "noTrustSelf"));
        noBanSelf = Utils.color(messagesConfig.getString("claim." + "noBanSelf"));
        noKickSelf = Utils.color(messagesConfig.getString("claim." + "noKickSelf"));
        maxClaims = Utils.color(messagesConfig.getString("claim." + "maxClaims"));
        noClaimHome = Utils.color(messagesConfig.getString("claim." + "noClaimHome"));
        pNoClaimHome = Utils.color(messagesConfig.getString("claim." + "pNoClaimHome"));
        claimHomeSet = Utils.color(messagesConfig.getString("claim." + "claimHomeSet"));
        claimOnSpawn = Utils.color(messagesConfig.getString("claim." + "claimOnSpawn"));
    }

    public void claim2() {
        banned = Utils.color(messagesConfig.getString("claim2." + "banned"));
    }

    public void group() {
        groupEnd = Utils.color(messagesConfig.getString("class." + "classEnd"));
    }

    public void groupCommands()  {

        needGroup = Utils.color(messagesConfig.getString("class." + "needClass"));

        pTpaRequestMsg = Utils.color(messagesConfig.getString("class.commands.tpa." + "pTpaRequestMsg"));
        pTpahereRequestMsg = Utils.color(messagesConfig.getString("class.commands.tpa." + "pTpahereRequestMsg"));
        selfTpaRequestMsg = Utils.color(messagesConfig.getString("class.commands.tpa." + "selfTpaRequestMsg"));
        selfTpahereRequestMsg = Utils.color(messagesConfig.getString("class.commands.tpa." + "selfTpahereRequestMsg"));
        hoverTpaMsg = Utils.color(messagesConfig.getString("class.commands.tpa." + "hoverTpaMsg"));
        hoverTpahereMsg = Utils.color(messagesConfig.getString("class.commands.tpa." + "hoverTpahereMsg"));
        pTpaAccept = Utils.color(messagesConfig.getString("class.commands.tpa." + "pTpaAccept"));
        pTpahereAccept = Utils.color(messagesConfig.getString("class.commands.tpa." + "pTpahereAccept"));
        selfTpaAccept = Utils.color(messagesConfig.getString("class.commands.tpa." + "selfTpaAccept"));
        selfTpahereAccept = Utils.color(messagesConfig.getString("class.commands.tpa." + "selfTpahereAccept"));
        coolDownTpa = Utils.color(messagesConfig.getString("class.commands.tpa." + "coolDownTpa"));

        flyEnabled = Utils.color(messagesConfig.getString("class.commands.fly." + "flyEnabled"));
        flyDisabled = Utils.color(messagesConfig.getString("class.commands.fly." + "flyDisabled"));
        flyNotAllowed = Utils.color(messagesConfig.getString("class.commands.fly." + "flyNotAllowed"));

        nearMsg = Utils.color(messagesConfig.getString("class.commands.near." + "nearMsg"));
        nearNoEntity = Utils.color(messagesConfig.getString("class.commands.near." + "nearNoEntity"));

        repairMsg = Utils.color(messagesConfig.getString("class.commands.repairhand." + "repairMsg"));
        notRepairable = Utils.color(messagesConfig.getString("class.commands.repairhand." + "notRepairable"));

        expWithDrawNoLevel = Utils.color(messagesConfig.getString("class.commands.expwithdraw." + "expWithDrawNoLevel"));
        expWithDrawMsg = Utils.color(messagesConfig.getString("class.commands.expwithdraw." + "expWithDrawMsg"));

        healMsg = Utils.color(messagesConfig.getString("class.commands.heal." + "healMsg"));
        healCoolDown = Utils.color(messagesConfig.getString("class.commands.heal." + "healCoolDown"));
//
        backMsg = Utils.color(messagesConfig.getString("class.commands.back." + "backMsg"));
        noBackMsg = Utils.color(messagesConfig.getString("class.commands.back." + "noBackMsg"));

        feedMsg = Utils.color(messagesConfig.getString("class.commands.feed." + "feedMsg"));

        nvMsg = Utils.color(messagesConfig.getString("class.commands.nv." + "nvMsg"));

        pTimeMsg = Utils.color(messagesConfig.getString("class.commands.ptime." + "pTimeMsg"));

        pWeatherMsg = Utils.color(messagesConfig.getString("class.commands.pweather." + "pWeatherMsg"));

        teleOffMsg = Utils.color(messagesConfig.getString("class.commands.tele." + "teleOffMsg"));
        teleOnMsg = Utils.color(messagesConfig.getString("class.commands.tele." + "teleOnMsg"));

        itemFilterNoMaterial = Utils.color(messagesConfig.getString("class.commands.itemFilter." + "itemFilterNoMaterial"));
        itemFilterNoMaterials = Utils.color(messagesConfig.getString("class.commands.itemFilter." + "itemFilterNoMaterials"));
        itemFilterMaterialExists = Utils.color(messagesConfig.getString("class.commands.itemFilter." + "itemFilterMaterialExists"));
        itemFilterMaxAmount = Utils.color(messagesConfig.getString("class.commands.itemFilter." + "itemFilterMaxAmount"));
        itemFilterRemove = Utils.color(messagesConfig.getString("class.commands.itemFilter." + "itemFilterRemove"));
        itemFilterAdd = Utils.color(messagesConfig.getString("class.commands.itemFilter." + "itemFilterAdd"));
        itemFilterMenu = Utils.color(messagesConfig.getString("class.commands.itemFilter." + "itemFilterMenu"));

        noPetAllowedMsg = Utils.color(messagesConfig.getString("class.commands.pet." + "noPetAllowedMsg"));
        petSummoned = Utils.color(messagesConfig.getString("class.commands.pet." + "petSummoned"));
        petRemove = Utils.color(messagesConfig.getString("class.commands.pet." + "petRemove"));

        homeNotExist = Utils.color(messagesConfig.getString("class.commands.home." + "homeNotExist"));
        homeAlreadyExist = Utils.color(messagesConfig.getString("class.commands.home." + "homeAlreadyExist"));
        reachedMaxHomes = Utils.color(messagesConfig.getString("class.commands.home." + "reachedMaxHomes"));
        homeRemove = Utils.color(messagesConfig.getString("class.commands.home." + "homeRemove"));
        homeAdd = Utils.color(messagesConfig.getString("class.commands.home." + "homeAdd"));

        sitPlayerHeadNotRange = Utils.color(messagesConfig.getString("class.commands.sitPlayerHead." + "sitPlayerHeadNotRange"));
    }

    public void eco() {
        ecoNotEnough = Utils.color(messagesConfig.getString("eco." + "ecoNotEnough"));
        ecoBuy = Utils.color(messagesConfig.getString("eco." + "ecoBuy"));
        ecoGet = Utils.color(messagesConfig.getString("eco." + "ecoGet"));
    }

    public void shop() {
        invFull = Utils.color(messagesConfig.getString("shop." + "invFull"));
        noItem = Utils.color(messagesConfig.getString("shop." + "noItem"));
    }

    public void resource() {
        inProtectionArea = Utils.color(messagesConfig.getString("resourceWorlds." + "inProtectionArea"));
    }

    public void registerNewMessage(String section, String msg, String pre) {
        String msgConfig= messagesConfig.getString(section + "." + msg);
        if (msgConfig == null) {
            messagesConfig.set(section + "." + msg, pre);
            try {
                messagesConfig.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Field f = Messages.class.getDeclaredField(msg);
            f.setAccessible(true);
            f.set(null, Utils.color(messagesConfig.getString(section + "." + msg)));
            f.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
