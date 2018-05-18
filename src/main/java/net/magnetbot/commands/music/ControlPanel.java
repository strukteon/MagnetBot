package net.magnetbot.commands.music;
/*
    Created by nils on 16.05.2018 at 18:49.
    
    (c) nils 2018
*/

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.audio.TrackScheduler;
import net.magnetbot.core.CLI;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;

import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ControlPanel extends ListenerAdapter implements Command {

    private static List<net.dv8tion.jda.core.entities.Message> activePanels = new ArrayList<>();

    private static List<String> actions = new ArrayList<>();

    private static Timer t;

    static {
        actions.add("\uD83D\uDD02");
        actions.add("\uD83D\uDD01");
        actions.add("\u23EA");
        actions.add("\u23EF");
        actions.add("\u23E9");
        actions.add("\u23ED");
        actions.add("\uD83D\uDD00");
        actions.add("\u002A\u20E3");
        actions.add("\uD83D\uDD04");
        actions.add("\u2139");
    }

    public static void startTiming(){
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                activePanels.forEach(message -> {
                    try {
                        message.editMessage(makeMessage(message.getGuild())).complete();
                    } catch (ErrorResponseException e){
                        if (e.getErrorCode() == 10008){
                            activePanels.remove(message);
                        }
                    }
                });
            }
        }, 5000, 5000);
    }

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        if (syntax.getExecutedBuilder() == 0){
            event.getChannel().sendMessage(helpMsg(event.getMember())).queue();
        } else {
            for (net.dv8tion.jda.core.entities.Message msg : activePanels){
                if (msg.getChannel().getId().equals(event.getChannel().getId())) {
                    activePanels.remove(msg);
                    break;
                }
            }
            net.dv8tion.jda.core.entities.Message msg = event.getChannel().sendMessage(makeMessage(event.getGuild())).complete();
            actions.forEach(k -> msg.addReaction(k).queue());
            activePanels.add(msg);
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("cp", PermissionLevel.MEMBER)
                .setHelp("show a control panel to control the music")
                .setSyntax(
                        new SyntaxBuilder(
                                new SyntaxBuilder()
                                        .addSubcommand("help", "help"),
                                new SyntaxBuilder()
                        )
                );
    }


    private static MessageEmbed makeMessage(Guild g){
        EmbedBuilder builder = Message.INFO();
        AudioPlayer player = AudioCore.getGuildAudioPlayer(g).player;
        TrackScheduler scheduler = AudioCore.getGuildAudioPlayer(g).scheduler;
        String mode = scheduler.getMode() != TrackScheduler.MODE_NORMAL ? scheduler.getMode() != TrackScheduler.MODE_REPEAT_ALL ?
                scheduler.getMode() == TrackScheduler.MODE_REPEAT_ONE ? "Repeat current track" : "Autoplay" : "Repeat queue" : "Normal";
        if (player.getPlayingTrack() != null) {
            builder.setDescription("**" + player.getPlayingTrack().getInfo().title + "**\n" + (player.isPaused() ? ":pause_button:" : ":arrow_forward:") + " " + progressBar(player.getPlayingTrack().getPosition(), player.getPlayingTrack().getDuration())
                    + " " + timeStamp(player.getPlayingTrack().getPosition(), player.getPlayingTrack().getDuration()) + " Mode: ``" + mode + "``");
        } else
            builder.setDescription("**No track playing...**\n:stop_button: " + progressBar(0, 10) + " " + timeStamp(0, 0));
        return builder.build();
    }

    private static String timeStamp(long position, long duration){
        return "``[" + TrackScheduler.getTimestamp(position) + " / " + TrackScheduler.getTimestamp(duration) + "]``";
    }


    private static String progressBar(long position, long duration){
        StringBuilder builder = new StringBuilder("\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC");
        builder.insert((int)(10 * position / duration), ":radio_button:");
        return builder.toString();
    }

    private static MessageEmbed helpMsg(Member m){
        return Message.INFO(m)
                .setTitle("Control panel help")
                .setDescription(
                        ":repeat_one:: ``Repeat the current song``\n" +
                                ":repeat:: ``Repeat the queue``\n" +
                                ":rewind:: ``Rewind the song 5 seconds``\n" +
                                ":play_pause:: ``Play/Pause the track``\n" +
                                ":fast_forward:: ``Fast-forward the song 5 seconds``\n" +
                                ":track_next:: ``Skip to the next track``\n" +
                                ":twisted_rightwards_arrows:: ``Shuffle the queue``\n" +
                                ":asterisk:: ``Enable youtube autoplay``\n" +
                                ":arrows_counterclockwise:: ``Disable repeating/autoplay``\n" +
                                ":information_source:: ``Show this help message``").build();
    }


    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        try {
            if (activePanels.contains(event.getChannel().getMessageById(event.getMessageId()).complete()) && !event.getUser().getId().equals(event.getJDA().getSelfUser().getId())) {
                event.getReaction().removeReaction(event.getUser()).queue();
                TrackScheduler scheduler = AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler;

                if (event.getMember().getVoiceState().getChannel() == event.getGuild().getMember(event.getJDA().getSelfUser()).getVoiceState().getChannel())
                    switch (actions.indexOf(event.getReactionEmote().getName())) {

                        case 0:
                            scheduler.setModeRepeatOne();
                            break;

                        case 1:
                            scheduler.setModeRepeatAll();
                            break;

                        case 2:
                            scheduler.seek(-5000);
                            break;

                        case 3:
                            if (scheduler.isPaused())
                                scheduler.resume();
                            else
                                scheduler.pause();
                            break;

                        case 4:
                            scheduler.seek(5000);
                            break;

                        case 5:
                            scheduler.skip();
                            break;

                        case 6:
                            scheduler.shuffleQueue(false);
                            break;

                        case 7:
                            scheduler.setModeAutoplay();
                            break;

                        case 8:
                            scheduler.setModeNormal();
                            break;

                        case 9:
                            event.getChannel().sendMessage(helpMsg(event.getMember())).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                            break;

                    }

            }
        } catch (InsufficientPermissionException e){
            if (e.getPermission() != Permission.MESSAGE_HISTORY)
                CLI.error(e);
        }
    }
}
