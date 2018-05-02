package net.magnetbot.commands.music;
/*
    Created by nils on 25.02.2018 at 23:20.
    
    (c) nils 2018
*/

import net.magnetbot.audio.AudioInfo;
import net.magnetbot.audio.GuildMusicManager;
import net.magnetbot.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.MagnetBot;
import net.magnetbot.core.tools.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;

public class Queue implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        GuildMusicManager manager = MagnetBot.audioCore.getGuildAudioPlayer(event.getGuild());
        List<AudioInfo> queue = new ArrayList<>(manager.scheduler.getQueue());

        if (queue.size() < 1 && manager.scheduler.getCurrentTrack() == null){
            event.getTextChannel().sendMessage(Message.INFO(event, ":musical_note: Infos about the queue", "The current queue is empty!").build()).queue();
        } else {
            EmbedBuilder builder = Message.INFO(event);
            builder
                    .setTitle(":musical_note: Current queue")

                    .addField("Now playing", audioInfo(manager.scheduler.getCurrentTrack()), false);

            for (int i = 0; i < queue.size(); i++) {
                builder.addField("Position #" + (i + 1), audioInfo(queue.get(i)), false);
            }
            event.getTextChannel().sendMessage(builder.build()).queue();
        }

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("queue", PermissionLevel.MEMBER)
                        .setHelp("show infos about the current queue");
    }

    private String audioInfo(AudioInfo audioInfo){
        String trackUrl = (Tools.isUrl(audioInfo.getTrack().getIdentifier()) ? audioInfo.getTrack().getIdentifier() : "https://youtube.com/watch?v=" + audioInfo.getTrack().getIdentifier() );
        AudioTrackInfo info = audioInfo.getTrack().getInfo();
        return "**[" + info.title + "](" + trackUrl + ")** by *" + info.author + "* **[" + TrackScheduler.getTimestamp(info.length) + "]**";
    }
}