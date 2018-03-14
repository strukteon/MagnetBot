package commands.chat.commands.music;
/*
    Created by nils on 07.02.2018 at 23:04.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Connect implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        Main.audioCore.connectToVoiceChannel(event.getGuild().getAudioManager(), event.getMember().getVoiceState().getChannel());
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("connect", 3)
                        .setHelp("connects this bot with a voicechannel");
    }
}
