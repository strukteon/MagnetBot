package net.magnetbot.commands.music;
/*
    Created by nils on 08.02.2018 at 01:25.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.MagnetBot;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;

public class Resume implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        if (MagnetBot.audioCore.getGuildAudioPlayer(event.getGuild()).scheduler.resume()){
            event.getTextChannel().sendMessage(Message.INFO(event, "Resumed the queue").build()).queue();
        } else {
            event.getTextChannel().sendMessage(Message.ERROR(event, "Queue is already playing").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("resume", PermissionLevel.MEMBER)
                        .setHelp("resumes the media playback");
    }
}
