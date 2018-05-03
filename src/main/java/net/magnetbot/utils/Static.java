package net.magnetbot.utils;
/*
    Created by nils on 28.12.2017 at 01:57.
    
    (c) nils 2017
*/

import net.magnetbot.core.Importer;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.awt.*;
import java.text.SimpleDateFormat;

public class Static {

    public static final String VERSION = "beta";

    public static final String PREFIX = "m.";

    public static final Game GAME = Game.watching( VERSION + " | " + PREFIX + " help");

    public static final OnlineStatus STATUS = OnlineStatus.ONLINE;

    public static final String BOT_OWNER_ID = "262951897290244096";


    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


    public static final String SUPPORT_SERVER = "332942033398267916";


    public static final String API_BASEURL = "api.magnetbot.net";


    public static final String JAVA_CLASSES = "90";

    public static final String CODE_LINES = "3702";


    public static class Audio {

        public static int PLAYLIST_LIMIT = 15;

    }


    public static class Money {

        public static int VOTE_REWARD = 500;

    }


    public static class Color {
        public static final java.awt.Color BLUE = java.awt.Color.decode("#7289DA");

        public static final java.awt.Color RED = java.awt.Color.decode("#cc2222");

        public static final java.awt.Color GREEN = java.awt.Color.decode("#22cc22");

        public static final java.awt.Color ORANGE = java.awt.Color.decode("#F0B958");

    }

}
