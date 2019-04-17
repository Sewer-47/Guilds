package me.sewer.guilds.command;

import org.bukkit.command.CommandSender;

public interface ICommand {

    boolean onCommand(CommandSender sender, String[] args);
}
