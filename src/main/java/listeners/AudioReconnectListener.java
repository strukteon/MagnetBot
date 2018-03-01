package listeners;
/*
    Created by nils on 01.03.2018 at 22:13.
    
    (c) nils 2018
*/

import audio.AudioCore;
import core.Main;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class AudioReconnectListener extends ListenerAdapter {

    @Override
    public void onReconnect(ReconnectedEvent event) {

        Main.audioCore.reconnectAll(event);

    }
}
