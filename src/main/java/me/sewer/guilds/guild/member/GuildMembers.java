package me.sewer.guilds.guild.member;

import me.sewer.guilds.user.User;

import java.util.*;

public class GuildMembers {

    private final Map<UUID, GuildMember> members = new HashMap<>();
    private final UUID ownerId;

    public GuildMembers(GuildMember member) {
        this.members.put(member.getUser().getUniqueId(), member);
        this.ownerId = member.getUser().getUniqueId();
    }

    public void addPlayer(GuildMember member) {//Change to uuid
        this.members.put(member.getUser().getUniqueId(), member);
    }

    public void removePlayer(UUID playerId) {
        this.members.remove(playerId);
    }

    public GuildMember getMember(UUID uuid) {
        return this.members.get(uuid);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public List<UUID> getAll() {
        return new ArrayList<>(this.members.keySet());
    }

    public boolean contains(GuildMember guildMember) {
        return this.members.values().contains(guildMember);
    }

    public boolean contains(User user) {
        return this.contains(user.getUniqueId());
    }

    public boolean contains(UUID uuid) {
        return this.members.keySet().contains(uuid);
    }
}
