package commands.console.core;
/*
    Created by nils on 28.12.2017 at 15:08.
    
    (c) nils 2017
*/

import net.dv8tion.jda.core.events.Event;

public interface ConsoleCommand {

    boolean execute(Event lastEvent, String full, String cmd, String[] args);

    void action(Event lastEvent, String full, String cmd, String[] args);

}
