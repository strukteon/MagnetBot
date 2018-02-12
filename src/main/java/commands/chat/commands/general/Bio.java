package commands.chat.commands.general;
/*
    Created by nils on 01.02.2018 at 21:51.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.UserData;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.UserSQL;

public class Bio implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("bio");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
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
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
