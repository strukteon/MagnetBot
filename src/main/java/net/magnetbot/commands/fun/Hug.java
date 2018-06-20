package net.magnetbot.commands.fun;
/*
    Created by nils on 18.05.2018 at 21:12.
    
    (c) nils 2018
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Hug implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String apiUrl = "https://nekos.life/api/v2/img/hug";
        URLConnection connection = new URL(apiUrl).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();

        JsonElement element = new JsonParser().parse(new InputStreamReader(connection.getInputStream()));

        EmbedBuilder builder = Message.INFO(event);


        builder.setDescription("**" + event.getMember().getAsMention() + " hugs " + syntax.getAsMember("user").getAsMention() + "**")
                .setImage(element.getAsJsonObject().get("url").getAsString());

        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("hug", PermissionLevel.MEMBER)
                .setHelp("hug someone")
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("user", SyntaxElementType.MEMBER)
                );
    }
}
