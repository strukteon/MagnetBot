package net.magnetbot.core.command;
/*
    Created by nils on 28.04.2018 at 17:51.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class ParsedCommandEvent {

    private MessageReceivedEvent event;
    private boolean noEmbed;

    public ParsedCommandEvent(MessageReceivedEvent event, boolean noEmbed){
        this.event = event;
        this.noEmbed = noEmbed;
    }

    public ChannelType getChannelType(){
        if (event.getPrivateChannel() != null)
            return ChannelType.PRIVATE;
        return ChannelType.GUILD;
    }


    public static enum ChannelType {
        PRIVATE,
        GUILD
    }

}
