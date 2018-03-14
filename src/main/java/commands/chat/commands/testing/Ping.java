package commands.chat.commands.testing;
/*
    Created by nils on 30.12.2017 at 01:55.
    
    (c) nils 2017
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class Ping implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        event.getTextChannel().sendMessage(Message.INFO(event, "My ping is: " + event.getJDA().getPing() + "ms").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("ping", 0)
                        .setHelp("gives you the connection speed from the bot to discord");
    }
}
