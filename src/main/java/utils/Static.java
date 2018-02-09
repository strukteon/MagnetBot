package utils;
/*
    Created by nils on 28.12.2017 at 01:57.
    
    (c) nils 2017
*/

import core.Importer;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class Static {

    public static final String VERSION = "beta";

    public static final String PREFIX = "-m ";

    public static final Game GAME = Game.watching( VERSION + " | " + PREFIX + " help");

    public static final OnlineStatus STATUS = OnlineStatus.ONLINE;

    public static final String BOT_OWNER_ID = "262951897290244096";



    public static void addListeners(JDABuilder builder){
        Importer.addListeners(builder);
    }

    public static void addConsoleCommands(){
        Importer.importConsoleCommands();
    }

    public static void addChatCommands(){
        Importer.importChatCommands();
    }

    //Firebase
    public static final String USER_PATH = "users";
    public static final String SERVER_PATH = "servers";


    public static class Audio {

        public static int PLAYLIST_LIMIT = 15;

    }

}
