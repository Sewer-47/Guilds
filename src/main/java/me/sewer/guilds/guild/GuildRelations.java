package me.sewer.guilds.guild;

import me.sewer.guilds.Relation;
import me.sewer.guilds.Request;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class GuildRelations {

    private Optional<Request> lastRequest = Optional.empty();
    private final Map<UUID, Relation> relations = new HashMap<>();

    public GuildRelations() {
    }

    public boolean acceptLastRequest() {
        if (!this.lastRequest.isPresent()) {
            return false;
        }

        Request request = this.lastRequest.get();
        request.accept();

        this.lastRequest = Optional.empty();
        return true;
    }

    public boolean declineLastRequest() {
        if (!this.lastRequest.isPresent()) {
            return false;
        }

        Request request = this.lastRequest.get();
        request.deny();

        this.lastRequest = Optional.empty();
        return true;
    }

    public Request getLastRequest() {
        return this.lastRequest.isPresent() ? this.lastRequest.get() : null;
    }

    public void setLastRequest(Optional<Request> lastRequest) {
        this.lastRequest = lastRequest;
    }

    public void request(Request request) {
        this.lastRequest = Optional.of(new GuildRelations.LastRequest(request, Instant.now().plus(5, ChronoUnit.MINUTES)).get());
    }

    public void request(Request request, Instant timeout) {
        this.lastRequest = Optional.of(new GuildRelations.LastRequest(request, timeout).get());
    }

    private class LastRequest {
        final Request request;
        final Instant timeout;

        LastRequest(Request request, Instant timeout) {
            this.request = request;
            this.timeout = timeout;
        }

        Request get() {
            return Instant.now().isBefore(this.timeout) ? this.request : null;
        }
    }

    public Map<UUID, Relation> getAll() {
        return this.relations;
    }

    public Relation relation(UUID guildId) {
        return this.relations.get(guildId);
    }

    public void remove(UUID guildId) {
        if (!this.relations.containsKey(guildId)) {
            this.relations.remove(guildId);
        }
    }

    public void set(UUID guildId, Relation relation) {
        if (this.contains(guildId)) {
            this.remove(guildId);
        }
        this.relations.put(guildId, relation);
    }

    public boolean contains(UUID guildId) {
        Relation relation = this.relation(guildId);
        if (relation == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isAlly(UUID guildId) {
        if (!this.contains(guildId)) {
            return false;
        }
        if (this.relation(guildId).equals(Relation.ALLY)) {
            return true;
        } else {
            return false;
        }
    }
}
