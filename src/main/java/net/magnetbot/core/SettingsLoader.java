package net.magnetbot.core;
/*
    Created by nils on 28.04.2018 at 21:59.
    
    (c) nils 2018
*/

import net.magnetbot.utils.Secret;
import net.magnetbot.utils.Static;

import java.io.*;
import java.util.Properties;

public class SettingsLoader {

    private static Properties properties;

    public static void init(File file){
        properties = new Properties();
        try {
            InputStream in = new FileInputStream(file);
            properties.load(in);
            in.close();
            load();
        } catch (IOException e) {
            CLI.error("Settings File '" + file.getName() + "' (" + file.getAbsolutePath() + ") not found");
            fileNotFoundAction(file);
        }
    }

    private static void load(){
        Secret.TOKEN = properties.getProperty("token");
        Secret.TESTING_TOKEN = properties.getProperty("testing_token");
        Secret.DISCORDBOTLIST_TOKEN = properties.getProperty("dbl_token");
        Secret.YOUTUBE_APIKEY = properties.getProperty("yt_apikey");
        Secret.PASTEBIN_APIKEY = properties.getProperty("pb_apikey");
        Secret.SQL_SERVER = properties.getProperty("sql_host");
        Secret.SQL_USER = properties.getProperty("sql_user");
        Secret.SQL_PASSWORD = properties.getProperty("sql_password");
        Secret.SQL_DATABASE = properties.getProperty("sql_database");

        Static.SHARD_ID = Integer.parseInt(properties.getProperty("shard_id"));
        Static.SHARD_COUNT = Integer.parseInt(properties.getProperty("shard_count"));
        CLI.info("Loaded Settings");
    }

    private static void fileNotFoundAction(File file){
        try {
            if(file.createNewFile())
                CLI.success("Created Settings File");

            properties.put("token", "");
            properties.put("testing_token", "");
            properties.put("dbl_token", "");
            properties.put("yt_apikey", "");
            properties.put("pb_apikey", "");
            properties.put("sql_host", "");
            properties.put("sql_user", "");
            properties.put("sql_password", "");
            properties.put("sql_database", "");
            properties.put("shard_id", 1);
            properties.put("shard_count", 1);

            OutputStream out = new FileOutputStream(file);
            properties.store(out, "MagnetBot secret credentials");
            out.close();

            CLI.error("Please set the tokens in the Settings file");

            System.exit(1);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
