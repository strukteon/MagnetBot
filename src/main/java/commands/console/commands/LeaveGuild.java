package commands.console.commands;
/*
    Created by nils on 30.12.2017 at 02:06.
    
    (c) nils 2017
*/

import commands.console.core.Console;
import commands.console.core.ConsoleCommand;
import core.tools.AutoComplete;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.Event;

public class LeaveGuild implements ConsoleCommand {
    @Override
    public boolean execute(Event lastEvent, String full, String cmd, String[] args) {
        return cmd.equals("leave");
    }

    @Override
    public void action(Event lastEvent, String full, String cmd, String[] args) {

        if (lastEvent.getJDA().getGuilds().size() != 0){

            if (args.length != 0)
            if (AutoComplete.guild(lastEvent.getJDA().getGuilds(), args[0]) != null){
                Guild g = AutoComplete.guild(lastEvent.getJDA().getGuilds(), args[0]);

                g.leave().queue();

                Console.info("This bot has left the guild " + g.getName() + " (" + g.getId() + ")");
            }

        } else {
            Console.error("This Bot is no member of any guild!");
        }

    }
}
