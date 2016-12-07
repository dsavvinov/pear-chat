package models;

import models.pojos.Message;
import models.pojos.User;

import java.util.Date;
import java.util.List;
import java.util.Observable;

/**
 * Model of Chat (a history of sent and received messages)
 *
 * Potentially can be changed from another models (e.g. from Agent
 * on receiving message), therefore implements Observable to notify
 * UI about such updates.
 *
 * Implementors should properly notify observers on any changes,
 * following contracts, described by each method.
 */
public abstract class Chat extends Observable {
    /**
     * Implementors should notify observer, passing added message instance
     * as `arg`.
     */
    abstract public void addMessage(User from, Date date, String text);

    /**
     * Implementors should notify observers, passing `null` as `arg`.
     */
    abstract public void clear();


    abstract public List<Message> getMessages();
}
