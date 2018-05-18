package net.magnetbot.commands.music;
/*
    Created by nils on 05.02.2018 at 22:22.
    
    (c) nils 2018
*/

import net.magnetbot.audio.AudioCore;
import net.magnetbot.audio.youtube.YouTubeAPI;
import com.google.api.services.youtube.model.SearchResult;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.tools.Tools;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;

import java.util.List;

public class Play implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {

        List<String> arg = syntax.getAsListString("url/query");

        if (event.getMember().getVoiceState().getChannel() == null)
            event.getTextChannel().sendMessage(Message.ERROR(event, "You have to be connected to a VoiceChannel!").build()).queue();
        else if (arg.get(0).startsWith("http://") || arg.get(0).startsWith("https://"))
            AudioCore.load(event, arg.get(0));
        else {
            try {
                SearchResult video = YouTubeAPI.searchVideo(Tools.listToString(arg, " "));
                AudioCore.load(event, video.getId().getVideoId(), event.getMember().getVoiceState().getChannel());
            } catch (YouTubeAPI.NoResultException e){
                event.getTextChannel().sendMessage(Message.ERROR(event, "No results found for: " + Tools.listToString(arg, " ")).build()).queue();
            }

        }

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("play", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder(
                                        new SyntaxBuilder()
                                                .addElement("url/query", SyntaxElementType.STRING, true)
                                )
                        )
                        .setHelp("play a track from an URL or youtube");
    }
}
