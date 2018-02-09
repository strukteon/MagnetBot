package commands.chat.commands.fun;
/*
    Created by nils on 06.01.2018 at 19:15.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.tools.Tools;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TTS implements ChatCommand{
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("tts");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {
        MessageBuilder builder = new MessageBuilder();
        builder .append(Tools.argsToString(args, " "))
                .setTTS(true);

        event.getTextChannel().sendMessage(builder.build()).complete().delete().queue();
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
