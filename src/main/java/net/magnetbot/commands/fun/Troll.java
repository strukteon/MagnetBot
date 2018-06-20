package net.magnetbot.commands.fun;
/*
    Created by nils on 08.03.2018 at 22:04.
    
    (c) nils 2018
*/

import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.MagnetBot;
import net.magnetbot.audio.youtube.YouTubeAPI;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.core.tools.Tools;

import java.util.List;

public class Troll implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        List<String> query = syntax.getAsListString("url/query");
        VoiceChannel channel = syntax.getAsVoiceChannel("channel");

        if (query.get(0).startsWith("http://") || query.get(0).startsWith("https://"))
            MagnetBot.audioCore.load(event, query.get(0));
        else {
            String q = Tools.listToString(query, " ");
            try {
                SearchResult video = YouTubeAPI.searchVideo(q);
                event.getGuild().getAudioManager().openAudioConnection(channel);
                MagnetBot.audioCore.load(event, video.getId().getVideoId());
            } catch (YouTubeAPI.NoResultException e){
                event.getTextChannel().sendMessage(Message.ERROR(event, "No results found for: " + q).build()).queue();
            }

        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("troll", PermissionLevel.MEMBER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("channel", SyntaxElementType.VOICECHANNEL)
                                .addElement("url/query", SyntaxElementType.STRING, true)
                )
                .setPremium(true);
    }
}
