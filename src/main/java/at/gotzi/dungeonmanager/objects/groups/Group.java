package at.gotzi.dungeonmanager.objects.groups;
// This class was created by Wireless


public enum Group {

    SQUIRE, //free
    // /tpa
    // /tpahere
    // 10% Job exp

    KNIGHT, //5000$
    // /near
    // +5% armor
    //

    ARCHER, //60000$
    // /craft bzw. /wb
    // Resistance 20%
    //

    SAMURAI, //200000$
    // /repairhand
    // +3 Armor + 10% resistance
    //

    MONK, //250000$
    // /exp withdraw
    // + 4 Armor
    // double health

    THIEF, //300000$
    // /stack
    // + 10% Movement
    //

    NINJA, //400000
    // speed increase 10%
    // mining speed increase 10%

    DRAGOON, //52000$
    // jump increase 1 block
    // jump increase 2 block
    //

    DANCER, //600000$
    // projectile protection + 20%
    // Regeneration
    //

    GEOMANCER, //800000$
    // never empty water bucket / lava bucket
    // Class only: Fire Resistance
    //

    DARK_KNIGHT, //2500000$
    // /fly (only overworld)
    // custom cosmetics
    // Class only: Armor +8 Health +8

    ONION_KNIGHT, //10000$
    // 3 /sethomes
    // custom cosmetics
    // Class only: sit on players head

    CHEMIST, //free
    // /tpa
    // /tpahere
    // Class only: 10% Money Boost

    WHITE_MAGE, //40000$
    // /heal
    // Class only: food +8
    //

    BLACK_MAGE, //100000$
    // /nightvision /nv
    // Class only: 25% chance to strike with upon hitting mob
    //

    MYSTIC, //225000$
    // /ptime
    // /pweather
    // Class Only: Frost Walk and Lava walk

    TIME_MAGE, //500000$
    // 10% Movement Speed
    // /back
    //

    MATHEMATICIAN, //700000$
    // /itemfiler
    // Class Only: 10% Money Job Experience Boost
    //

    ORATOR, //1000000$
    // silk touch spawners
    // Class Only: 20% Job Experience Boost
    //

    SUMMONER, //1400000$
    // allowed pet
    // Class Only: /telekenisis
    //

    BARD, //1600000$
    // /feed
    // Class only: 20% Money Boost
    //

    MIME; //2000000$
    // /fly (nether, end world only)
    // custom cosmetics
    // 20% Money and 25% Job Experience Boost

    public static Group getGroup(int nr) {
        switch (nr) {
            case 101:
                return ARCHER;

            case 102:
                return BARD;

            case 103:
                return BLACK_MAGE;

            case 104:
                return CHEMIST;

            case 105:
                return DANCER;

            case 106:
                return DARK_KNIGHT;

            case 107:
                return DRAGOON;

            case 108:
                return GEOMANCER;

            case 109:
                return KNIGHT;

            case 110:
                return MATHEMATICIAN;

            case 111:
                return MIME;

            case 112:
                return MONK;

            case 113:
                return MYSTIC;

            case 114:
                return NINJA;

            case 115:
                return ONION_KNIGHT;

            case 116:
                return ORATOR;

            case 117:
                return SAMURAI;

            case 118:
                return SQUIRE;

            case 119:
                return SUMMONER;

            case 120:
                return THIEF;

            case 121:
                return TIME_MAGE;

            case 122:
                return WHITE_MAGE;

            default:
                return null;
        }
    }

    public static Group getGroup(String groupName) {
        switch (groupName) {
            case "SQUIRE":
                return SQUIRE;

            case "KNIGHT":
                return KNIGHT;

            case "ARCHER":
                return ARCHER;

            case "SAMURAI":
                return SAMURAI;

            case "MONK":
                return MONK;

            case "THIEF":
                return THIEF;

            case "NINJA":
                return NINJA;

            case "DRAGOON":
                return DRAGOON;

            case "DANCER":
                return DANCER;

            case "GEOMANCER":
                return GEOMANCER;

            case "DARK_KNIGHT":
                return DARK_KNIGHT;

            case "ONION_KNIGHT":
                return ONION_KNIGHT;

            case "CHEMIST":
                return CHEMIST;

            case "WHITE_MAGE":
                return WHITE_MAGE;

            case "BLACK_MAGE":
                return BLACK_MAGE;

            case "MYSTIC":
                return MYSTIC;

            case "TIME_MAGE":
                return TIME_MAGE;

            case "MATHEMATICIAN":
                return MATHEMATICIAN;

            case "ORATOR":
                return ORATOR;

            case "SUMMONER":
                return SUMMONER;

            case "BARD":
                return BARD;

            case "MIME":
                return MIME;

            default:
                return null;
        }
    }
}
