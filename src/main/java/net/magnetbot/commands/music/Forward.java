package net.magnetbot.commands.music;
/*
    Created by nils on 15.05.2018 at 18:43.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.MagnetBot;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.audio.TrackScheduler;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;

public class Forward implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        TrackScheduler scheduler = AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler;
        if (scheduler.isPlaying(true)){

            long time = syntax.getAsInt("seconds") * 1000;

            if (syntax.getExecutedBuilder() <= 1)
                time += syntax.getAsInt("minutes") * 60 * 1000;
            if (syntax.getExecutedBuilder() == 0)
                time += syntax.getAsInt("hours") * 60 * 60 * 1000;


            scheduler.seek(time);

            event.getChannel().sendMessage(Message.INFO(event, "New position: " + TrackScheduler.getTimestamp(scheduler.getPlayer().getPlayingTrack().getPosition())).build()).queue();

        } else
            event.getTextChannel().sendMessage(Message.ERROR(event, "No track is playing").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("forward", PermissionLevel.MEMBER)
                .setHelp("go forward in the current playing track")
                .setSyntax(
                        new SyntaxBuilder(
                                new SyntaxBuilder()
                                        .addElement("hours", SyntaxElementType.INT)
                                        .addElement("minutes", SyntaxElementType.INT)
                                        .addElement("seconds", SyntaxElementType.INT),

                                new SyntaxBuilder()
                                        .addElement("minutes", SyntaxElementType.INT)
                                        .addElement("seconds", SyntaxElementType.INT),

                                new SyntaxBuilder()
                                        .addElement("seconds", SyntaxElementType.INT)
                                )
                );
    }
}
