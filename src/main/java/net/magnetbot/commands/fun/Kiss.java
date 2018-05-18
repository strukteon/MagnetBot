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

public class Kiss implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String apiUrl = "https://nekos.life/api/kiss";
        URL url = new URL(apiUrl);

        JsonElement element = new JsonParser().parse(new InputStreamReader(url.openStream()));

        EmbedBuilder builder = Message.INFO(event);


        builder.setDescription("**" + event.getMember().getAsMention() + " kisses " + syntax.getAsMember("user").getAsMention() + "**")
                .setImage(element.getAsJsonObject().get("url").getAsString());

        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("kiss", PermissionLevel.MEMBER)
                .setHelp("kiss someone")
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("user", SyntaxElementType.MEMBER)
                );
    }
}
