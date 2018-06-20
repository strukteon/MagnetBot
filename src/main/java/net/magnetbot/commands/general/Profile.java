package net.magnetbot.commands.general;
/*
    Created by nils on 01.02.2018 at 20:40.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.core.sql.UserSQL;

import java.util.List;

public class Profile implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        Member m = syntax.getAsMember("user");
        if (m == null)
            m = event.getMember();

        event.getTextChannel().sendMessage(profile(event, m.getUser())).queue();
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

    private MessageEmbed profile(MessageReceivedEvent event, User u) throws Exception {
        UserSQL userSQL = UserSQL.fromUser(u);
        List<String> userPerms = userSQL.getPermissions();
        StringBuilder badges = new StringBuilder();
        StringBuilder badgeTitles = new StringBuilder();
        userSQL.getBadges().forEach(badge -> {
            badges.append(badge.getEmote().getAsMention());
            badgeTitles.append(badge.getTitle()).append(", ");
        });
        if (badges.length() == 0){
            badges.append(":no_entry_sign:");
            badgeTitles.append("None");
        } else
            badgeTitles.delete(badgeTitles.length()-2, badgeTitles.length());

        String perms = "";
        for (String s : userPerms){
            if (! perms.equals("") )
                perms += ", ";
            perms += s;
        }
        perms = perms.equals("") ? "none" : perms;

        EmbedBuilder builder = Message.INFO(event);
            builder.setAuthor(u.getName() + (u.getName().endsWith("s") ? "'" : "'s") + " profile", "https://magnetbot.net/stats/user?userid=" + u.getId(), u.getEffectiveAvatarUrl());

            builder.addField(":label: Bio", "``" + userSQL.getBio() + "``", false)
                    .addField(":military_medal: Badges: " + badges.toString(), badgeTitles.toString(), false)
                    .addField(":moneybag: Money", "" + userSQL.getMoney() + " m$", true)
                    .addField(":keyboard: Commands", "" + userSQL.getCommandCount(), true)
                    .addField(":wrench: Permissions", "``" + perms + "``", false);


        return builder.build();
    }
}
