package net.magnetbot.commands.settings;
/*
    Created by nils on 07.03.2018 at 00:31.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.Command;
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
import org.apache.commons.io.IOUtils;
import net.magnetbot.utils.Static;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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
                File temp = File.createTempFile("welcome-", ".png");
                URL url = new URL("http://" + Static.API_BASEURL + "/images/welcome/?user="+user.getName()+
                        "&discrim="+user.getDiscriminator()+
                        "&member="+event.getGuild().getMembers().size()+"&url=" + URLEncoder.encode(user.getEffectiveAvatarUrl(), "UTF-8"));
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                connection.connect();

                OutputStream outputStream = new FileOutputStream(temp);
                IOUtils.copy(connection.getInputStream(), outputStream);
                outputStream.close();

                channel.sendFile(temp).queue();
                temp.delete();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

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
