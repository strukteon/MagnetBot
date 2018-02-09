package commands.chat.commands.testing;
/*
    Created by nils on 30.12.2017 at 01:55.
    
    (c) nils 2017
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class Ping implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("ping");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {
        event.getTextChannel().sendMessage(Message.INFO(event, "My ping is: " + event.getJDA().getPing() + "ms").build()).queue();
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
