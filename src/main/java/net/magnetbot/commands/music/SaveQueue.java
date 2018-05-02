package net.magnetbot.commands.music;
/*
    Created by nils on 02.03.2018 at 17:16.
    
    (c) nils 2018
*/

import net.magnetbot.audio.AudioInfo;
import net.magnetbot.audio.GuildMusicManager;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.MagnetBot;
import net.magnetbot.core.sql.GuildSQL;
import net.magnetbot.core.tools.Tools;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;

public class SaveQueue implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        GuildMusicManager manager = MagnetBot.audioCore.getGuildAudioPlayer(event.getGuild());
        List<AudioInfo> queue = new ArrayList<>(manager.scheduler.getQueue());

        if (queue.size() < 1 && manager.scheduler.getCurrentTrack() == null) {
            event.getTextChannel().sendMessage(Message.ERROR(event, "The current queue is empty!").build()).queue();
        } else {
            List<String> queueList = new ArrayList<>();
            AudioInfo cur = manager.scheduler.getCurrentTrack();
            if (cur != null)
                queueList.add(Tools.isUrl(cur.getTrack().getIdentifier()) ? cur.getTrack().getIdentifier() : "https://youtube.com/watch?v=" + cur.getTrack().getIdentifier());
            for (AudioInfo ai : queue) {
                String identifier = ai.getTrack().getIdentifier();
                if (Tools.isUrl(identifier))
                    queueList.add(identifier);
                else
                    queueList.add("https://youtube.com/watch?v=" + identifier);
            }
            GuildSQL.fromGuild(event.getGuild()).setSavedQueue(queueList);

            event.getTextChannel().sendMessage(Message.INFO(event, "Queue has been saved to the cloud.").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("savequeue", PermissionLevel.SUPPORTER)
                        .setAlias("saveq", "save")
                        .setHelp("save the current queue to the cloud");
    }
}
