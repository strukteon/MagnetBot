package commands.chat.commands.music;
/*
    Created by nils on 17.02.2018 at 22:19.
    
    (c) nils 2018
*/

import audio.AudioInfo;
import audio.TrackScheduler;
import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.BlockingQueue;

public class Repeat implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        TrackScheduler scheduler = Main.audioCore.getGuildAudioPlayer(event.getGuild()).scheduler;

        if (scheduler.getQueue().size() == 0 && scheduler.getCurrentTrack() == null){
            event.getTextChannel().sendMessage(Message.ERROR(event, "Queue is empty!").build()).queue();
        } else {

            if (scheduler.repeat){
                scheduler.repeatQueue(false);
                event.getTextChannel().sendMessage(Message.INFO(event, "The current queue is no longer repeating!").build()).queue();
            } else {
                scheduler.repeatQueue(true);
                event.getTextChannel().sendMessage(Message.INFO(event, "The current queue is now repeating!").build()).queue();
            }

        }

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("loop", 0)
                        .setAlias("repeat")
                        .setPremium("premium.music.loop")
                        .setHelp("turn looping for the queue on/off");
    }
}
