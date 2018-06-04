package net.magnetbot;
/*
    Created by nils on 27.04.2018 at 18:20.
    
    (c) nils 2018
*/
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.core.CLI;
import net.magnetbot.core.Importer;
import net.magnetbot.core.SettingsLoader;
import net.magnetbot.core.sql.MySQL;
import net.magnetbot.utils.Secret;
import net.magnetbot.utils.Static;
import org.discordbots.api.client.DiscordBotListAPI;
import java.io.File;

public class MagnetBot {

    public static DiscordBotListAPI dblAPI;

    public static MySQL mySQL;

    public static boolean isTestBot = false;
    public static AudioCore audioCore = new AudioCore();

    private static JDA jda;

    public static long startTime;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
        startTime = System.currentTimeMillis();

        System.out.println(CLI.BLUE +
                " __    __     ______     ______     __   __     ______     ______  \n" +
                "/\\ \"-./  \\   /\\  __ \\   /\\  ___\\   /\\ \"-.\\ \\   /\\  ___\\   /\\__  _\\ \n" +
                "\\ \\ \\-./\\ \\  \\ \\  __ \\  \\ \\ \\__ \\  \\ \\ \\-.  \\  \\ \\  __\\   \\/_/\\ \\/ \n" +
                " \\ \\_\\ \\ \\_\\  \\ \\_\\ \\_\\  \\ \\_____\\  \\ \\_\\\\\"\\_\\  \\ \\_____\\    \\ \\_\\ \n" +
                "  \\/_/  \\/_/   \\/_/\\/_/   \\/_____/   \\/_/ \\/_/   \\/_____/     \\/_/ \n" +
                "                                                                  ");

        System.err.close();
        System.setErr(System.out);

        if (args.length>0)
            if (args[0].equals("testing"))
                isTestBot = true;

        SettingsLoader.init(new File((isTestBot?"C:\\Users\\nilss\\IdeaProjects\\MagnetBotV2\\testing.props":"/home/magnetbot.properties")));

        JDABuilder builder = new JDABuilder(AccountType.BOT);
        try {
            builder.useSharding(Static.SHARD_ID, Static.SHARD_COUNT);
        } catch (IllegalArgumentException e){
            CLI.error("Could not use sharding, invalid id/count provided");
        }

        builder.setGame(Game.listening("booting sound"));
        builder.setToken(Secret.TOKEN);

        Importer.init();
        Importer.addListeners(builder);
        Importer.importCommands();

        AudioCore.initialize();

        try {
            jda = builder.buildAsync();
        } catch (Exception e) {
            CLI.error(e);
        }


    }

    public static JDA getJDA(){
        return jda;
    }

    private static class ShutdownThread extends Thread {
        @Override
        public void run() {
            MagnetBot.mySQL.disconnect();
        }
    }

}
