package net.magnetbot.listeners;
/*
    Created by nils on 11.02.2018 at 22:00.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.MagnetBot;
import net.magnetbot.utils.Static;

public class OnGuildChangeListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        updateStats(event.getJDA());

        EmbedBuilder builder =
                new EmbedBuilder()
                        .setDescription("***Thank you** for adding me to your server!* You can begin to  ``-m play`` some *music*, or type ``-m help`` to get a *list of every command* available with the bot. You can join our **[Support Server](https://discord.gg/uAT7uUb)** if you have any *questions*. \n" +
                                "If you enjoy my features, check our __**[patreon](https://www.patreon.com/strukteon)**__ out!\nAnyways, have fun using this bot!")
                        .setColor(Static.Color.BLUE);

        event.getGuild().getDefaultChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        updateStats(event.getJDA());
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        updateStats(event.getJDA());
    }

    public static void updateStats(JDA jda){
        MagnetBot.dblAPI.setStats(jda.getSelfUser().getId(), jda.getGuilds().size());
    }
}
