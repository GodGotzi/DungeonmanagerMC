package at.gotzi.dungeonmanager.objects.pets;

public enum Pet {
    Chicken,
    Cow,
    Fox,
    Pig,
    Rabbit,
    Sheep,
    Zombie;

    public static Pet getPet(String pet) {
        switch(pet) {
            case "Chicken":
                return Chicken;
            case "Cow":
                return Cow;
            case "Fox":
                return Fox;
            case "Pig:":
                return Pig;
            case "Rabbit":
                return Rabbit;
            case "Sheep":
                return Sheep;
            case "Zombie":
                return Zombie;
            default:
                return null;
        }
    }
}
