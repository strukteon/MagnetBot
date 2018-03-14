package commands.chat.commands.music;
/*
    Created by nils on 15.02.2018 at 23:31.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Disconnect implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        Main.audioCore.disconnectFromVoiceChannel(event.getGuild().getAudioManager());
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("disconnect", 3)
                        .setHelp("disconnects this bot from a voicechannel");
    }
}
