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
import java.net.URLConnection;

public class RandomDog implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String apiUrl = "https://dog.ceo/api/breeds/image/random";

        URLConnection connection = new URL(apiUrl).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();

        JsonElement element = new JsonParser().parse(new InputStreamReader(connection.getInputStream()));

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
