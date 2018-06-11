package net.magnetbot.commands.music;
/*
    Created by nils on 15.02.2018 at 23:31.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.Permission;
import net.magnetbot.audio.AudioCore;import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;

public class Leave implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        AudioCore.disconnectFromVoiceChannel(event.getGuild().getAudioManager());
    }

    @Override
    public Permission[] requiredUserPerms() {
        return new Join().requiredUserPerms();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("leave", PermissionLevel.SUPPORTER)
                        .setHelp("disconnects this bot from a voicechannel");
    }
}
