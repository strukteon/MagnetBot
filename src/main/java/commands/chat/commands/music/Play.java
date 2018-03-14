package commands.chat.commands.music;
/*
    Created by nils on 05.02.2018 at 22:22.
    
    (c) nils 2018
*/

import audio.AudioCore;
import audio.youtube.YouTubeAPI;
import com.google.api.services.youtube.model.SearchResult;
import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import core.tools.Tools;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Static;

import java.awt.*;
import java.sql.Statement;

public class Play implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {

        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, Static.PREFIX + "play <videourl>\n " + Static.PREFIX + "play <youtubequery>").build()).queue();
        else {

            if (event.getMember().getVoiceState().getChannel() == null)
                event.getTextChannel().sendMessage(Message.ERROR(event, "You have to be connected to a VoiceChannel!").build()).queue();
            else
                if (args[0].startsWith("http://") || args[0].startsWith("https://"))
                    Main.audioCore.load(event, rawArgs[0]);
                else {
                    try {
                        SearchResult video = YouTubeAPI.searchVideo(Tools.argsToString(args, " "));

                        Main.audioCore.load(event, video.getId().getVideoId());
                    } catch (YouTubeAPI.NoResultException e){
                        event.getTextChannel().sendMessage(Message.ERROR(event, "No results found for: " + Tools.argsToString(args, " ")).build()).queue();
                    }

                }

        }

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("play", 0)
                        .setHelp("play a track from an URL or youtube");
    }
}
