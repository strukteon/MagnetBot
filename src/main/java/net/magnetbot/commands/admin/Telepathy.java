package net.magnetbot.commands.admin;
/*
    Created by nils on 06.04.2018 at 10:32.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;

public class Telepathy implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("telepathy", PermissionLevel.ADMIN)
                .setSyntax(
                        new SyntaxBuilder(
                                new SyntaxBuilder()
                        )
                );
    }
}
