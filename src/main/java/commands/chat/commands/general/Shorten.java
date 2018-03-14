package commands.chat.commands.general;
/*
    Created by nils on 07.03.2018 at 23:42.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Shorten implements ChatCommand {
    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m shorten <url>").build()).queue();
        else {
            String url = args[0];
            if (! (url.startsWith("https://") || url.startsWith("http://")) ){
                url = "http://" + url;
            }
            URLConnection connection = new URL("http://chop.gq/create.php?url=" + URLEncoder.encode(url, "UTF-8")).openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            event.getTextChannel().sendMessage(Message.INFO(event, "Your URL has been shortened:\nhttps://chop.gq/" + in.readLine()).build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("shorten", 0)
                .setHelp("simply shorten an url");
    }
}
