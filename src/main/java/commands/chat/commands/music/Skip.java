package commands.chat.commands.music;
/*
    Created by nils on 05.02.2018 at 22:22.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Skip implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("skip");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {
        Main.audioCore.skipTrack(event);
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
