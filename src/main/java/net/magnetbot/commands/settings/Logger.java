package net.magnetbot.commands.settings;
/*
    Created by nils on 05.04.2018 at 22:59.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;

public class Logger implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("logger", PermissionLevel.ADMIN)
                        .setSyntax(
                                new SyntaxBuilder(
                                        new SyntaxBuilder()
                                                .addSubcommand("set", "set")
                                                .addElement("channel", SyntaxElementType.TEXTCHANNEL),
                                        new SyntaxBuilder()
                                                .addSubcommand("disable", "disable")
                                )
                        );
    }
}
