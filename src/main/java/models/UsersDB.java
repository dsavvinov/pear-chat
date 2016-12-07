package models;

import models.pojos.User;

import java.util.Observable;

public abstract class UsersDB extends Observable {
    public abstract User getUser(String nick);
    public abstract void addUser(String nick, String address);
}
