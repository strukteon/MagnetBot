package net.magnetbot.commands.music;
/*
    Created by nils on 02.03.2018 at 17:14.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.sql.GuildSQL;

import java.util.List;

public class LoadQueue implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        if (event.getMember().getVoiceState().getChannel() == null)
            event.getTextChannel().sendMessage(Message.ERROR(event, "You have to be connected to a VoiceChannel!").build()).queue();
        else {
            List<String> queueList = GuildSQL.fromGuild(event.getGuild()).getSavedQueue();
            if (queueList.size() < 1)
                event.getTextChannel().sendMessage(Message.ERROR(event, "The saved queue is empty.").build()).queue();
            else
                AudioCore.loadMultiple(event, queueList, event.getMember().getVoiceState().getChannel());
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
