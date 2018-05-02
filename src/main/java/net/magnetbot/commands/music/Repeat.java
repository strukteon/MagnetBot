package net.magnetbot.commands.music;
/*
    Created by nils on 17.02.2018 at 22:19.
    
    (c) nils 2018
*/

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
        TrackScheduler scheduler = MagnetBot.audioCore.getGuildAudioPlayer(event.getGuild()).scheduler;

        if (scheduler.getQueue().size() == 0 && scheduler.getCurrentTrack() == null){
            event.getTextChannel().sendMessage(Message.ERROR(event, "Queue is empty!").build()).queue();
        } else {

            if (scheduler.repeat){
                scheduler.repeatQueue(false);
                event.getTextChannel().sendMessage(Message.INFO(event, "The current queue is no longer repeating!").build()).queue();
            } else {
                scheduler.repeatQueue(true);
                event.getTextChannel().sendMessage(Message.INFO(event, "The current queue is now repeating!").build()).queue();
            }

        }

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("loop", PermissionLevel.MEMBER)
                        .setAlias("repeat")
                //        .setPremium("premium.music.loop")
                        .setHelp("turn looping for the queue on/off");
    }
}
