package controllers;

import models.Chat;
import models.Log;
import models.pojos.User;

import java.util.Date;

public class ChatController {
    private static Chat chat = null;
    private static Log log = null;

    public static void setChat(Chat chat) {
        ChatController.chat = chat;
    }
    public static void setLog(Log log) { ChatController.log = log; }

    public static void addMessage(User author, String message) {
        log.info("[ChatController]: Adding message");
        chat.addMessage(author, new Date(), message);
    }
}
