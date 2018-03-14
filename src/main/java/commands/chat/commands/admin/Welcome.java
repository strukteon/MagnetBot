package commands.chat.commands.admin;
/*
    Created by nils on 07.03.2018 at 00:31.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.GuildData;
import core.tools.AutoComplete;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import utils.Static;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Welcome extends ListenerAdapter implements ChatCommand {
    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m welcome set <channel>\n -m welcome off").build()).queue();
        else {

            if (args[0].equals("off")){
                String welcomeChannel = GuildData.getWelcomeChannel(event.getGuild().getId());
                if (welcomeChannel.equals(""))
                    event.getTextChannel().sendMessage(Message.INFO(event,"Welcome message is already disabled.").build()).queue();
                else {
                    GuildData.setWelcomeChannel(event.getGuild().getId(), "");
                    event.getTextChannel().sendMessage(Message.INFO(event,"Welcome message turned off.").build()).queue();
                }
            }

            else if (args[0].equals("set") && args.length >= 2){
                if (!args[1].equals("")) {
                    System.out.println(args[1]);
                    TextChannel c = AutoComplete.textChannel(event.getGuild().getTextChannels(), args[1]);

                    if (c == null)
                        event.getTextChannel().sendMessage(Message.ERROR(event, "TextChannel ``" + args[1] + "`` not found.", false).build()).queue();
                    else {
                        GuildData.setWelcomeChannel(event.getGuild().getId(), c.getId());
                        event.getTextChannel().sendMessage(Message.INFO(event, "Welcome message channel set to " + c.getAsMention() + "!").build()).queue();
                    }

                } else
                    event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m welcome set <channel>\n -m welcome off").build()).queue();
            }

            else
                event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m welcome set <channel>\n -m welcome off").build()).queue();



        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {
            String ch = GuildData.getWelcomeChannel(event.getGuild().getId());

            if (!ch.equals("")){
                TextChannel channel = event.getGuild().getTextChannelById(ch);
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
        return new Chat.CommandInfo("welcome", 1)
                .setHelp("set a channel for the welcome message that will be displayed when someone joins");
    }
}
