package commands.chat.commands.music;
/*
    Created by nils on 08.02.2018 at 18:19.
    
    (c) nils 2018
*/

import audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Info implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        TrackScheduler scheduler = Main.audioCore.getGuildAudioPlayer(event.getGuild()).scheduler;
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
                new Chat.CommandInfo("info", 0)
                        .setHelp("shows info about the playing track");
    }
}
