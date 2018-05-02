package net.magnetbot.listeners;
/*
    Created by nils on 01.04.2018 at 21:00.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.utils.Static;

public class LogListener extends ListenerAdapter {

    private static String LOG_CHANNEL = "388743195476885515";

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        TextChannel t = event.getJDA().getGuildById(Static.SUPPORT_SERVER).getTextChannelById(LOG_CHANNEL);

        Guild g = event.getGuild();
        User owner = g.getOwner().getUser();

        EmbedBuilder b = new EmbedBuilder()
                .setColor(Static.Color.GREEN)
                .setTitle("Added to guild")
                .setThumbnail(g.getIconUrl())
                .setDescription("Name: ``" + g.getName() + "``\n" +
                                "ID: ``" + g.getId() + "``\n" +
                                "Member count: ``" + g.getMembers().size() + "``\n" +
                                "Owner: ``" + owner.getName() + "#" + owner.getDiscriminator() + "``"
                );

        t.sendMessage(b.build()).queue();

    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        TextChannel t = event.getJDA().getGuildById(Static.SUPPORT_SERVER).getTextChannelById(LOG_CHANNEL);

        Guild g = event.getGuild();
        User owner = g.getOwner().getUser();

        EmbedBuilder b = new EmbedBuilder()
                .setColor(Static.Color.ORANGE)
                .setTitle("Removed from guild")
                .setThumbnail(g.getIconUrl())
                .setDescription("Name: ``" + g.getName() + "``\n" +
                                "ID: ``" + g.getId() + "``\n" +
                                "Member count: ``" + g.getMembers().size() + "``\n" +
                                "Owner: ``" + owner.getName() + "#" + owner.getDiscriminator() + "``"
                );

        t.sendMessage(b.build()).queue();
    }
}
