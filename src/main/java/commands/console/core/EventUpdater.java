package commands.console.core;
/*
    Created by nils on 28.12.2017 at 15:19.
    
    (c) nils 2017
*/

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EventUpdater extends ListenerAdapter {

    public static Event lastEvent = null;

    @Override
    public void onGenericEvent(Event event) {
        lastEvent = event;
    }
}
