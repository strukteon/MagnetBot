package net.magnetbot.commands.music;
/*
    Created by nils on 07.02.2018 at 23:04.
    
    (c) nils 2018
*/

import net.magnetbot.MagnetBot;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.core.command.PermissionLevel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;

public class Join implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        AudioCore.connectToVoiceChannel(event.getGuild().getAudioManager(), event.getMember().getVoiceState().getChannel());
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("join", PermissionLevel.SUPPORTER)
                        .setHelp("connects this bot with a voicechannel");
    }
}
