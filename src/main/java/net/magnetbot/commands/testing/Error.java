package net.magnetbot.commands.testing;
/*
    Created by nils on 30.01.2018 at 18:24.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Error implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        StringBuilder builder = new StringBuilder();
        for (String s : syntax.getAsListString("message")){
            if (builder.length() != 0)
                builder.append(" ");
            builder.append(s);
        }
        event.getTextChannel().sendMessage(Message.ERROR(event, builder.toString()).setTitle("**Custom Error**").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("error", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addElement("message", SyntaxElementType.STRING, true)
                        )
                        .setHelp("shows a custom error message");
    }
}
