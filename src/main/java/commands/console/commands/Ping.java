package commands.console.commands;
/*
    Created by nils on 28.12.2017 at 15:31.
    
    (c) nils 2017
*/

import commands.console.core.Console;
import commands.console.core.ConsoleCommand;
import net.dv8tion.jda.core.events.Event;

public class Ping implements ConsoleCommand {
    @Override
    public boolean execute(Event lastEvent, String full, String cmd, String[] args) {

        return cmd.equals("ping");

    }

    @Override
    public void action(Event lastEvent, String full, String cmd, String[] args) {

        Console.info("Pong!");

    }
}
