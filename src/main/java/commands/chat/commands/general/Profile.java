package commands.chat.commands.general;
/*
    Created by nils on 01.02.2018 at 20:40.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.tools.AutoComplete;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.UserSQL;

import java.sql.SQLException;

public class Profile implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("profile");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {
        Member m = AutoComplete.member(event.getGuild().getMembers(), args[0]);
        if (m == null)
            m = event.getMember();
        if (!UserSQL.userExists(m.getUser().getId()))
            UserSQL.createUser(m.getUser().getId());

        event.getTextChannel().sendMessage(profile(event, args, m)).queue();
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    private MessageEmbed profile(MessageReceivedEvent event, String[] args, Member m){


        EmbedBuilder builder = Message.INFO(event);
            builder.setAuthor(m.getEffectiveName() + (!m.getEffectiveName().endsWith("s") ? "'s " : "") + " profile", "https://magnet.strukteon.me/user?userid=" + m.getUser().getId(), m.getUser().getEffectiveAvatarUrl());

            builder.addField(":label: Bio", "``" + UserSQL.getBio(m.getUser().getId()) + "``", false)
                    .addField(":moneybag: Lodestones", "" + UserSQL.getMoney(m.getUser().getId()), true);

        return builder.build();
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
