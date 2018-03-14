package commands.chat.commands.general;
/*
    Created by nils on 01.02.2018 at 21:51.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.UserData;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.UserSQL;

public class Bio implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
            if (args.length == 0)
                Message.delAfter(event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m bio <content>").build()), 5000);
            else {
                StringBuilder out = new StringBuilder();
                for (String s : args) {
                    if (out.length() != 0)
                        out.append(" ");
                    out.append(s);
                }

                UserData.updateUser(event.getAuthor().getId(), new UserSQL.Column.Change("bio", out.toString()));

                event.getTextChannel().sendMessage(Message.INFO(event, "Your bio has been set to:\n``" + out.toString() + "``").build()).queue();
            }


    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("bio", 0)
                        .setHelp("set a bio that will be shown in your profile");
    }
}
