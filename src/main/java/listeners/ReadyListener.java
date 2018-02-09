package listeners;
/*
    Created by nils on 28.12.2017 at 02:10.
    
    (c) nils 2017
*/

import audio.AudioCore;
import commands.console.core.EventUpdater;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {

        System.out.println("Logged in as: " + event.getJDA().getSelfUser().getName() + " (" + event.getJDA().getSelfUser().getId() + ")");

        System.out.println("\nCurrent guilds: ");

        for (Guild g : event.getJDA().getGuilds()) {

            System.out.println(g.getName() + " (" + g.getId() + ")");

        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                long playercount = 0;

                for (Guild g : event.getJDA().getGuilds()) {

                    playercount += g.getMembers().size();
                }

                event.getJDA().getPresence().setGame(Game.listening(playercount + " Members | -m help"));
            }
        }, 0, 60000);

    }
}
