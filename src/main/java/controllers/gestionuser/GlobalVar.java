package controllers.gestionuser;

import entities.gestionuser.User;

public final class GlobalVar {
    private static User user;
    private GlobalVar() {
    }
    public static void setUser(User user) {
        GlobalVar.user = user;
    }
    public static User getUser() {
        return user;
    }
}
