package net.magnetbot.commands.fun;
/*
    Created by nils on 20.03.2018 at 17:01.
    
    (c) nils 2018
*/

import com.google.gson.JsonArray;
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
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.core.tools.Tools;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class Urban implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        if (!event.getTextChannel().isNSFW()){
            event.getTextChannel().sendMessage(Message.ERROR(event, "Sorry, but this command is only allowed in nsfw channels as it may contain nsfw-related content.").build()).queue();
            return;
        }
        long cur = System.currentTimeMillis();
        String apiUrl = "http://api.urbandictionary.com/v0/define?term=";
        URL url = new URL(apiUrl + URLEncoder.encode(Tools.listToString(syntax.getAsListString("query"), " "), "UTF-8"));

        JsonElement element = new JsonParser().parse(new InputStreamReader(url.openStream()));

        JsonArray list = element.getAsJsonObject().getAsJsonArray("list");
        JsonObject def = list.get(0).getAsJsonObject();

        EmbedBuilder builder = Message.INFO(event);

        builder.setTitle("**Description of " + def.get("word").getAsString() + " (" + def.get("permalink").getAsString() + ")**");

        builder.setDescription(def.get("definition").getAsString())
                .addField("Example:", def.get("example").getAsString(), false)
        .addField("", def.get("thumbs_up").getAsInt() + ":+1:, " + def.get("thumbs_down").getAsInt() + ":-1:", true);

        event.getTextChannel().sendMessage(builder.build()).queue();
        System.out.println("Time needed: " + (System.currentTimeMillis() - cur) + "ms");
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("urban", PermissionLevel.MEMBER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("query", SyntaxElementType.STRING, true)
                )
                .setHelp("look somethink up in the urban dictionary")
                .setAlias(
                        "dict",
                        "urbandict"
                );
    }
}
