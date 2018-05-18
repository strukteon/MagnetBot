package net.magnetbot.commands.music;
/*
    Created by nils on 08.02.2018 at 01:21.
    
    (c) nils 2018
*/

import net.magnetbot.audio.AudioCore;
import net.magnetbot.audio.TrackScheduler;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;

public class Pause implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        TrackScheduler scheduler = AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler;
        if (!scheduler.isPaused()){
            scheduler.pause();
            event.getTextChannel().sendMessage(Message.INFO(event, "Paused the queue").build()).queue();
        } else
            event.getTextChannel().sendMessage(Message.ERROR(event, "Queue is already paused").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("pause", PermissionLevel.MEMBER)
                        .setHelp("pauses the media playback");
    }
}
