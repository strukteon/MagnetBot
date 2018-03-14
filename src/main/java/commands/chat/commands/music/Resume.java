package commands.chat.commands.music;
/*
    Created by nils on 08.02.2018 at 01:25.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Resume implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (Main.audioCore.getGuildAudioPlayer(event.getGuild()).scheduler.resume()){
            event.getTextChannel().sendMessage(Message.INFO(event, "Resumed the queue").build()).queue();
        } else {
            event.getTextChannel().sendMessage(Message.ERROR(event, "Queue is already playing").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("resume", 0)
                        .setHelp("resumes the media playback");
    }
}
