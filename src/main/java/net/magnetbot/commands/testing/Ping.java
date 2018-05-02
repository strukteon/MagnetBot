package net.magnetbot.commands.testing;
/*
    Created by nils on 30.12.2017 at 01:55.
    
    (c) nils 2017
*/

import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        event.getTextChannel().sendMessage(Message.INFO(event, "My ping is: " + event.getJDA().getPing() + "ms").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("ping", PermissionLevel.MEMBER)
                        .setHelp("gives you the connection speed from the bot to discord");
    }
}
