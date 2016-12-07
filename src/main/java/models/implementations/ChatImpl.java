package models.implementations;

import models.Chat;
import models.Log;
import models.pojos.Message;
import models.pojos.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatImpl extends Chat {
    private final List<Message> messages = new ArrayList<>();
    private final Log log;

    public ChatImpl(Log log) {
        this.log = log;
    }

    @Override
    public List<Message> getMessages() {
        log.info("[Chat]: Returning history");
        return messages;
    }

    @Override
    public void addMessage(User from, Date date, String text) {
        log.info("[Chat]: Adding message from <" + from.name + "> on " + date.toString());
        Message msg = new Message(from, date, text);
        messages.add(msg);
        setChanged();
        notifyObservers(msg);
    }

    @Override
    public void clear() {
        log.info("[Chat]: Clearing chat history");
        messages.clear();
        setChanged();
        notifyObservers(null);
    }

}
