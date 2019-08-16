package me.sewer.guilds.command;

import me.sewer.guilds.user.User;

public interface ICommand {

    boolean onCommand(User user, String... args);

}
