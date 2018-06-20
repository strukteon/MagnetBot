package net.magnetbot.commands.music;
/*
    Created by nils on 07.02.2018 at 23:04.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;

public class Join implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        AudioCore.connectToVoiceChannel(event.getGuild().getAudioManager(), event.getMember().getVoiceState().getChannel());
    }

    @Override
    public Permission[] requiredUserPerms() {
        return new Permission[]{Permission.VOICE_MOVE_OTHERS};
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("join", PermissionLevel.SUPPORTER)
                        .setHelp("connects this bot with a voicechannel");
    }
}
