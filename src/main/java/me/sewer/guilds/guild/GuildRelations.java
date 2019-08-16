package me.sewer.guilds.guild;

import me.sewer.guilds.Request;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GuildRelations {

    private Optional<Request> lastRequest = Optional.empty();
    private final Set<Guild> friends = new HashSet<>();
    private final Set<Guild> enemies = new HashSet<>();

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

    public Set<Guild> getFriends() {
        return this.friends;
    }

    public Set<Guild> getEnemies() {
        return this.enemies;
    }
}
