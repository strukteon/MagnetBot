package commands.chat.commands.testing;
/*
    Created by nils on 30.01.2018 at 18:24.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Error implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("error");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++){
            if (i != 0)
                builder.append(" ");
            builder.append(args[i]);
        }
        event.getTextChannel().sendMessage(Message.ERROR(event, builder.toString()).setTitle("**Custom Error**").build()).queue();
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
