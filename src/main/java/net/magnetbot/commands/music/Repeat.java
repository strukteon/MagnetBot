package net.magnetbot.commands.music;
/*
    Created by nils on 17.02.2018 at 22:19.
    
    (c) nils 2018
*/

import net.magnetbot.audio.AudioCore;
import net.magnetbot.audio.TrackScheduler;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.MagnetBot;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;

public class Repeat implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        TrackScheduler scheduler = AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler;

        if (scheduler.getQueue().size() == 0 && scheduler.getCurrentTrack() == null){
            event.getTextChannel().sendMessage(Message.ERROR(event, "Queue is empty!").build()).queue();
        } else {

            if (scheduler.getMode() == syntax.getAsSubCommand("which").getSelection()) {
                event.getChannel().sendMessage(Message.INFO(event, "The queue is already doing that!").build()).queue();
                return;
            }

            switch (syntax.getAsSubCommand("which").getSelection()){
                case 0:
                    scheduler.setModeNormal();
                    event.getChannel().sendMessage(Message.INFO(event, "Queue is now playing normally!").build()).queue();
                    break;
                case 1:
                    scheduler.setModeRepeatAll();
                    event.getChannel().sendMessage(Message.INFO(event, "Queue is now repeating!").build()).queue();
                    break;
                case 2:
                    scheduler.setModeRepeatOne();
                    event.getChannel().sendMessage(Message.INFO(event, "The current track is now repeating!").build()).queue();
                    break;
                case 3:
                    scheduler.setModeAutoplay();
                    event.getChannel().sendMessage(Message.INFO(event, "The queue is now on autoplay!").build()).queue();
                    break;
            }

        }

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("mode", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addSubcommand("which", "none", "repeatall", "repeatthis", "autoplay")

                        )
                //        .setPremium("premium.music.loop")
                        .setHelp("turn looping for the queue on/off");
    }
}
