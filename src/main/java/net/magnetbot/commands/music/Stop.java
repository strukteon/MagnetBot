package net.magnetbot.commands.music;
/*
    Created by nils on 07.02.2018 at 15:00.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.audio.TrackScheduler;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;

public class Stop implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        TrackScheduler scheduler = AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler;
        if (scheduler.isPlaying(true)){
            AudioCore.stop(event.getGuild());
            event.getTextChannel().sendMessage(Message.INFO(event, "Stopped the media playback").build()).queue();
        } else {
            event.getTextChannel().sendMessage(Message.ERROR(event, "There is no song playing.").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("stop", PermissionLevel.MEMBER)
                        .setHelp("stops the media playback");
    }
}
