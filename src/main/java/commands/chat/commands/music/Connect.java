package commands.chat.commands.music;
/*
    Created by nils on 07.02.2018 at 23:04.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Connect implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("connect");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        Main.audioCore.connectToVoiceChannel(event.getGuild().getAudioManager(), event.getMember().getVoiceState().getChannel());
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 3;
    }
}
