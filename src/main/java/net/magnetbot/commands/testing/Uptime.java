package net.magnetbot.commands.testing;
/*
    Created by nils on 08.04.2018 at 02:14.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.MagnetBot;

public class Uptime implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        long time = (System.currentTimeMillis() - MagnetBot.startTime)/1000;
        long days = time/24/60/60;
        time -= days*24*60*60;
        long hours = time/(60*60);
        time -= hours*60*60;
        long minutes = time/60;
        time -= minutes*60;
        long seconds = time;

        String out = "";
        if (days > 0)
            out += days + "d";
        if (hours > 0){
            if (out.length()>0)
                out += ", ";
            out += hours + "h";
        }
        if (minutes > 0){
            if (out.length()>0)
                out += ", ";
            out += minutes + "m";
        }
        if (seconds > 0){
            if (out.length()>0)
                out += ", ";
            out += seconds + "s";
        }
        event.getTextChannel().sendMessage(Message.INFO(event, "Uptime: ``" + out + "``").build()).queue();


    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("uptime", PermissionLevel.MEMBER)
                .setHelp("shows you the uptime of this bot since the last restart");
    }
}
