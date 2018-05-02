package net.magnetbot.commands.music;
/*
    Created by nils on 05.02.2018 at 22:22.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.MagnetBot;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;

public class Skip implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        MagnetBot.audioCore.skipTrack(event);
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("skip", PermissionLevel.MEMBER)
                        .setHelp("skips the current track");
    }
}
