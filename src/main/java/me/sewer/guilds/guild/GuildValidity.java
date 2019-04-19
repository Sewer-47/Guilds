package me.sewer.guilds.guild;

import me.sewer.guilds.validity.Validity;

import java.time.LocalDateTime;

public class GuildValidity extends Validity {

    public GuildValidity(LocalDateTime expires) {
        super(expires);
    }
}
