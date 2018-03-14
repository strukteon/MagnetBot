package core;
/*
    Created by nils on 28.12.2017 at 15:36.
    
    (c) nils 2017
*/

import audio.AudioCore;
import commands.chat.commands.admin.*;
import commands.chat.commands.fun.Achievement;
import commands.chat.commands.fun.TTS;
import commands.chat.commands.fun.Poke;
import commands.chat.commands.fun.Troll;
import commands.chat.commands.general.*;
import commands.chat.commands.money.Slots;
import commands.chat.commands.money.Vote;
import commands.chat.commands.music.*;
import commands.chat.commands.testing.Error;
import commands.chat.commands.testing.Ping;
import commands.chat.commands.testing.Test;
import commands.chat.core.ChatHandler;
import listeners.AudioReconnectListener;
import listeners.ChatCommandListener;
import listeners.OnGuildChangeListener;
import listeners.ReadyListener;
import net.dv8tion.jda.core.JDABuilder;

public class Importer {

    public static void importConsoleCommands(){

        /*ConsoleHandler  .addCommand(new Ping());
        ConsoleHandler  .addCommand(new LeaveGuild());*/

    }

    public static void importChatCommands(){
        new ChatHandler()

                .addSection("Admin",
                        new AutoRole(),
                        new Clear(),
                        new Permission(),
                        new Prefix(),
                        new Welcome())

                .addSection("Fun",
                        new Achievement(),
                        new Poke(),
                        new TTS())

                .addSection("General",
                        new About(),
                        new Bio(),
                        new DecToHex(),
                        new Help(),
                        new HexToDec(),
                        new Invite(),
                        new Profile(),
                        new Server(),
                        new Shorten(),
                        new WhoAmI(),
                        new WhoIs())

                .addSection("Money",
                        new Slots(),
                        new Vote())

                .addSection("Music",
                        new Connect(),
                        new Disconnect(),
                        new Info(),
                        new LoadQueue(),
                        new Pause(),
                        new Play(),
                        new Playlist(),
                        new Queue(),
                        new Repeat(),
                        new Resume(),
                        new SaveQueue(),
                        new Skip(),
                        new Stop(),
                        new Volume())

                .addSection("Testing",
                        new Error(),
                        new Ping(),
                        new Test())


        ;

    }

    public static void addListeners(JDABuilder builder){

        builder
                // Core Listeners
                .addEventListener(new ReadyListener())
                .addEventListener(new ChatCommandListener())
                .addEventListener(new OnGuildChangeListener())
                .addEventListener(new AudioCore())

                .addEventListener(new AudioReconnectListener())

                // Command Specific Listeners
                .addEventListener(new AutoRole.Listener())
                .addEventListener(new Welcome())

        ;

    }

}
