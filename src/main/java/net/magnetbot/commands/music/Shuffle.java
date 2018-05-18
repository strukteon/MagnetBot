package net.magnetbot.commands.music;
/*
    Created by nils on 15.05.2018 at 23:46.
    
    (c) nils 2018
*/

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.audio.TrackScheduler;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;

public class Shuffle implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        TrackScheduler scheduler = AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler;
        if (scheduler.isPlaying(true)){
            scheduler.shuffleQueue(false);
            event.getTextChannel().sendMessage(Message.INFO(event, "The queue has been shuffled").build()).queue();
        } else {
            event.getTextChannel().sendMessage(Message.ERROR(event, "The queue is empty").build()).queue();
        }

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("shuffle", PermissionLevel.MEMBER)
                        .setHelp("shuffle the current queue");
    }
}
