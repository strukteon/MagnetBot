package net.magnetbot.commands.fun;
/*
    Created by nils on 06.01.2018 at 19:15.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.tools.Tools;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TTS implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        MessageBuilder builder = new MessageBuilder();
        builder .append(Tools.listToString(syntax.getAsListString("text"), " "))
                .setTTS(true);

        event.getTextChannel().sendMessage(builder.build()).complete().delete().queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("tts", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addElement("text", SyntaxElementType.STRING, true)
                        )
                        .setHelp("let this bot send a tts message");
    }
}
