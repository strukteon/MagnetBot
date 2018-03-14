package commands.chat.commands.general;
/*
    Created by nils on 06.01.2018 at 15:04.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Invite implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        event.getTextChannel().sendMessage(Message.INFO(event, "You can invite me with the following link: https://magnet.strukteon.me/redirect?rel=invite").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("invite", 0)
                        .setHelp("gives you the invite link for this bot");
    }
}
