package listeners;
/*
    Created by nils on 11.02.2018 at 22:00.
    
    (c) nils 2018
*/

import core.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;
import utils.Secret;
import utils.Static;

public class OnGuildChangeListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        updateStats(event.getJDA());

        EmbedBuilder builder =
                new EmbedBuilder()
                .setDescription("***Thank you** for adding me to your server!* You can begin to  ``-m play`` some *music*, or type ``-m help`` to get a *list of every command* available with the bot. You can join our **[Support Server](https://discord.gg)** if you have any *questions*. Anyways, have fun using this bot!")
                .setColor(Static.Color.DISCORD_COLOR);

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
        Main.discordBotListAPI.setStats(jda.getSelfUser().getId(), jda.getGuilds().size());
    }
}
