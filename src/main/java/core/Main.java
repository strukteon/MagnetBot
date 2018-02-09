package core;

/*
    Created by nils on 28.12.2017 at 01:54.
    
    (c) nils 2017
*/

import audio.AudioCore;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.console.core.ConsoleHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import utils.Secret;
import utils.Static;
import utils.UserSQL;

import javax.security.auth.login.LoginException;
import java.sql.ResultSet;
import java.util.Arrays;

public class Main {

    public static JDABuilder builder;
    public static JDA jda;
    public static AudioCore audioCore;

    public static void main(String[] args){
        UserSQL.login(Secret.SQL_USER, Secret.SQL_PASSWORD, Secret.SQL_DATABASE, Secret.SQL_SERVER);

        try {

            builder = new JDABuilder(AccountType.BOT);
            builder.setToken(Secret.TOKEN);
            builder.setAutoReconnect(true);

            builder.setGame(Game.listening( "beta | -m help"));

            builder.setStatus(Static.STATUS);

            Static.addListeners(builder);
            Static.addConsoleCommands();
            Static.addChatCommands();


            jda = builder.buildBlocking();
            audioCore = new AudioCore();

            System.out.println(jda.getSelfUser().getName());



        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
