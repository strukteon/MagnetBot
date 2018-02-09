package commands.console.core;
/*
    Created by nils on 28.12.2017 at 15:05.
    
    (c) nils 2017
*/

import commands.console.commands.Ping;
import net.dv8tion.jda.core.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleHandler {

    public static List<ConsoleCommand> consoleCommands = new ArrayList<>();

    public static void checkInput(){

        Scanner scanner = new Scanner(System.in);

        String full = scanner.nextLine().toLowerCase();

        String cmd = full.split(" ")[0];

        String[] args = full.replaceFirst(cmd + " ", "").split(" ");

        testCommands(full, cmd, args);
        checkInput();
    }

    private static void testCommands(String full, String cmd, String[] args){

        for (ConsoleCommand command : consoleCommands){

            if (command.execute(EventUpdater.lastEvent, full, cmd, args)){

                command.action(EventUpdater.lastEvent, full, cmd, args);
                return;
            }
        }

        Console.error("No command called '" + cmd + "' found");
    }

    public static void addCommand(ConsoleCommand cmd){
        consoleCommands.add(cmd);
    }

}
