package commands.chat.commands.general;
/*
    Created by nils on 01.02.2018 at 20:40.
    
    (c) nils 2018
*/

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

public class Profile implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("profile");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        Member m = AutoComplete.member(event.getGuild().getMembers(), args[0]);
        if (m == null)
            m = event.getMember();
        Main.userData.getUserSQL().createUserIfNotExists("id", m.getUser().getId());

        event.getTextChannel().sendMessage(profile(event, args, m)).queue();
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    private MessageEmbed profile(MessageReceivedEvent event, String[] args, Member m) throws Exception {


        EmbedBuilder builder = Message.INFO(event);
            builder.setAuthor(m.getEffectiveName() + (!m.getEffectiveName().endsWith("s") ? "'s " : "") + " profile", "https://magnet.strukteon.me/user?userid=" + m.getUser().getId(), m.getUser().getEffectiveAvatarUrl());

            builder.addField(":label: Bio", "``" + UserData.getUser(m.getUser().getId()).get("bio") + "``", false)
                    .addField(":moneybag: Money", "" + UserData.getUser(m.getUser().getId()).get("money") + " m$", true);

        return builder.build();
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
