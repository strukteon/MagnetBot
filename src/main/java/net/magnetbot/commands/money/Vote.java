package net.magnetbot.commands.money;
/*
    Created by nils on 13.02.2018 at 19:28.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;

public class Vote implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        event.getTextChannel().sendMessage(Message.INFO(event, "You can vote now on [discordbotlist.org](https://discordbots.org/bot/389016516261314570/vote)! The reward will be automatically added to your profile!").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("vote", PermissionLevel.MEMBER)
                        .setHelp("vote for the bot on [discordbotlist.org](https://discordbots.org/bot/389016516261314570/vote) and get a reward");
    }
}
