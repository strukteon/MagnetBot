package net.magnetbot.commands.general;
/*
    Created by nils on 05.05.2018 at 12:39.
    
    (c) nils 2018
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.MagnetBot;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.utils.Static;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Stats implements Command {


    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        JDA jda = event.getJDA();
        int guilds = jda.getGuilds().size();
        int textChannels = jda.getTextChannels().size();
        int voiceChannels = jda.getVoiceChannels().size();
        int privateChannels = jda.getPrivateChannels().size();

        int playing = 0;
        int listeners = 0;
        for (Guild g : jda.getGuilds())
            if (g.getAudioManager().isConnected()) {
                playing++;
                listeners += g.getAudioManager().getConnectedChannel().getMembers().size()-1;
            }
        String apiUrl = "https://discordbots.org/api/bots/389016516261314570";
        JsonObject dbl = new JsonParser().parse(new InputStreamReader(new URL(apiUrl).openStream())).getAsJsonObject();
        JsonObject dblStats = new JsonParser().parse(new InputStreamReader(new URL(apiUrl + "/stats").openStream())).getAsJsonObject();

        int upvotes = dbl.get("points").getAsInt();

        long ping = jda.getPing();

        EmbedBuilder builder = net.magnetbot.core.command.Message.INFO(event);

        builder.addField("Generic Stats", "Shard: ``" + (Static.SHARD_ID + 1) +"`` of ``" + Static.SHARD_COUNT + "``\n" +
                "Users: ``" + event.getJDA().getUsers().size() + "``\n" + "Guilds (on this shard): ``" + guilds + "``\n" + "Guilds (global): ``" + dblStats.get("server_count").getAsInt() + "``", true)
                .addField("Channel Stats", "TextChannels: ``" + textChannels + "``\n" + "VoiceChannels: ``" + voiceChannels + "``\n" + "PrivateChannels: ``" + privateChannels + "``", true)
                .addBlankField(false)
                .addField("Music Stats", "Guilds playing: ``" + playing + "``\n" + "Listeners: ``" + listeners + "``", true)
                .addField("Advanced Stats", "Ping to Discord: ``" + ping + "``\n" + "Upvotes: ``" + (upvotes>=0?upvotes:"Oops, an error occurred!") + "``", true);

        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("stats", PermissionLevel.MEMBER);
    }
}
