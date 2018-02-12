package listeners;
/*
    Created by nils on 11.02.2018 at 22:00.
    
    (c) nils 2018
*/

import core.Main;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;
import utils.Secret;

public class OnGuildChangeListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        updateStats(event.getJDA());
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
