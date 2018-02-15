package commands.chat.commands.music;
/*
    Created by nils on 15.02.2018 at 23:31.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Disconnect implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("disconnect");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        Main.audioCore.disconnectFromVoiceChannel(event.getGuild().getAudioManager());
    }

    @Override
    public String premiumPermission() {
        return "premium.music.disconnect";
    }

    @Override
    public int permissionLevel() {
        return 3;
    }
}
