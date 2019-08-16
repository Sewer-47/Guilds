package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.user.User;

public class UnkownCommand extends Command {

    public UnkownCommand() {
        super(null);
    }

    @Override
    public boolean onCommand(User user, String... args) {
        user.sendMessage("unkownCommand");
        return true;
    }
}
