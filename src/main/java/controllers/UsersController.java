package controllers;

import models.Log;
import models.pojos.User;
import models.UsersDB;

public class UsersController {
    private static UsersDB usersDB;
    private static Log log = null;

    public static void setUsersDB(UsersDB usersDB) {
        UsersController.usersDB = usersDB;
    }
    public static void setLog(Log log) { UsersController.log = log; }
    public static User getUser(String nick) {
        return usersDB.getUser(nick);
    }

    public static void addUser(String nick, String address) {
        log.info("[UsersController]: trying to add user <" + nick + "> from <" + address + ">");
        usersDB.addUser(nick, address);
    }
}
