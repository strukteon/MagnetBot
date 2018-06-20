package net.magnetbot.core;
/*
    Created by nils on 28.12.2017 at 15:36.
    
    (c) nils 2017
*/

import net.dv8tion.jda.core.JDABuilder;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.commands.admin.*;
import net.magnetbot.commands.fun.*;
import net.magnetbot.commands.general.*;
import net.magnetbot.commands.money.DailyReward;
import net.magnetbot.commands.money.Slots;
import net.magnetbot.commands.money.Vote;
import net.magnetbot.commands.music.*;
import net.magnetbot.commands.settings.AutoRole;
import net.magnetbot.commands.settings.Prefix;
import net.magnetbot.commands.settings.Welcome;
import net.magnetbot.commands.testing.*;
import net.magnetbot.commands.testing.Error;
import net.magnetbot.commands.testing.Shutdown;
import net.magnetbot.commands.tools.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.CommandHandler;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.listeners.*;

import java.util.HashMap;

public class Importer {


    public static void importCommands(){
        new CommandHandler()

                .addSection("Admin",
                        new Ban(),
                        new Clear(),
                        new Kick(),
                        new Permission(),
                        new Unban())

                .addSection("Fun",
                        new Achievement(),
                        new BeLikeBill(),
                        new ChuckNorris(),
                        new Hug(),
                        new Kiss(),
                    //    new Osu(),
                        new Pat(),
                        new Poke(),
                        new RandomCat(),
                        new RandomDog(),
                        new TRBMB(),
                        new TTS(),
                        new Urban())

                .addSection("General",
                        new About(),
                        new Bio(),
                        new Credits(),
                        new Help(),
                        new Invite(),
                        new Profile(),
                        new Server(),
                        new Stats())

                .addSection("Money",
                        new DailyReward(),
                        new Slots(),
                        new Vote())

                .addSection("Tools",
                        new AddEmote(),
                        new DecToHex(),
                        new HexToDec(),
                      //  new Pastebin(),
                        new Poll(),
                        new WhoAmI(),
                        new WhoIs())

                .addSection("Music",
                        new ControlPanel(),
                        new Forward(),
                        new Join(),
                        new Leave(),
                        new Track(),
                        new LoadQueue(),
                        new Pause(),
                        new Play(),
                        new Playlist(),
                        new Queue(),
                        new Repeat(),
                        new Resume(),
                        new Rewind(),
                        new SaveQueue(),
                        new Shuffle(),
                        new Skip(),
                        new Stop(),
                        new Volume())

                .addSection("Settings",
                        new AutoRole(),
                        new Prefix(),
                        new Welcome())

                .addSection("Testing",
                        new Broadcast(),
                        new Error(),
                       // new Eval(),
                        new Ping(),
                        new Shutdown(),
                        new Test(),
                        new Uptime())

              /*  .addSection("User",
                        new Item())*/


        ;

    }

    public static void addListeners(JDABuilder builder){

        builder
                // Core Listeners
                .addEventListener(new ReadyListener())
                .addEventListener(new CommandListener())
                .addEventListener(new OnGuildChangeListener())
                .addEventListener(new AudioCore())
                .addEventListener(new LogListener())

                .addEventListener(new MusicLeaveListener())

                .addEventListener(new ControlPanel())

                // Command Specific Listeners
                .addEventListener(new Poll())
                .addEventListener(new AutoRole.Listener())
                .addEventListener(new Welcome())
                .addEventListener(new AddEmote());

    }

    public static void init(){
        Chat.names = new HashMap<>();
        Chat.names.put(PermissionLevel.BANNED, "Banned");
        Chat.names.put(PermissionLevel.MEMBER, "Member");
        Chat.names.put(PermissionLevel.SUPPORTER, "Admin");
        Chat.names.put(PermissionLevel.ADMIN, "Admin");
        Chat.names.put(PermissionLevel.OWNER, "Owner");
        Chat.names.put(PermissionLevel.BOT_ADMIN, "Bot Admin");
        Chat.names.put(PermissionLevel.BOT_OWNER, "Bot Owner");
    }

}
