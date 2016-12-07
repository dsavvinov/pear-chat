package net;

import models.pojos.User;

/**
 * Interface for absracting network interactions from other parts of system
 */
public interface Agent {
    boolean sendMessage(User author, User adressee, String message);

    void shutdown();
}
