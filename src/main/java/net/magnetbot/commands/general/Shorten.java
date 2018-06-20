package net.magnetbot.commands.general;
/*
    Created by nils on 07.03.2018 at 23:42.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Shorten implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String url = syntax.getAsString("url");
        if (! (url.startsWith("https://") || url.startsWith("http://")) ){
            url = "http://" + url;
        }
        URLConnection connection = new URL("http://terse.tk/?url=" + URLEncoder.encode(url, "UTF-8")).openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));

        event.getTextChannel().sendMessage(Message.INFO(event, "Your URL has been shortened:\nhttps://terse.tk/" + in.readLine()).build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("shorten", PermissionLevel.MEMBER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("url", SyntaxElementType.STRING)
                )
                .setHelp("simply shorten an url");
    }
}
