package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.permission.GuildPermission;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import org.bukkit.util.Vector;

public class SethomeCommand extends Command {

    public static final String NAME = "sethome";
    private final MessageManager messageManager;

    public SethomeCommand(MessageManager messageManager) {
        super(NAME, "ustawbaze");
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(User user, String... args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        Guild guild = user.getGuild().get();
        GuildMembers guildMembers = user.getGuild().get().getMembers();
        if (!guildMembers.getMember(user.getUniqueId()).hasPermission(GuildPermission.SETHOME)) {
            String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.SETHOME.getDisplayName());
            user.sendMessage("noPerm", perm);
            return true;

        }
        user.getBukkit().ifPresent(player -> {
            Vector vector = player.getLocation().toVector();
            if (guild.getTerrain().getRegion().contains(vector)) {
                guild.getTerrain().setHome(vector);
                user.sendMessage("guildHomeSet");
            } else {
                user.sendMessage("mustBeInGuildRegion");
            }
        });
        return true;
    }
}
