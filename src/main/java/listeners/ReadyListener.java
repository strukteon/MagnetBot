package listeners;
/*
    Created by nils on 28.12.2017 at 02:10.
    
    (c) nils 2017
*/

import audio.AudioCore;
import commands.chat.utils.GeneralData;
import commands.chat.utils.GuildData;
import commands.chat.utils.UserData;
import core.Main;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;
import utils.Secret;
import utils.UserSQL;

import java.util.Timer;
import java.util.TimerTask;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        try {

            System.out.println("Logged in as: " + event.getJDA().getSelfUser().getName() + " (" + event.getJDA().getSelfUser().getId() + ")");

            System.out.println("\nCurrent guilds: ");

            for (Guild g : event.getJDA().getGuilds()) {

                System.out.println(g.getName() + " [ " + g.getOwner().getUser().getName() + "#" + g.getOwner().getUser().getDiscriminator() + " ] { "  + g.getMembers().size() + " } (" + g.getId() + ")");

            }


            UserData.init(new UserSQL(UserSQL.login(Secret.SQL_USER, Secret.SQL_PASSWORD, Secret.SQL_DATABASE, Secret.SQL_SERVER)));
            GuildData.init(new UserSQL(UserSQL.login(Secret.SQL_USER, Secret.SQL_PASSWORD, Secret.SQL_DATABASE, Secret.SQL_SERVER)));
            GeneralData.init(new UserSQL(UserSQL.login(Secret.SQL_USER, Secret.SQL_PASSWORD, Secret.SQL_DATABASE, Secret.SQL_SERVER)));

            Main.audioCore = new AudioCore();

            Main.discordBotListAPI = new DiscordBotListAPI.Builder().token(Secret.DISCORDBOTLIST_TOKEN).build();

            Main.discordBotListAPI.setStats(event.getJDA().getSelfUser().getId(), event.getJDA().getGuilds().size());

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    event.getJDA().getPresence().setPresence(Game.listening(event.getJDA().getUsers().size() + " Members | -m help"), true);
                }
            }, 0, 60000);

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
