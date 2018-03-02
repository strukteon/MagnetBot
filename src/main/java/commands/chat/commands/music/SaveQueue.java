package commands.chat.commands.music;
/*
    Created by nils on 02.03.2018 at 17:16.
    
    (c) nils 2018
*/

import audio.AudioInfo;
import audio.GuildMusicManager;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.GuildData;
import core.Main;
import core.tools.Tools;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class SaveQueue implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("savequeue") || cmd.equals("saveq") || cmd.equals("save");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        GuildMusicManager manager = Main.audioCore.getGuildAudioPlayer(event.getGuild());
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
            GuildData.setSavedQueue(event.getGuild().getId(), queueList);

            event.getTextChannel().sendMessage(Message.INFO(event, "Queue has been saved to the cloud.").build()).queue();
        }
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 1;
    }
}
