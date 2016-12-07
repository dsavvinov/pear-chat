package models;

import java.util.Observable;

/**
 * Abstract class for logging.
 *
 * Note that implementors should notify all observers properly
 * at the end of each method, attaching 'msg' to the notification.
 */
public abstract class Log extends Observable {
    public abstract void info(String msg);
    public abstract void error(String msg);
}
