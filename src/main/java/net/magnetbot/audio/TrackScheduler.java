package net.magnetbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.MagnetBot;
import net.magnetbot.audio.youtube.YouTubeAPI;
import net.magnetbot.core.CLI;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.tools.Tools;
import net.magnetbot.utils.Static;

import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the net.magnetbot.audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioInfo> queue;

    private MessageReceivedEvent lastEvent = null;
    private AudioTrack previousTrack = null;

    public static final int MODE_NORMAL = 0;
    public static final int MODE_REPEAT_ALL = 1;
    public static final int MODE_REPEAT_ONE = 2;
    public static final int MODE_AUTOPLAY = 3;

    // SETTINGS
    private int mode = MODE_NORMAL;



    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public BlockingQueue<AudioInfo> getQueue() {
        return queue;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public MessageReceivedEvent getLastEvent() {
        return lastEvent;
    }

    public AudioInfo getCurrentTrack() {
        return new AudioInfo(player.getPlayingTrack(), lastEvent);
    }

    public AudioTrack getPlayingTrack(){
        return player.getPlayingTrack();
    }

    public TrackScheduler addToQueue(AudioInfo audioInfo, boolean announce){
        queue.add(audioInfo);
        CLI.debug("Track added to queue");
        lastEvent = audioInfo.getEvent();
        CLI.debug(getCurrentTrack());
        CLI.debug(isPlaying(true));
        if (announce)
            audioInfo.getEvent().getChannel().sendMessage(queuedMessage(audioInfo.getTrack(), audioInfo.getEvent())).queue();
        if (!isPlaying(true)) {
            player.startTrack(queue.poll().getTrack(), false);
            CLI.debug("Track starting...");
        }
        return this;
    }

    public TrackScheduler addToQueue(MessageReceivedEvent event, AudioPlaylist playlist, boolean announce){
        playlist.getTracks().forEach(audioTrack -> queue.add(new AudioInfo(audioTrack, event)));
        if (announce)
            event.getChannel().sendMessage(queuedPlaylistMessage(playlist.getTracks().size(), playlist, event)).queue();
        return this;
    }

    public boolean isPaused(){
        return player.isPaused();
    }

    public void pause(){
        player.setPaused(true);
    }

    public void resume(){
        player.setPaused(false);
    }

    public void stop(){
        queue.clear();
        player.destroy();

        CLI.debug("Stopped the queue");
    }

    public void skip(){
        player.startTrack(queue.poll().getTrack(), false);
        CLI.debug("Skipping...");
    }

    public boolean isPlaying(boolean ignorePause){
        return player.getPlayingTrack() != null && (!player.isPaused() || ignorePause);
    }


    public void seek(long time){
        if (getPlayingTrack().isSeekable())
            getPlayingTrack().setPosition(getPlayingTrack().getPosition() + Math.max(0, Math.min(time, getPlayingTrack().getDuration() - getPlayingTrack().getPosition())));
    }

    public void setVolume(int volume){
        player.setVolume(volume);
        CLI.debug("Volume set to");
    }

    public void shuffleQueue(boolean keepCurrent){
        List<AudioInfo> queueTemp = new ArrayList<>(queue);
        if (keepCurrent)
            queueTemp.add(getCurrentTrack());
        Collections.shuffle(queueTemp);
        queue.clear();
        queue.addAll(queueTemp);
        CLI.debug("Shuffled the queue");
    }

    public int getMode() {
        return mode;
    }

    public void setModeNormal(){
        mode = MODE_NORMAL;
    }

    public void setModeRepeatAll(){
        mode = MODE_REPEAT_ALL;
    }

    public void setModeRepeatOne(){
        mode = MODE_REPEAT_ONE;
    }

    public void setModeAutoplay(){
        mode = MODE_AUTOPLAY;
    }


    private MessageEmbed queuedMessage(AudioTrack track, MessageReceivedEvent event){
        return Message.INFO(event, "**Queued:** ***" + track.getInfo().title + "*** in Position **#" + (queue.size() + ( player.getPlayingTrack() != null ?  1 : 0)) + "**").build();
    }

    private MessageEmbed queuedPlaylistMessage(int tracksLength, AudioPlaylist playlist, MessageReceivedEvent event){
        return Message.INFO(event, "**Queued:** Videos of ***" + playlist.getName() + "*** in Position **#" + (queue.size() - tracksLength + ( player.getPlayingTrack() != null ?  2 : 1)) + "**").build();
    }

    private MessageEmbed onTrackStartMessage(){
        AudioTrack track = getPlayingTrack();
        MessageReceivedEvent event = lastEvent;

        String trackUrl = (Tools.isUrl(track.getIdentifier()) ? track.getIdentifier() : "https://youtube.com/watch?v=" + track.getIdentifier() );

        EmbedBuilder builder = Message.INFO(event, "**Now playing:** ***[" + track.getInfo().title + "](" + trackUrl +")*** by *" + track.getInfo().author + "*\n"
                + " [" + getTimestamp(track.getInfo().length) + "]");

        return builder.build();
    }

    public static String getTimestamp(long time){
        time = Long.parseLong("" + (int) Math.floor(time / 1000));
        long hours = Long.parseLong("" + (int) Math.floor(time / 3600));
        time -= hours*3600;
        long minutes = Long.parseLong("" + (int) Math.floor(time / 60));
        time -= minutes*60;
        long seconds = time;
        return (hours > 0 ? hours + ":" + (minutes < 10 ? "0" : "") : "") + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        lastEvent.getTextChannel().sendMessage(onTrackStartMessage()).queue();
        CLI.debug("onTrackStart()");
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        previousTrack = track;
        CLI.debug("onTrackEnd()");
        CLI.debug("Queue size: " + queue.size());
        if (endReason.mayStartNext) {
            if (queue.size() <= 0 && mode == MODE_AUTOPLAY) {
                try {
                    CLI.debug(lastEvent);
                    CLI.debug(previousTrack);
                    com.google.api.services.youtube.model.SearchResult result = YouTubeAPI.relatedVideo(previousTrack.getIdentifier());
                    CLI.debug(result);
                    AudioCore.load(lastEvent, result.getId().getVideoId(), null, false);
                    CLI.debug("mode == AUTOPLAY");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                CLI.debug("May start next track");
                if (queue.size() > 0) {
                    player.startTrack(queue.poll().getTrack(), false);
                    CLI.debug("Next track started");
                }
            }
        } else if (player.getPlayingTrack() == null) {
            CLI.debug("AudioManager#closeConnection");
            AudioCore.disconnectFromVoiceChannel(lastEvent.getGuild().getAudioManager());
        }
    }
}
