package me.sewer.guilds.guild;

import java.util.HashSet;
import java.util.Set;

public class GuildRelations {

    private final Set<Guild> friendsInv = new HashSet<>();
    private final Set<Guild> friends = new HashSet<>();
    private final Set<Guild> enemies = new HashSet<>();

    public Set<Guild> getFriendsInvs() {
        return this.friendsInv;
    }

    public Set<Guild> getFriends() {
        return this.friends;
    }

    public Set<Guild> getEnemies() {
        return this.enemies;
    }
}
