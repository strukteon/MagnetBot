package audio;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import commands.chat.commands.music.Playlist;
import commands.chat.tools.Message;
import core.tools.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Emoji;
import utils.Static;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter { 
    private final AudioPlayer player;
    private final BlockingQueue<AudioInfo> queue;

    private AudioInfo current;

    public boolean repeat = false;

    /**
     * @param player The audio player this scheduler uses
     */
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

    public void repeatQueue(boolean repeat){
        this.repeat = repeat;
        if (repeat)
            queue.add(new AudioInfo(current.getTrack().makeClone(), current.getEvent()));
    }

    public void shuffleQueue(){
        List<AudioInfo> _queue = new ArrayList<>(getQueue());
        AudioInfo current = _queue.get(0);
        _queue.remove(current);
        Collections.shuffle(_queue);
        _queue.add(0, current);
        purgeQueue();
        queue.addAll(_queue);
    }

    public void queue(AudioTrack track, MessageReceivedEvent event){
        AudioInfo info = new AudioInfo(track, event);
        queue.add(info);

        event.getTextChannel().sendMessage(queuedMessage(track, event)).queue();

        if (player.getPlayingTrack() == null) {
            current = queue.poll();
            player.playTrack(current.getTrack());
        }
    }

    public void queuePlaylist(AudioPlaylist playlist, MessageReceivedEvent event){
        List<AudioTrack> tracks = playlist.getTracks();
        int tracksLength = (tracks.size() <= Static.Audio.PLAYLIST_LIMIT ? tracks.size() : Static.Audio.PLAYLIST_LIMIT);
        for (int i = 0; i < tracksLength; i++){
            AudioInfo info = new AudioInfo(tracks.get(i), event);
            queue.add(info);
        }

        event.getTextChannel().sendMessage(queuedPlaylistMessage(tracksLength, playlist, event)).queue();

        if (player.getPlayingTrack() == null) {
            current = queue.poll();

            player.playTrack(current.getTrack());
        }

    }

    private boolean isQueueing = false;
    private BlockingQueue<AudioInfo> queueQueue;

    public void startQueueing(){
        isQueueing = true;
        this.queueQueue = new LinkedBlockingQueue<>();
    }

    public boolean isQueueing(){
        return isQueueing;
    }

    public void queueQueue(AudioTrack track, MessageReceivedEvent event, boolean lastTrack){
        if (isQueueing) {

            AudioInfo info = new AudioInfo(track, event);
            queueQueue.add(info);

            if (lastTrack) {
                queue.addAll(queueQueue);
                isQueueing = false;

                event.getTextChannel().sendMessage(Message.INFO(event, Emoji.MUSIC + " Loaded the queue", "The saved queue has been loaded").build()).queue();
                if (player.getPlayingTrack() == null) {
                    current = queue.poll();

                    player.playTrack(current.getTrack());
                }
            }
        }
    }

    public boolean nextTrack(boolean keepInPlaylist){
        if (!queue.isEmpty()) {
            if (keepInPlaylist)
                queue.add(current);

            current = queue.poll();
            player.startTrack(current.getTrack(), false);
            return true;
        } else {
            stop();
            return false;
        }
    }

    public void stop(){
        player.stopTrack();
        current = null;
    }

    public void purgeQueue(){
        queue.clear();
    }

    public boolean pause(){
        if (!player.isPaused()) {
            player.setPaused(true);
            return true;
        }
        return false;
    }

    public boolean resume(){
        if (player.isPaused()) {
            player.setPaused(false);
            return true;
        }
        return false;
    }

    public void setVolume(int volume){
        player.setVolume(volume);
    }

    public boolean isPlaying(boolean ignorePause){
        return player.getPlayingTrack() != null && (!player.isPaused() || ignorePause);
    }

    public AudioInfo getCurrentTrack() {
        return current;
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        current.getEvent().getTextChannel().sendMessage(onTrackStartMessage()).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            Guild g = current.getEvent().getGuild();

            if (queue.isEmpty()) {
                new TrackEndThread(g).start();
                current = null;
            } else {
                current = queue.poll();
                player.playTrack(current.getTrack());
                if (repeat)
                    queue.add(current);
            }
        }
    }

    private MessageEmbed queuedMessage(AudioTrack track, MessageReceivedEvent event){
        return Message.INFO(event, "**Queued:** ***" + track.getInfo().title + "*** in Position **#" + (queue.size() + ( player.getPlayingTrack() != null ?  1 : 0)) + "**").build();
    }

    private MessageEmbed queuedPlaylistMessage(int tracksLength, AudioPlaylist playlist, MessageReceivedEvent event){
        return Message.INFO(event, "**Queued:** Videos of ***" + playlist.getName() + "*** in Position **#" + (queue.size() - tracksLength + ( player.getPlayingTrack() != null ?  2 : 1)) + "**").build();
    }

    private MessageEmbed onTrackStartMessage(){
        AudioTrack track = current.getTrack();
        MessageReceivedEvent event = current.getEvent();

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


    private class TrackEndThread extends Thread {
        private final Guild guild;
        public TrackEndThread(Guild guild){
            this.guild = guild;
        }
        @Override
        public void run() {
            guild.getAudioManager().closeAudioConnection();
            repeat = false;
        }
    }
}
