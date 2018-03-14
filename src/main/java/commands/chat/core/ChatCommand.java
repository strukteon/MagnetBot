package commands.chat.core;
/*
    Created by nils on 30.12.2017 at 01:36.
    
    (c) nils 2017
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface ChatCommand {

    void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception;

    Chat.CommandInfo commandInfo(); // banned = -10, guest = -1, member = 0, admin = 1, owner = 2, bot_owner = 3
}
