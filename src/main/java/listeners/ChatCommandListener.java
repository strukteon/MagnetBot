package listeners;
/*
    Created by nils on 30.12.2017 at 01:45.
    
    (c) nils 2017
*/

import commands.chat.core.ChatHandler;
import commands.chat.utils.GuildData;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.Static;

public class ChatCommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentRaw().trim();

        String prefix = getPrefix(event, msg);
        if (!event.getAuthor().isBot() && !event.getAuthor().isFake() && prefix != null) {
            String trimmed = msg.toLowerCase().replaceFirst(prefix, "").trim();
            ChatHandler.handleInput(event, trimmed);

        } else if (!event.getAuthor().isBot() && (msg.equals("@"+event.getJDA().getSelfUser().getName()) || msg.equals("@"+event.getGuild().getMember(event.getJDA().getSelfUser()).getNickname())) ){

            PrivateChannel channel = event.getAuthor().openPrivateChannel().complete();

            channel.sendMessage("My Prefix is ``" + prefix + "``, please use that for commands").queue();

        }
    }

    private String getPrefix(MessageReceivedEvent event, String msg){
        msg = msg.toLowerCase();
        if (msg.startsWith(Static.PREFIX))
            return Static.PREFIX;
        else
            try {
                String prefix = GuildData.getPrefix(event.getGuild().getId());
                if (msg.startsWith(prefix))
                    return prefix;
            } catch (Exception e){
                e.printStackTrace();
            }

        return null;
    }
}
