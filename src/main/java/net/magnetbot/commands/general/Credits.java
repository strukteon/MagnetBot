package net.magnetbot.commands.general;
/*
    Created by nils on 18.05.2018 at 18:13.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;

public class Credits implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        EmbedBuilder b = Message.INFO(event, "");
        b.setDescription("This is a list of projects and users who inspired or helped me")
                .addField("Inspirations:",
                        "[Rxsto](https://rxsto.github.io) - *the idea of the **control panel** is from him*"
                , false);

        event.getChannel().sendMessage(b.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("credits", PermissionLevel.MEMBER)
                .setHelp("see a list of projects and users who inspired or helped me");
    }
}
