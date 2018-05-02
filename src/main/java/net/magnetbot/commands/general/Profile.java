package net.magnetbot.commands.general;
/*
    Created by nils on 01.02.2018 at 20:40.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.sql.UserSQL;

import java.util.List;

public class Profile implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        Member m = syntax.getAsMember("user");
        if (m == null)
            m = event.getMember();

        event.getTextChannel().sendMessage(profile(event, m)).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("profile", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addOptionalElement("user", SyntaxElementType.MEMBER)
                        )
                        .setHelp("shows your/someones profile");
    }

    private MessageEmbed profile(MessageReceivedEvent event, Member m) throws Exception {
        UserSQL userSQL = UserSQL.fromUser(event.getAuthor());
        List<String> userPerms = userSQL.getPermissions();

        String perms = "";
        for (String s : userPerms){
            if (! perms.equals("") )
                perms += ", ";
            perms += s;
        }
        perms = perms.equals("") ? "none" : perms;

        EmbedBuilder builder = Message.INFO(event);
            builder.setAuthor(m.getEffectiveName() + (!m.getEffectiveName().endsWith("s") ? "'s " : "") + " profile", "https://magnetbot.net/user?userid=" + m.getUser().getId(), m.getUser().getEffectiveAvatarUrl());

            builder.addField(":label: Bio", "``" + userSQL.getBio() + "``", false)
                    .addField(":moneybag: Money", "" + userSQL.getMoney() + " m$", true)
                    .addField(":wrench: Permissions", "``" + perms + "``", false);


        return builder.build();
    }
}
