package utils;

import models.Admin;
import models.Guest;
import models.Receptionist;

public class SessionData {
    public static Guest currentGuest;
    public static Admin currentAdmin;
    public static Receptionist currentReceptionist;

    public static void clearSession() {
        currentGuest = null;
        currentAdmin = null;
        currentReceptionist = null;
    }
}