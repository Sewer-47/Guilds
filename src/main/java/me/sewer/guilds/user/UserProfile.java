package me.sewer.guilds.user;

import me.sewer.guilds.Unique;
import me.sewer.guilds.guild.Guild;

import java.util.Locale;
import java.util.Optional;

public interface UserProfile extends Unique {

    String getUsername();

    Optional<Guild> getGuild();

    void setGuild(Guild guild);

    Locale getLocale();

    void setLocale(Locale locale);
}
