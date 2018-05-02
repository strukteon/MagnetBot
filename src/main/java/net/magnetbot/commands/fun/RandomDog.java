package net.magnetbot.commands.fun;
/*
    Created by nils on 18.03.2018 at 23:08.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.InputStreamReader;
import java.net.URL;

public class RandomDog implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String apiUrl = "https://dog.ceo/api/breeds/image/random";
        URL url = new URL(apiUrl);

        JsonElement element = new JsonParser().parse(new InputStreamReader(url.openStream()));

        JsonObject dog = element.getAsJsonObject();

        EmbedBuilder builder = Message.INFO(event);

        builder.setImage(dog.get("message").getAsString());

        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("randomdog", PermissionLevel.MEMBER)
                .setHelp("generate a pic of a cute dog")
                .setAlias("rdog",
                        "dog");
    }
}
