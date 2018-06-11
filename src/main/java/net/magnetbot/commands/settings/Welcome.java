package net.magnetbot.commands.settings;
/*
    Created by nils on 07.03.2018 at 00:31.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.Command;
 import net.dv8tion.jda.core.Permission;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.core.sql.GuildSQL;
import net.magnetbot.utils.WelcomeImageUtil;

import java.io.File;

public class Welcome extends ListenerAdapter implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        GuildSQL guildSQL = GuildSQL.fromGuild(event.getGuild());
        switch (syntax.getAsSubCommand("action").getContent()) {

            case "set":
                TextChannel c = syntax.getAsTextChannel("channel");
                guildSQL.setWelcomeChannel(c.getId());
                event.getTextChannel().sendMessage(Message.INFO(event, "Welcome message channel set to " + c.getAsMention() + "!").build()).queue();
                break;

            case "off":
                TextChannel welcomeChannel = guildSQL.getWelcomeChannel();
                if (welcomeChannel == null)
                    event.getTextChannel().sendMessage(Message.INFO(event, "Welcome message is already disabled.").build()).queue();
                else {
                    guildSQL.setWelcomeChannel(null);
                    event.getTextChannel().sendMessage(Message.INFO(event, "Welcome message turned off.").build()).queue();
                }
                break;
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {
            TextChannel channel = GuildSQL.fromGuild(event.getGuild()).getWelcomeChannel();

            if (channel != null){
                User user = event.getMember().getUser();
                File temp = new WelcomeImageUtil(user.getEffectiveAvatarUrl(), user.getName(), user.getDiscriminator()).getFile();

                channel.sendFile(temp).queue();
                temp.delete();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Permission[] requiredUserPerms() {
        return new Permission[]{Permission.MANAGE_SERVER};
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("welcome", PermissionLevel.ADMIN)
                .setSyntax(
                        new SyntaxBuilder(
                                new SyntaxBuilder()
                                        .addSubcommand("action", "set")
                                        .addElement("channel", SyntaxElementType.TEXTCHANNEL),
                                new SyntaxBuilder()
                                        .addSubcommand("action", "off")
                        )
                )
                .setHelp("set a channel for the welcome message that will be displayed when someone joins");
    }
}
