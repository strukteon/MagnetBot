package commands.chat.commands.music;
/*
    Created by nils on 17.02.2018 at 22:19.
    
    (c) nils 2018
*/

import audio.AudioInfo;
import audio.TrackScheduler;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.BlockingQueue;

public class Repeat implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("repeat") || cmd.equals("Repeat");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        TrackScheduler scheduler = Main.audioCore.getGuildAudioPlayer(event.getGuild()).scheduler;

        if (scheduler.getQueue().size() == 0){
            event.getTextChannel().sendMessage(Message.ERROR(event, "Queue is empty!").build()).queue();
        } else {

            if (scheduler.repeat){
                scheduler.repeatQueue(false);
                event.getTextChannel().sendMessage(Message.INFO(event, "The current queue is now repeating!").build()).queue();
            } else {
                scheduler.repeatQueue(true);
                event.getTextChannel().sendMessage(Message.INFO(event, "The current queue is no longer repeating!").build()).queue();
            }

        }

    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
