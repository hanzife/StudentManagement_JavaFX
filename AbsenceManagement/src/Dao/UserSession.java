package Dao;

public class UserSession {

    private static UserSession instance;

    private static String userID = null;

    private UserSession(String userID) {
        this.userID = userID;
    }

    public static String getID() {
        return userID;
    }

    public static void setUserID(String userID) {
        UserSession.userID = userID;
    }

    public static void cleanUserSession() {
        userID = null;

    }
}