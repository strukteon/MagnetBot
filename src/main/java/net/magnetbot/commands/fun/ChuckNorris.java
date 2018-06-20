package net.magnetbot.commands.fun;
/*
    Created by nils on 18.03.2018 at 22:24.
    
    (c) nils 2018
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;

import java.io.InputStreamReader;
import java.net.URL;

public class ChuckNorris implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String apiUrl = "http://api.icndb.com/jokes/random";
        URL url = new URL(apiUrl);

        JsonElement element = new JsonParser().parse(new InputStreamReader(url.openStream()));

        JsonObject joke = element.getAsJsonObject();

        EmbedBuilder builder = Message.INFO(event);

        builder.setDescription(joke.get("value").getAsJsonObject().get("joke").getAsString());

        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("chucknorris", PermissionLevel.MEMBER)
                .setHelp("generate a true tale about chuck norris")
                .setAlias(
                        "cn",
                        "norris"
                );
    }
}
