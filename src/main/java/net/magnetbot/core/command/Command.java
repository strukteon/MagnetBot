package net.magnetbot.core.command;
/*
    Created by nils on 30.04.2018 at 18:09.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.syntax.Syntax;

public interface Command {

        void action(MessageReceivedEvent event, Syntax syntax) throws Exception;

        Chat.CommandInfo commandInfo(); // banned = -10, guest = -1, member = 0, admin = 1, owner = 2, bot_owner = 3

        default Permission[] requiredBotPerms(){
            return new Permission[0];
        }

        default Permission[] requiredUserPerms(){
            return new Permission[0];
        }
}
