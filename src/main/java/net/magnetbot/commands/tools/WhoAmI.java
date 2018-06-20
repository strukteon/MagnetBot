package net.magnetbot.commands.tools;
/*
    Created by nils on 30.12.2017 at 03:21.
    
    (c) nils 2017
*/

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.utils.PlayerInfo;

public class WhoAmI implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        event.getTextChannel().sendMessage(PlayerInfo.getBuilder(event.getMember()).build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("whoami", PermissionLevel.MEMBER)
                        .setHelp("gives you some informations about you");
    }

    private String getGame(Member author){
        if (author.getGame() == null)
            return "none";
        return author.getGame().getName();
    }
    private String getNickname(Member author){
        if (author.getNickname() == null)
            return "none";
        return author.getNickname();
    }
}
