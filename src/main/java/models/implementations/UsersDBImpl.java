package models.implementations;

import models.Log;
import models.UsersDB;
import models.pojos.User;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class for bookkeeping all users that ever sent us a message
 * or we sent message to them.
 */
public class UsersDBImpl extends UsersDB {
    private final Map<String, User> usersByNicks = new LinkedHashMap<>();
    private final Map<String, User> usersByAdresses = new LinkedHashMap<>();
    private final Log log;

    public UsersDBImpl(Log log) {
        this.log = log;
    }

    public User getUser(String nick) {
        return usersByNicks.get(nick);
    }

    public void addUser(String nick, String adress) {
        if (usersByNicks.containsKey(nick)) {
            log.info("[UsersDBImpl] User already exists, returning");
            return;
        }

        // Possible situation, when we give a nick to user, connect to him
        // and then he responds with another nick. Then we just add record
        // with this another nick to the existing User instance.
        // We do not notify UI, because we haven't find another User, and
        // change to the UsersDBImpl model is strictly internal.
        if (usersByAdresses.containsKey(adress)) {
            log.info("[UsersDBImpl] User alias found, adding silently");
            User user = usersByAdresses.get(adress);
            usersByNicks.put(nick, user);
            return;
        }

        User user = new User(nick, adress);
        usersByNicks.put(nick, user);
        usersByAdresses.put(adress, user);

        log.info("[UsersDBImpl] User not found, adding and notifying");
        setChanged();
        notifyObservers(nick);
    }
}
