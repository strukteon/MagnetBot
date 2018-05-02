package net.magnetbot.commands.testing;
/*
    Created by nils on 07.04.2018 at 23:35.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;

public class Shutdown implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        System.exit(0);
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("shutdown", PermissionLevel.BOT_OWNER)
                .setHelp("shuts this bot down");
    }
}
