package commands.chat.commands.admin;
/*
    Created by nils on 19.02.2018 at 22:27.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.GuildData;
import core.tools.Tools;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Prefix implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals("")){
            String prefix = GuildData.getPrefix(event.getGuild().getId());
            if (prefix.equals(""))
                event.getTextChannel().sendMessage(Message.INFO(event, "There is no custom prefix set for this server.\nYou can set one with ``-m prefix <prefix>``").build()).queue();
            else
                event.getTextChannel().sendMessage(Message.INFO(event, "The custom prefix set on this server is: ``" + prefix + Character.toString((char)8199) + "``").build()).queue();
        }
        else {
            String prefix = Tools.argsToString(args, " ");
            GuildData.setPrefix(event.getGuild().getId(), prefix);
            event.getTextChannel().sendMessage(Message.INFO(event, "The custom prefix has been set to: ``" + prefix + "``").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("prefix", 1)
                        .setHelp("set a custom prefix for this server");
    }
}
