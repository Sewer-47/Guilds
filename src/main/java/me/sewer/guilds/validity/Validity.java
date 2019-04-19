package me.sewer.guilds.validity;

import java.time.LocalDateTime;

public abstract class Validity implements IValidity {

    private final LocalDateTime expires;

    public Validity(LocalDateTime expires) {
        this.expires = expires;
    }

    @Override
    public LocalDateTime getExpires() {
        return this.expires;
    }

    @Override
    public boolean hasExpired() {
        return this.hasExpired(LocalDateTime.now());
    }

    @Override
    public boolean hasExpired(LocalDateTime when) {
        return !this.expires.isBefore(when);
    }
}
