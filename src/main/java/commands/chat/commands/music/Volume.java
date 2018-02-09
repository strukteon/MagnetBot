package commands.chat.commands.music;
/*
    Created by nils on 08.02.2018 at 01:29.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Volume implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("volume");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m volume <percent>").build()).queue();
        else {
            Main.audioCore.getGuildAudioPlayer(event.getGuild()).scheduler.setVolume(Integer.parseInt(args[0]));
            event.getTextChannel().sendMessage(Message.INFO(event, "Volume set to " + Integer.parseInt(args[0]) + "%").build()).queue();
        }
    }

    @Override
    public String premiumPermission() {
        return "music.volume";
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
