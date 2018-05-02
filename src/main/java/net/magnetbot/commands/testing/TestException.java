package net.magnetbot.commands.testing;
/*
    Created by nils on 18.03.2018 at 21:05.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TestException implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        throw new Exception("This is a Exception");
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("exception", PermissionLevel.BOT_ADMIN);
    }
}
