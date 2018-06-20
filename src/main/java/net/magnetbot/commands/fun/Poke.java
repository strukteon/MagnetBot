package net.magnetbot.commands.fun;
/*
    Created by nils on 30.01.2018 at 20:19.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;

public class Poke implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        Member m = syntax.getAsMember("user");
        PrivateChannel dm = m.getUser().openPrivateChannel().complete();

        dm.sendMessage(Message.INFO_RAW(":point_left: " + event.getAuthor().getAsMention() + " poked you!").build()).queue();

        dm.close().queue();
        event.getTextChannel().sendMessage(Message.INFO(event, ":information_source: Poked **" + m.getEffectiveName() + "**").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("poke", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addElement("user", SyntaxElementType.MEMBER)
                        )
                        .setHelp("send a pm to a member to wake him up");
    }
}
