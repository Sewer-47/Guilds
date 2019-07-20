package me.sewer.guilds.guild;

import me.sewer.guilds.user.User;

import java.util.HashSet;
import java.util.Set;

public class GuildMemebers {

    private final Set<User> members = new HashSet<>();
    private final User owner;

    public GuildMemebers(User owner) {
        this.owner = owner;
        this.members.add(owner);
    }

    public Set<User> getAll() {
        return members;
    }

    public User getOwner() {
        return owner;
    }
}
