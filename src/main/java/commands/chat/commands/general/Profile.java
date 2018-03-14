package commands.chat.commands.general;
/*
    Created by nils on 01.02.2018 at 20:40.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.UserData;
import core.Main;
import core.tools.AutoComplete;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.UserSQL;

import java.sql.SQLException;
import java.util.List;

public class Profile implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        Member m = AutoComplete.member(event.getGuild().getMembers(), args[0]);
        if (m == null)
            m = event.getMember();
        Main.userData.getUserSQL().createUserIfNotExists("id", m.getUser().getId());

        event.getTextChannel().sendMessage(profile(event, args, m)).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("profile", 0)
                        .setHelp("shows your/someones profile");
    }

    private MessageEmbed profile(MessageReceivedEvent event, String[] args, Member m) throws Exception {

        List<String> userPerms = UserData.getPermissions(event.getAuthor().getId());

        String perms = "";
        for (String s : userPerms){
            if (! perms.equals("") )
                perms += ", ";
            perms += s;
        }
        perms = perms.equals("") ? "none" : perms;

        EmbedBuilder builder = Message.INFO(event);
            builder.setAuthor(m.getEffectiveName() + (!m.getEffectiveName().endsWith("s") ? "'s " : "") + " profile", "https://magnet.strukteon.me/user?userid=" + m.getUser().getId(), m.getUser().getEffectiveAvatarUrl());

            builder.addField(":label: Bio", "``" + UserData.getUser(m.getUser().getId()).get("bio") + "``", false)
                    .addField(":moneybag: Money", "" + UserData.getUser(m.getUser().getId()).get("money") + " m$", true)
                    .addField(":wrench: Permissions", "``" + perms + "``", false);

        return builder.build();
    }
}
