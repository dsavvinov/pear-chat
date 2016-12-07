package controllers;

import models.Log;
import models.pojos.User;
import net.Agent;

public class NetController {
    private static Agent agent = null;
    private static Log log = null;

    public static void setAgent(Agent agent) {
        NetController.agent = agent;
    }
    public static void setLog(Log log) { NetController.log = log; }

    public static boolean sendMessage(String nick, String message) {
        User user = UsersController.getUser(nick);
        if (user == null) {
            log.error("[NetController]: Error! User not found!");
            return false;
        }
        log.info("[NetController]: Sending message...");
        boolean result = agent.sendMessage(User.THIS_USER, user, message);

        return result;
    }
}
