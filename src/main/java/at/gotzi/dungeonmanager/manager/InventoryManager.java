// This class was created by Wireless


package at.gotzi.dungeonmanager.manager;

import java.util.HashMap;

public class InventoryManager {

    private static HashMap<Double, String> invs = new HashMap<>();
    //public static InventoryManager inventoryManager;

    //public InventoryManager() {

    //}

    public static void registerInv(double hashCode, String str) {
        invs.remove(hashCode);
        invs.put(hashCode, str);
    }


    public static void unregisterInv(double hashCode) {
        invs.remove(hashCode);
    }

    public static String getInv(double hashCode) {
        return invs.get(hashCode);
    }

    public static boolean isRegistered(double hashCode) {
        return invs.containsKey(hashCode);
    }
}