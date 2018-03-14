package listeners;
/*
    Created by nils on 28.12.2017 at 02:10.
    
    (c) nils 2017
*/

import audio.AudioCore;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import commands.chat.utils.GeneralData;
import commands.chat.utils.GuildData;
import commands.chat.utils.UserData;
import core.Main;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;
import utils.Secret;
import utils.Static;
import utils.UserSQL;
import utils.UserSqlConnection;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onShutdown(ShutdownEvent event) {
        super.onShutdown(event);
    }

    @Override
    public void onReady(ReadyEvent event) {
        try {

            System.out.println("Logged in as " + event.getJDA().getSelfUser().getName() + " (" + event.getJDA().getSelfUser().getId() + ")");

            System.out.println("\nCurrent guilds: ");

            for (Guild g : event.getJDA().getGuilds()) {

                System.out.println(g.getName() + " [ " + g.getOwner().getUser().getName() + "#" + g.getOwner().getUser().getDiscriminator() + " ] { "  + g.getMembers().size() + " } (" + g.getId() + ")");

            }

            //UserSqlConnection con = new UserSqlConnection(Secret.SQL_USER, Secret.SQL_PASSWORD, Secret.SQL_DATABASE, Secret.SQL_SERVER, 3306);

            UserData.init(new UserSQL(new UserSqlConnection(Secret.SQL_USER, Secret.SQL_PASSWORD, Secret.SQL_DATABASE, Secret.SQL_SERVER, 3306)));
            GuildData.init(new UserSQL(new UserSqlConnection(Secret.SQL_USER, Secret.SQL_PASSWORD, Secret.SQL_DATABASE, Secret.SQL_SERVER, 3306)));
            GeneralData.init(new UserSQL(new UserSqlConnection(Secret.SQL_USER, Secret.SQL_PASSWORD, Secret.SQL_DATABASE, Secret.SQL_SERVER, 3306)));

            Main.audioCore = new AudioCore();

            Main.discordBotListAPI = new DiscordBotListAPI.Builder().token(Secret.DISCORDBOTLIST_TOKEN).build();

            Main.discordBotListAPI.setStats(event.getJDA().getSelfUser().getId(), event.getJDA().getGuilds().size());

            event.getJDA().getUserById(Static.BOT_OWNER_ID).openPrivateChannel().complete().sendMessage("I am now online").queue();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    event.getJDA().getPresence().setPresence(Game.listening(event.getJDA().getUsers().size() + " Members | -m help"), true);
                }
            }, 0, 60000);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        GeneralData.addCommandsHandled(Main.commandsHandled);
                        Main.commandsHandled = 0;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, 60000, 60000);

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
