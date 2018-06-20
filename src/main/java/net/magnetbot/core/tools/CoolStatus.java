package net.magnetbot.core.tools;
/*
    Created by nils on 05.04.2018 at 19:41.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.magnetbot.utils.Static;

import java.util.Timer;
import java.util.TimerTask;

public class CoolStatus extends Thread {
    private Timer timer;
    private int cur = 0;
    private long period;

    private JDA jda;

    private String[] msgs = {
            "magnetbot.net",
            "{%GUILDS%} servers!",
            "{%USERS%} users!",
            "patreon.com/strukteon"
    };

    public CoolStatus(JDA jda){
        this(jda, 30000);
    }

    public CoolStatus(JDA jda, long period){
        this.timer = new Timer();
        this.period = period;
        this.jda = jda;
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
}
