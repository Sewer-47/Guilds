package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.member.GuildPermission;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

public class SethomeCommand extends Command {

    public static final String NAME = "sethome";
    public static final String DESCRIPTION = "setting guild home";
    private final UserManager userManager;
    private final MessageManager messageManager;

    public SethomeCommand(UserManager userManager, MessageManager messageManager) {
        super(NAME, DESCRIPTION);
        this.userManager = userManager;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            if (user.getGuild().isPresent()) {
                Guild guild = user.getGuild().get();
                GuildMembers guildMembers = user.getGuild().get().getMembers();
                if (guildMembers.getMember(user.getUniqueId()).hasPermission(GuildPermission.SETHOME)) {
                        user.getBukkit().ifPresent(player -> {
                            Vector vector = player.getLocation().toVector();
                            if (guild.getTerrain().getRegion().contains(vector)) {
                                guild.getTerrain().setHome(vector);
                                user.sendMessage("guildHomeSet");
                            } else {
                                user.sendMessage("mustBeInGuildRegion");
                            }
                        });
                } else {
                    String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.SETHOME.getDisplayName());
                    user.sendMessage("noPerm", perm);
                }
            } else {
                user.sendMessage("noGuild");
            }
        });
        return true;
    }
}
