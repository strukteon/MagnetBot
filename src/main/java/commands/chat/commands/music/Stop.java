package commands.chat.commands.music;
/*
    Created by nils on 07.02.2018 at 15:00.
    
    (c) nils 2018
*/

import audio.AudioCore;
import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Stop implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (Main.audioCore.stopPlaying(event.getGuild())){
            event.getTextChannel().sendMessage(Message.INFO(event, "Stopped the media playback").build()).queue();
        } else {
            event.getTextChannel().sendMessage(Message.ERROR(event, "Could not stop media playback").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("stop", 1)
                        .setHelp("stops the media playback");
    }
}
