package net.magnetbot.utils;
/*
    Created by nils on 05.04.2018 at 19:41.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.magnetbot.utils.Static;

import javax.security.auth.login.LoginException;
import java.util.Timer;
import java.util.TimerTask;

public class CoolStatus extends Thread {
    Timer timer;
    int cur = 0;
    long period;

    JDA jda;

    String[] msgs = {
            "magnetbot.net",
            "{%GUILDS%} servers!",
            "{%USERS%} users!",
            "patreon.com/strukteon",
            "Much wow",
            "Fun included!"
    };

    public CoolStatus(){
        this(30000);
    }

    public CoolStatus(long period){
        this.timer = new Timer();
        this.period = period;

        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(Secret.TOKEN);
        try {
            this.jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 60000/5);
    }

    private void update(){
        String msg = msgs[cur];
        msg = msg   .replace("{%GUILDS%}", ""+jda.getGuilds().size())
                    .replace("{%USERS%}", ""+jda.getUsers().size());
        jda.getPresence().setPresence(Game.playing(Static.PREFIX + "help | " + msg), false);
        cur++;
        if (cur == msgs.length)
            cur = 0;
    }

    public static class OnlineState {
        int status = 0;
        public OnlineStatus switch_(){
            switch (status){
                case 0:
                    status++;
                    return OnlineStatus.ONLINE;
                case 1:
                    status++;
                    return OnlineStatus.DO_NOT_DISTURB;
                case 2:
                    status = 0;
                    return OnlineStatus.IDLE;
            }
            return OnlineStatus.ONLINE;
        }
    }

}
