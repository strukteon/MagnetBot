package commands.chat.commands.general;
/*
    Created by nils on 06.01.2018 at 15:04.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Invite implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("invite");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {
        event.getTextChannel().sendMessage(Message.INFO(event, "You can invite me with the following link: https://magnet.strukteon.me/invite").build()).queue();
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
