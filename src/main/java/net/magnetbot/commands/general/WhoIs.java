package net.magnetbot.commands.general;
/*
    Created by nils on 30.12.2017 at 04:07.
    
    (c) nils 2017
*/

import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.utils.PlayerInfo;

public class WhoIs implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        Member member = syntax.getAsMember("member");

        event.getTextChannel().sendMessage(PlayerInfo.getBuilder(member).build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("whois", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder().addElement(new SyntaxElement("member", SyntaxElementType.MEMBER))
                        )
                        .setHelp("gives you some informations about an user");
    }

    private String getGame(Member member){
        if (member.getGame() == null)
            return "none";
        return member.getGame().getName();
    }
    private String getNickname(Member member){
        if (member.getNickname() == null)
            return "none";
        return member.getNickname();
    }
}
