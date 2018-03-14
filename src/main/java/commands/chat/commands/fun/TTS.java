package commands.chat.commands.fun;
/*
    Created by nils on 06.01.2018 at 19:15.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.tools.Tools;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TTS implements ChatCommand{

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        MessageBuilder builder = new MessageBuilder();
        builder .append(Tools.argsToString(args, " "))
                .setTTS(true);

        event.getTextChannel().sendMessage(builder.build()).complete().delete().queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("tts", 0)
                        .setHelp("let this bot send a tts message");
    }
}
