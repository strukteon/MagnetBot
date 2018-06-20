package net.magnetbot.listeners;
/*
    Created by nils on 27.04.2018 at 18:25.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.core.command.CommandHandler;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.sql.GuildSQL;
import net.magnetbot.utils.Static;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot())
            if (event.isFromType(ChannelType.TEXT))
                guildHandler(event);
    }

    void guildHandler(MessageReceivedEvent event){
        String prefix = getGuildPrefix(event);
        if (event.getMessage().getContentRaw().trim().startsWith(prefix)) {
            CommandHandler.handleInput(event, event.getMessage().getContentRaw().replaceFirst(prefix, ""));
        } else if (event.getMessage().getContentRaw().startsWith(event.getJDA().getSelfUser().getAsMention())){
            event.getTextChannel().sendMessage(Message.INFO(event, "My prefix is ``" + prefix + "``, please use that for commands! Type ``" + prefix + "help`` to get a list of every command available.\n" +
                    "If you need any help, join our [Support Server](https://discord.gg/uAT7uUb)!").build()).queue();
        }
    }

    private String getGuildPrefix(MessageReceivedEvent event){
        GuildSQL guildSQL = GuildSQL.fromGuild(event.getGuild());
        String prefix = guildSQL.getPrefix();
        if (!prefix.equals(""))
            return prefix;
        return Static.PREFIX;
    }

}
