package core;

/*
    Created by nils on 28.12.2017 at 01:54.
    
    (c) nils 2017
*/

import audio.AudioCore;
import commands.chat.utils.GuildData;
import commands.chat.utils.UserData;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.discordbots.api.client.DiscordBotListAPI;
import utils.Secret;
import utils.Static;
import utils.UserSQL;

public class Main {

    public static int commandsHandled = 0;

    public static JDABuilder builder;
    public static JDA jda;
    public static AudioCore audioCore;

    public static DiscordBotListAPI discordBotListAPI;
    public static UserData userData;

    public static void main(String[] args){

        boolean testing = false;

        if (args.length > 0)
            if (args[0].equals("testing"))
                testing = true;

        try {

            builder = new JDABuilder(AccountType.BOT);
            builder.setToken(!testing ? Secret.TOKEN : Secret.TESTING_TOKEN);
            builder.setAutoReconnect(true);

            builder.setGame(Game.listening( "beta | -m help"));

            builder.setStatus(Static.STATUS);

            Static.addListeners(builder);
            Static.addConsoleCommands();
            Static.addChatCommands();


            jda = builder.buildBlocking();



        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
