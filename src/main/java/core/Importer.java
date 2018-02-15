package core;
/*
    Created by nils on 28.12.2017 at 15:36.
    
    (c) nils 2017
*/

import audio.AudioCore;
import commands.chat.commands.admin.Clear;
import commands.chat.commands.admin.Permission;
import commands.chat.commands.fun.TTS;
import commands.chat.commands.fun.Poke;
import commands.chat.commands.general.*;
import commands.chat.commands.money.Slots;
import commands.chat.commands.money.Vote;
import commands.chat.commands.music.*;
import commands.chat.commands.testing.Error;
import commands.chat.commands.testing.Ping;
import commands.chat.core.ChatHandler;
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
            //Admin
                .addCommand(new Clear())
                .addCommand(new Permission())

            //Fun
                .addCommand(new Poke())
                .addCommand(new TTS())

            //General
                .addCommand(new About())
                .addCommand(new Bio())
                .addCommand(new Help())
                .addCommand(new Invite())
                .addCommand(new Profile())
                .addCommand(new Server())
                .addCommand(new WhoAmI())
                .addCommand(new WhoIs())

            //Money
                .addCommand(new Slots())
                .addCommand(new Vote())

            //Music
                .addCommand(new Connect())
                .addCommand(new Disconnect())
                .addCommand(new Info())
                .addCommand(new Pause())
                .addCommand(new Play())
                .addCommand(new Playlist())
                .addCommand(new Resume())
                .addCommand(new Skip())
                .addCommand(new Stop())
                .addCommand(new Volume())

            //Testing
                .addCommand(new Error())
                .addCommand(new Ping())


        ;

    }

    public static void addListeners(JDABuilder builder){

        builder .addEventListener(new ReadyListener())
                .addEventListener(new ChatCommandListener())
                .addEventListener(new OnGuildChangeListener())
        .addEventListener(new AudioCore());

    }

}
