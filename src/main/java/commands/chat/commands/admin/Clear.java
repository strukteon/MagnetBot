package commands.chat.commands.admin;
/*
    Created by nils on 07.02.2018 at 18:11.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class Clear implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {

        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(commands.chat.tools.Message.WRONG_SYNTAX(event, "-m clear <msg-amount>").build()).queue();
        else {
            try {
                int num = Integer.parseInt(args[0]);

                MessageHistory history = event.getTextChannel().getHistory();

                List<Message> messages = history.retrievePast(num).complete();

                for (int i = 0; i < messages.size(); i++){
                    if (i != messages.size()-1){
                        messages.get(i).delete().queue();
                    } else {
                        messages.get(i).delete().queue((response) -> event.getTextChannel().sendMessage(commands.chat.tools.Message.INFO(event, "Deleted ``" + messages.size() + "`` Messages").build()).queue());
                    }
                }



                ;

            } catch (NumberFormatException e) {
                event.getTextChannel().sendMessage(commands.chat.tools.Message.WRONG_SYNTAX(event, "-m clear <msg-amount>").build()).queue();
            }

        }


    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("clear", 1)
                        .setHelp("clear a specific amount of messages in the current channel");
    }
}
