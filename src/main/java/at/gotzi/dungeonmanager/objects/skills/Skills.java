package at.gotzi.dungeonmanager.objects.skills;

public enum Skills {

    ARMORPLUS(true, SkillType.Effect, true),
    BACKCMD(false, SkillType.Command, false),
    CRAFTCMD(false, SkillType.Command, false),
    EXPWITHDRAWCMD(false, SkillType.Command, false),
    FEEDCMD(false, SkillType.Command, false),
    FIRERESISTANCE(true, SkillType.PotionEffect, true),
    FLYCMDNETHEREND(false, SkillType.Command, false),
    HUNGERBOOST(true, SkillType.Effect, false),
    FLYCMDOVERWORLD(false, SkillType.Command, false),
    FROSTWALK(false, SkillType.Effect, false),
    HEALCMD(false, SkillType.Command, false),
    HEALTHPLUS(true, SkillType.Effect, true),
    ITEMFILTERCMD(false, SkillType.Command, false),
    JOBEXPBOOST(true, SkillType.Boost, false),
    JUMPBOOST(true, SkillType.PotionEffect, true),
    LAVAWALK(false, SkillType.Effect, false),
    MININGSPEEDBOOST(true, SkillType.Boost, true),
    MONEYBOOST(true, SkillType.Boost, false),
    MOVEMENTBOOST(true, SkillType.Boost, true),
    NEARCMD(true, SkillType.Command, false),
    NEVEREMPTYBUCKET(false, SkillType.NoNeed, false),
    NIGHTVISIONCMD(false, SkillType.Command, false),
    PET(false, SkillType.Effect, false),
    PROJECTILEPROT(true, SkillType.Effect, false),
    PTIMECMD(false, SkillType.Command, false),
    PWEATHERCMD(false, SkillType.Command, false),
    REGENERATION(true, SkillType.PotionEffect, true),
    REPAIRHANDCMD(false, SkillType.Command, false),
    RESISTANCE(true, SkillType.PotionEffect, true),
    HOMECMD(true, SkillType.Command, false),
    SILKTOUCHSPAWNERS(false, SkillType.Effect, false),
    SITONPLAYERHEADS(false, SkillType.Effect, false),
    STACKCMD(false, SkillType.Command, false),
    TELEKINESIS(false, SkillType.Effect, false),
    TPACMD(false, SkillType.Command, false),
    TPAHERECMD(false, SkillType.Command, false),
    CHANCELIGHTNING(true, SkillType.Effect, false);

    private final boolean bool;
    private final boolean needScheduler;
    private final SkillType skillType;

    Skills(boolean b, SkillType skillType, boolean needScheduler) {
        this.bool = b;
        this.skillType = skillType;
        this.needScheduler = needScheduler;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public boolean isNeedScheduler() {
        return needScheduler;
    }

    public boolean isBool() {
        return bool;
    }

    public static Skills getSkill(String skillName) {
        switch (skillName) {
            case "ARMORPLUS":
                    return ARMORPLUS;
            case "BACKCMD":
                    return BACKCMD;
            case "CRAFTCMD":
                    return CRAFTCMD;
            case "EXPWITHDRAWCMD":
                    return EXPWITHDRAWCMD;
            case "FEEDCMD":
                    return FEEDCMD;
            case "FIRERESISTANCE":
                    return FIRERESISTANCE;
            case "FLYCMDNETHEREND":
                    return FLYCMDNETHEREND;
            case "FLYCMDOVERWORLD":
                    return FLYCMDOVERWORLD;
            case "FROSTWALK":
                    return FROSTWALK;
            case "HEALCMD":
                    return HEALCMD;
            case "HEALTHPLUS":
                    return HEALTHPLUS;
            case "ITEMFILTERCMD":
                    return ITEMFILTERCMD;
            case "JOBEXPBOOST":
                    return JOBEXPBOOST;
            case "JUMPBOOST":
                    return JUMPBOOST;
            case "LAVAWALK":
                    return LAVAWALK;
            case "MININGSPEEDBOOST":
                    return MININGSPEEDBOOST;
            case "MONEYBOOST":
                    return MONEYBOOST;
            case "MOVEMENTBOOST":
                    return MOVEMENTBOOST;
            case "NEARCMD":
                    return NEARCMD;
            case "NEVEREMPTYBUCKET":
                    return NEVEREMPTYBUCKET;
            case "NIGHTVISIONCMD":
                    return NIGHTVISIONCMD;
            case "PET":
                    return PET;
            case "PROJECTILEPROT":
                    return PROJECTILEPROT;
            case "PTIMECMD":
                    return PTIMECMD;
            case "PWEATHERCMD":
                    return PWEATHERCMD;
            case "REGENERATION":
                    return REGENERATION;
            case "REPAIRHANDCMD":
                    return REPAIRHANDCMD;
            case "RESISTANCE":
                    return RESISTANCE;
            case "HOMECMD":
                    return HOMECMD;
            case "SILKTOUCHSPAWNERS":
                    return SILKTOUCHSPAWNERS;
            case "SITONPLAYERHEADS":
                    return SITONPLAYERHEADS;
            case "STACKCMD":
                return STACKCMD;
            case "TELEKINESIS":
                    return TELEKINESIS;
            case "TPACMD":
                    return TPACMD;
            case "TPAHERECMD":
                    return TPAHERECMD;
            case "CHANCELIGHTNING":
                return CHANCELIGHTNING;
            case "HUNGERBOOST":
                return HUNGERBOOST;
            default:
                return null;
        }
    }
}
