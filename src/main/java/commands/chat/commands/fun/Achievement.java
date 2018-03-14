package commands.chat.commands.fun;
/*
    Created by nils on 09.03.2018 at 23:40.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.tools.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;
import utils.Static;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Achievement implements ChatCommand {
    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m achievement <text>").build()).queue();
        else {

            EmbedBuilder builder = Message.INFO(event);
            builder.setImage("https://www.minecraftskinstealer.com/achievement/a.php?i=2&h=" + URLEncoder.encode("Achievement get!", "UTF-8") + "&t=" +
                    URLEncoder.encode(Tools.argsToString(rawArgs, " "), "UTF-8"));

            event.getTextChannel().sendMessage(builder.build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("achievement", 0)
                .setHelp("create a minecraft achievement");
    }
}
