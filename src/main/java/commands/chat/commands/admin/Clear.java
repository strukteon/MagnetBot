package commands.chat.commands.admin;
/*
    Created by nils on 07.02.2018 at 18:11.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class Clear implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("clear");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {

        if (args.length == 1 && args[0] == "")
            event.getTextChannel().sendMessage(commands.chat.tools.Message.WRONG_SYNTAX(event, "-m clear <msg-amount>").build()).queue();
        else {
            
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
