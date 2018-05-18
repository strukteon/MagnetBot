package net.magnetbot.listeners;
/*
    Created by nils on 28.04.2018 at 22:42.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.MagnetBot;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.commands.music.ControlPanel;
import net.magnetbot.core.CLI;
import net.magnetbot.core.sql.*;
import net.magnetbot.utils.CoolStatus;
import net.magnetbot.utils.Secret;
import org.discordbots.api.client.DiscordBotListAPI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException e) { }
        JDA jda = event.getJDA();
        CLI.info("Logged in as " + jda.getSelfUser().getName() + "#" + jda.getSelfUser().getDiscriminator());

        CLI.info(jda.getGuilds().size() + " Guilds");

        int users = 0;
        for (Guild g : jda.getGuilds())
            users += g.getMembers().size();
        long start = System.currentTimeMillis();
        List<String> user_s = new ArrayList<>();
        for (Guild g : jda.getGuilds())
            g.getMembers().forEach(member -> {
                if (!user_s.contains(member.getUser().getId()) && !member.getUser().isBot())
                    user_s.add(member.getUser().getId());
            });
        System.out.println("wow it only took " + (System.currentTimeMillis()-start) + "ms");

        CLI.info("("+users+") "+user_s.size() + " Users, " + jda.getUsers().size() + " Online (" + Math.round(jda.getUsers().size()/(float)users*100) + "%)");

        new CoolStatus(jda).start();
        CoolStatus.OnlineState onlineState = new CoolStatus.OnlineState();

        if (MagnetBot.isTestBot)
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    jda.getPresence().setPresence(onlineState.switch_(), false);
                }
            }, 0, 1000);

        //if (!MagnetBot.isTestBot) {
            MagnetBot.dblAPI = new DiscordBotListAPI.Builder().token(Secret.DISCORDBOTLIST_TOKEN).build();
            MagnetBot.dblAPI.setStats(jda.getSelfUser().getId(), jda.getGuilds().size());

            MagnetBot.mySQL = new MySQL(Secret.SQL_SERVER, 3306, Secret.SQL_USER, Secret.SQL_PASSWORD, Secret.SQL_DATABASE);
            MagnetBot.mySQL.connect();

            UserSQL.init(MagnetBot.mySQL);
            GuildSQL.init(MagnetBot.mySQL);
            PremiumSQL.init(MagnetBot.mySQL);
            PollSQL.init(MagnetBot.mySQL);
        //}


        ControlPanel.startTiming();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Connection temp = MagnetBot.mySQL.getConnection();
                MagnetBot.mySQL.connect();
                try {
                    temp.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 500*1000, 500*1000);

        new CoolStatus(event.getJDA()).start();

    }
}
