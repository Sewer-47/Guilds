package me.sewer.guilds.validity;

import java.time.LocalDateTime;

public interface IValidity {

    LocalDateTime getExpires();

    boolean hasExpired(LocalDateTime when);

    boolean hasExpired();
}
