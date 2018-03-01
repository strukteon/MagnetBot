package commands.chat.tools;
/*
    Created by nils on 30.01.2018 at 18:52.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Message {

    public static EmbedBuilder INFO(MessageReceivedEvent event,String title, String message){
        EmbedBuilder builder = INFO(event, message);

        builder.setTitle(title);

        return builder;
    }

    public static EmbedBuilder INFO(MessageReceivedEvent event, String message){
        EmbedBuilder builder = INFO(event);

        builder.setDescription(message);

        return builder;
    }

    public static EmbedBuilder INFO(MessageReceivedEvent event){
        return INFO(event.getMember());
    }

    public static EmbedBuilder INFO(Member member){
        EmbedBuilder builder = new EmbedBuilder();

        String name = (member.getNickname() != null ? member.getNickname() + " (" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + ")" : member.getUser().getName() + "#" + member.getUser().getDiscriminator());

        builder.setFooter("Requested by " + name, member.getUser().getEffectiveAvatarUrl());

        builder .setColor(Color.decode("#7289DA"));

        return builder;
    }

    public static EmbedBuilder INFO_RAW(String text){
        EmbedBuilder builder = new EmbedBuilder();

        builder .setColor(Color.decode("#7289DA"))
                .setDescription(text);

        return builder;
    }

    public static EmbedBuilder WRONG_SYNTAX(MessageReceivedEvent event, String help){
        EmbedBuilder builder = INFO(event.getMember());

        builder.setColor(Color.RED)
                .setTitle("**Wrong syntax!**")
                .setDescription("``" + help + "``");

        return builder;
    }

    public static EmbedBuilder ERROR(MessageReceivedEvent event, String errorMsg){
        EmbedBuilder builder = INFO(event.getMember());

        builder.setColor(Color.RED)
                .setTitle("**Error**")
                .setDescription("``" + errorMsg + "``");

        return builder;
    }

    public static EmbedBuilder INTERNAL_ERROR(MessageReceivedEvent event, Exception e){
        EmbedBuilder builder = INFO(event.getMember());

        builder.setColor(Color.RED)
                .setTitle("**An internal error occurred**")
                .setDescription("Sorry, but an internal error occurred\n```" + e.toString() + "```");

        return builder;
    }

    public static void delAfter(RestAction<net.dv8tion.jda.core.entities.Message> message, int millis){
        message.queueAfter(millis, TimeUnit.MILLISECONDS);
    }

}
