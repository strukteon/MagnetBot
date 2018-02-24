package listeners;
/*
    Created by nils on 30.12.2017 at 01:45.
    
    (c) nils 2017
*/

import commands.chat.core.ChatHandler;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.Static;

public class ChatCommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentRaw().trim();

        if (!event.getAuthor().isBot() && !event.getAuthor().isFake() && msg.toLowerCase().startsWith(Static.PREFIX)) {
            String trimmed = msg.toLowerCase().replaceFirst(Static.PREFIX, "").trim();
            ChatHandler.handleInput(event, trimmed);

        } else if (!event.getAuthor().isBot() && (msg.equals("@"+event.getJDA().getSelfUser().getName()) || msg.equals("@"+event.getGuild().getMember(event.getJDA().getSelfUser()).getNickname())) ){

            PrivateChannel channel = event.getAuthor().openPrivateChannel().complete();

            channel.sendMessage("My Prefix is ``" + Static.PREFIX + "``, please use that for commands").queue();

        }
    }
}
