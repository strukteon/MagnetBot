package net.magnetbot.commands.general;
/*
    Created by nils on 05.05.2018 at 12:39.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.MagnetBot;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;

import java.util.ArrayList;
import java.util.List;

public class Stats implements Command {


    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        Message deleteAfter = event.getChannel().sendMessage("This could take some time, please wait until I finished the calculations").complete();
        event.getChannel().sendTyping().queue();

        int guilds = event.getJDA().getGuilds().size();
        int textChannels = event.getJDA().getTextChannels().size();
        int voiceChannels = event.getJDA().getVoiceChannels().size();
        int privateChannels = event.getJDA().getPrivateChannels().size();

        int playing = 0;
        int listeners = 0;
        for (Guild g : event.getJDA().getGuilds())
            if (g.getAudioManager().isConnected()) {
                playing++;
                listeners += g.getAudioManager().getConnectedChannel().getMembers().size()-1;
            }

        int upvotes = -1;//MagnetBot.dblAPI.getBot(event.getJDA().getSelfUser().getId()).getPoints();

        long ping = event.getJDA().getPing();

        EmbedBuilder builder = net.magnetbot.core.command.Message.INFO(event);

        builder.addField("Generic Stats", "Users: ``" + event.getJDA().getUsers().size() + "``\n" + "Guilds: ``" + guilds + "``", true)
                .addField("Channel Stats", "TextChannels: ``" + textChannels + "``\n" + "VoiceChannels: ``" + voiceChannels + "``\n" + "PrivateChannels: ``" + privateChannels + "``", true)
                .addBlankField(false)
                .addField("Music Stats", "Guilds playing: ``" + playing + "``\n" + "Listeners: ``" + listeners + "``", true)
                .addField("Advanced Stats", "Ping: ``" + ping + "``\n" + "Upvotes: ``" + (upvotes>=0?upvotes:"Coming soon!") + "``", true);

        Thread.sleep(10);
        event.getChannel().sendMessage(builder.build()).queue();
        deleteAfter.delete().queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("stats", PermissionLevel.MEMBER);
    }
}
