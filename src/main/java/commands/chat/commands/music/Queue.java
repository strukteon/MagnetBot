package commands.chat.commands.music;
/*
    Created by nils on 25.02.2018 at 23:20.
    
    (c) nils 2018
*/

import audio.AudioInfo;
import audio.GuildMusicManager;
import audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.GuildData;
import core.Main;
import core.tools.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Emoji;
import utils.Static;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Queue implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        GuildMusicManager manager = Main.audioCore.getGuildAudioPlayer(event.getGuild());
        List<AudioInfo> queue = new ArrayList<>(manager.scheduler.getQueue());

        if (queue.size() < 1 && manager.scheduler.getCurrentTrack() == null){
            event.getTextChannel().sendMessage(Message.INFO(event, Emoji.MUSIC + " Infos about the queue", "The current queue is empty!").build()).queue();
        } else {
            EmbedBuilder builder = Message.INFO(event);
            builder
                    .setTitle(Emoji.MUSIC + " Current queue")

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
                new Chat.CommandInfo("queue", 0)
                        .setHelp("show infos about the current queue");
    }

    private String audioInfo(AudioInfo audioInfo){
        String trackUrl = (Tools.isUrl(audioInfo.getTrack().getIdentifier()) ? audioInfo.getTrack().getIdentifier() : "https://youtube.com/watch?v=" + audioInfo.getTrack().getIdentifier() );
        AudioTrackInfo info = audioInfo.getTrack().getInfo();
        return "**[" + info.title + "](" + trackUrl + ")** by *" + info.author + "* **[" + TrackScheduler.getTimestamp(info.length) + "]**";
    }
}
