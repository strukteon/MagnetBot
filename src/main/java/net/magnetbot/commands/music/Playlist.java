package net.magnetbot.commands.music;
/*
    Created by nils on 08.02.2018 at 00:31.
    
    (c) nils 2018
*/

import net.magnetbot.audio.youtube.YouTubeAPI;
import com.google.api.services.youtube.model.SearchResult;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.MagnetBot;
import net.magnetbot.core.tools.Tools;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;

import java.util.List;

public class Playlist implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {

        List<String> arg = syntax.getAsListString("url/query");

        if (event.getMember().getVoiceState().getChannel() == null)
            event.getTextChannel().sendMessage(Message.ERROR(event, "You have to be connected to a VoiceChannel!").build()).queue();
        else if (arg.get(0).startsWith("http://") || arg.get(0).startsWith("https://"))
            MagnetBot.audioCore.load(event, arg.get(0));
        else {
            try {
                SearchResult playlist = YouTubeAPI.searchPlaylist(Tools.listToString(arg, " "));

                MagnetBot.audioCore.load(event, playlist.getId().getPlaylistId());
            } catch (YouTubeAPI.NoResultException e){
                event.getTextChannel().sendMessage(Message.ERROR(event, "No results found for: " + Tools.listToString(arg, " ")).build()).queue();
            }

        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("playlist", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder(
                                        new SyntaxBuilder()
                                                .addElement("url/query", SyntaxElementType.STRING, true)
                                )
                        )
                        .setHelp("play a playlist from an URL or youtube");
    }
}
