package models.implementations;

import models.Log;

public class LogImpl extends Log {
    @Override
    public void info(String msg) {
        setChanged();
        notifyObservers(msg + "\n");
        System.out.println(msg);
    }

    @Override
    public void error(String msg) {
        setChanged();
        notifyObservers(msg + "\n");
        System.err.println(msg);
    }
}
