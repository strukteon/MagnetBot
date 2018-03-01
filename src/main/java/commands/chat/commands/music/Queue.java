package commands.chat.commands.music;
/*
    Created by nils on 25.02.2018 at 23:20.
    
    (c) nils 2018
*/

import audio.AudioInfo;
import audio.GuildMusicManager;
import audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
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
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("queue");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        GuildMusicManager manager = Main.audioCore.getGuildAudioPlayer(event.getGuild());
        List queueArray = new ArrayList(manager.scheduler.getQueue());
        List<AudioInfo> queue = new ArrayList<>(queueArray);

        if (queue.size() < 1 && manager.scheduler.getCurrentTrack() == null){
            event.getTextChannel().sendMessage(Message.INFO(event, Emoji.MUSIC + " Infos about the queue", "The current queue is empty!").build()).queue();
        } else {
            EmbedBuilder builder = Message.INFO(event);
            builder
                    .setTitle("Current queue")

                    .addField("Now playing", audioInfo(manager.scheduler.getCurrentTrack()), false);

            for (int i = 0; i < queue.size(); i++) {
                builder.addField("Position #" + (i + 1), audioInfo(queue.get(i)), false);
            }
            event.getTextChannel().sendMessage(builder.build()).queue();
        }
    }

    private String audioInfo(AudioInfo audioInfo){
        String trackUrl = (Tools.isUrl(audioInfo.getTrack().getIdentifier()) ? audioInfo.getTrack().getIdentifier() : "https://youtube.com/watch?v=" + audioInfo.getTrack().getIdentifier() );
        AudioTrackInfo info = audioInfo.getTrack().getInfo();
        return "**[" + info.title + "](" + trackUrl + ")** by *" + info.author + "* **[" + TrackScheduler.getTimestamp(info.length) + "]**";
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
