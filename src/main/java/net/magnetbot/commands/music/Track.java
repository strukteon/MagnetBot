package net.magnetbot.commands.music;
/*
    Created by nils on 08.02.2018 at 18:19.
    
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

public class Track implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        TrackScheduler scheduler = AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler;
        if (scheduler.isPlaying(true)){
            AudioTrack track = scheduler.getCurrentTrack().getTrack();
            event.getTextChannel().sendMessage(
                    Message.INFO(event, "**Currently playing:** ***" + track.getInfo().title + "*** by ***" + track.getInfo().author + "***\n" +
                    "**" + TrackScheduler.getTimestamp(scheduler.getPlayer().getPlayingTrack().getPosition()) + " / " + TrackScheduler.getTimestamp(track.getDuration()) + "**").build()).queue();

        } else {
            event.getTextChannel().sendMessage(Message.ERROR(event, "No track is playing").build()).queue();
        }

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("now", PermissionLevel.MEMBER)
                        .setAlias("tinfo",
                                "song")
                        .setHelp("shows info about the playing track");
    }
}
