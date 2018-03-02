package commands.chat.commands.music;
/*
    Created by nils on 02.03.2018 at 17:14.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.GuildData;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class LoadQueue implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("loadqueue") || cmd.equals("loadq") || cmd.equals("load");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        List<String> queueList = GuildData.getSavedQueue(event.getGuild().getId());
        if (queueList.size() < 1)
            event.getTextChannel().sendMessage(Message.ERROR(event, "The saved queue is empty.").build()).queue();
        else {
            Main.audioCore.loadQueue(event, queueList);
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
