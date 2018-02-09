package commands.chat.commands.general;
/*
    Created by nils on 31.12.2017 at 02:24.
    
    (c) nils 2017
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class Server implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("server");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {

        OffsetDateTime _time = event.getGuild().getCreationTime();
        String crtime = _time.getDayOfMonth() + ". " + _time.getMonth().getValue() + ". " + _time.getYear() + " " + _time.getHour() + ":" + _time.getMinute();

        int bots = 0;
        for (Member m : event.getGuild().getMembers())
            if (m.getUser().isBot())
                bots++;

        EmbedBuilder builder = Message.INFO(event);
        builder .setThumbnail(event.getGuild().getIconUrl())
                .addField("Name", event.getGuild().getName(), true)
                .addField("ID", event.getGuild().getId(), true)
                .addField("Members", ""+event.getGuild().getMembers().size(), true)
                .addField("Bots", ""+bots, true)
                .addField("Pending invites", ""+event.getGuild().getInvites().complete().size(), true)
                .addField("Bans", ""+event.getGuild().getBans().complete().size(), true)
                .addField("Server Owner", event.getGuild().getOwner().getAsMention(), true)
                .addField("Created", crtime, true)
                .addField("Region", event.getGuild().getRegion().getName(), true)
                .addField("Security level", event.getGuild().getVerificationLevel().name().toLowerCase(), true);

        event.getTextChannel().sendMessage(builder.build()).queue();

    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
