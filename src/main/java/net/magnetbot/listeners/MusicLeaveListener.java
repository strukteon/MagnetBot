package net.magnetbot.listeners;
/*
    Created by nils on 21.04.2018 at 17:05.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.MagnetBot;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.audio.TrackScheduler;
import net.magnetbot.core.command.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MusicLeaveListener extends ListenerAdapter {
    private static Map<Guild, Timer> leaveMap = new HashMap<>();

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        int users = 0;
        for (Member m : event.getChannelLeft().getMembers())
            if ( !m.getUser().isBot() && !m.getUser().isFake() && m.getUser().getId().equals(event.getJDA().getSelfUser().getId()))
                users++;
        if ( !event.getMember().getUser().getId().equals(event.getJDA().getSelfUser().getId()) && AudioCore.hasGuildAudioPlayer(event.getGuild()) && users == 0 && AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler.isPlaying(false)
                && event.getGuild().getAudioManager().getConnectedChannel().getId().equals(event.getChannelLeft().getId())) {
            Timer leaveTimer = new Timer();
            leaveTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    TrackScheduler scheduler = AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler;
                    scheduler.getLastEvent().getTextChannel()
                            .sendMessage(Message.INFO("I disconnected from ``" + event.getChannelLeft().getName() + "`` since nobody was listening anymore.").build()).queue();
                    scheduler.stop();
                    leaveMap.remove(event.getGuild());
                }
            }, 1000*60);
            leaveMap.put(event.getGuild(), leaveTimer);
        }
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        if (leaveMap.containsKey(event.getGuild())){
            leaveMap.get(event.getGuild()).cancel();
            leaveMap.remove(event.getGuild());
        }
    }
}
