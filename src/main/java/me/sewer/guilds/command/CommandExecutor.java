package me.sewer.guilds.command;

import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;

public class CommandExecutor {

    private final CommandManager commandManager;
    private final MessageManager messageManager;

    public CommandExecutor (CommandManager commandManager, MessageManager messageManager) {
        this.commandManager = commandManager;
        this.messageManager = messageManager;
    }

    public void execute(String cmd, User user, String... args) {
        Command command = this.commandManager.get(cmd);

        if (!command.onCommand(user, args)) {
            String usage = this.messageManager.getMessage(user.getLocale(), command.getUsage());
            user.sendMessage("correctUsage", usage);
        }
    }
}