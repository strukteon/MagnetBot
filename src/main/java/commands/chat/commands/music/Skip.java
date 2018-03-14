package commands.chat.commands.music;
/*
    Created by nils on 05.02.2018 at 22:22.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Skip implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        Main.audioCore.skipTrack(event);
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("skip", 0)
                        .setHelp("skips the current track");
    }
}
