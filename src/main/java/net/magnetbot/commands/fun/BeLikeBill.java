package net.magnetbot.commands.fun;
/*
    Created by nils on 18.03.2018 at 22:40.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;

public class BeLikeBill implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String apiUrl = "http://belikebill.azurewebsites.net/billgen-API.php?default=1";

        EmbedBuilder builder = Message.INFO(event);

        builder.setImage(apiUrl + "&refresh=" + Math.random());

        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("blb", PermissionLevel.MEMBER)
                .setHelp("generate a random be like bill meme")
                .setAlias("belikebill",
                        "bill");
    }
}
