package net.magnetbot.commands.music;
/*
    Created by nils on 02.03.2018 at 17:14.
    
    (c) nils 2018
*/

import net.magnetbot.MagnetBot;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.sql.GuildSQL;

public class LoadQueue implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        List<String> queueList = GuildSQL.fromGuild(event.getGuild()).getSavedQueue();
        if (queueList.size() < 1)
            event.getTextChannel().sendMessage(Message.ERROR(event, "The saved queue is empty.").build()).queue();
        else {
            MagnetBot.audioCore.loadQueue(event, queueList);
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("loadqueue", PermissionLevel.MEMBER)
                        .setAlias("loadq", "load")
                        .setHelp("load the saved queue from the server");
    }
}
