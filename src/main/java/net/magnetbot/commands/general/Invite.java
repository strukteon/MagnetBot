package net.magnetbot.commands.general;
/*
    Created by nils on 06.01.2018 at 15:04.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;

public class Invite implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        event.getTextChannel().sendMessage(Message.INFO(event, "You can invite me with the following link: https://magnetbot.net/redirect?rel=invite").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("invite", PermissionLevel.MEMBER)
                        .setHelp("gives you the invite link for this bot");
    }
}
