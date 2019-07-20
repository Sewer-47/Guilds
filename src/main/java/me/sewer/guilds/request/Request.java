package me.sewer.guilds.request;

import me.sewer.guilds.user.User;

public abstract class Request {

    private final User owner;

    public Request(User owner) {
        this.owner = owner;
    }

    public abstract void accept();

    public abstract void deny();

    public User getOwner() {
        return this.owner;
    }
}