package net.magnetbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import net.magnetbot.core.CLI;
import net.magnetbot.core.command.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioCore extends ListenerAdapter {

    private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    public static void initialize() {
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public static boolean hasGuildAudioPlayer(Guild guild){
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        return musicManager != null;
    }

    public static void connectToVoiceChannel(AudioManager audioManager, VoiceChannel voiceChannel) {
        if ( !audioManager.isConnected() && !audioManager.isAttemptingToConnect() && audioManager.getGuild().getVoiceChannels().contains(voiceChannel))
            audioManager.openAudioConnection(voiceChannel);
    }

    public static void disconnectFromVoiceChannel(AudioManager audioManager) {
        if (audioManager.isConnected() || audioManager.isAttemptingToConnect()) {
            audioManager.closeAudioConnection();
        }
    }

    public static void stop(Guild g){
        if (hasGuildAudioPlayer(g)){
            getGuildAudioPlayer(g).scheduler.stop();
            disconnectFromVoiceChannel(g.getAudioManager());
        }
    }

    public static void load(MessageReceivedEvent event, String trackUrl){
        load(event, trackUrl, null);
    }

    public static void load(MessageReceivedEvent event, String trackUrl, VoiceChannel channel){
        load(event, trackUrl, channel, true);
    }

    public static void load(MessageReceivedEvent event, String trackUrl, VoiceChannel channel, boolean announce){
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                CLI.debug("AudioTrack loaded");
                if (!event.getGuild().getAudioManager().isConnected() && channel != null)
                    connectToVoiceChannel(event.getGuild().getAudioManager(), channel);
                musicManager.scheduler.addToQueue(new AudioInfo(audioTrack, event), announce);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if (!event.getGuild().getAudioManager().isConnected() && channel != null)
                    connectToVoiceChannel(event.getGuild().getAudioManager(), channel);
                musicManager.scheduler.addToQueue(event, audioPlaylist, announce);
            }

            @Override
            public void noMatches() {
                event.getChannel().sendMessage(noMatchesMessage(event, trackUrl)).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.getChannel().sendMessage(loadFailedMessage(event, trackUrl, e)).queue();
            }
        });
    }

    public static void loadMultiple(MessageReceivedEvent event, List<String> trackUrls, VoiceChannel channel){
        for (int i = 0; i < trackUrls.size(); i++) {
            load(event, trackUrls.get(i), channel, i == trackUrls.size() - 1);
        }
    }



    private static MessageEmbed noMatchesMessage(MessageReceivedEvent event, String trackUrl){
        return Message.ERROR(event, "No matches were found for: " + trackUrl).build();
    }

    private static MessageEmbed loadFailedMessage(MessageReceivedEvent event, String trackUrl, FriendlyException exception){
        return Message.ERROR(event, "Could not play: " + exception.getMessage()).build();
    }

    private static MessageEmbed skipTrackMessage(MessageReceivedEvent event){
        return Message.INFO(event, "Skipped to next track.").build();
    }

}
