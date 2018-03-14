package commands.chat.commands.fun;
/*
    Created by nils on 08.03.2018 at 22:04.
    
    (c) nils 2018
*/

import audio.youtube.YouTubeAPI;
import com.google.api.services.youtube.model.SearchResult;
import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import core.tools.AutoComplete;
import core.tools.Tools;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Static;

import java.util.Arrays;

public class Troll implements ChatCommand {
    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        String[] argso = Arrays.copyOfRange(args, 1, args.length);
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, Static.PREFIX + "play <channel> <videourl>\n " + Static.PREFIX + "play <channel> <youtubequery>").build()).queue();
        else {
            String url = event.getMessage().getContentRaw().substring(7).trim();
            VoiceChannel channel = AutoComplete.voiceChannel(event.getGuild().getVoiceChannels(), args[0]);
            if (channel == null)
                event.getTextChannel().sendMessage(Message.ERROR(event, "The channel ``" + args[0] + "`` does not exist.").build()).queue();
            else
            if (argso[0].startsWith("http://") || argso[0].startsWith("https://"))
                Main.audioCore.load(event, url);
            else {
                try {
                    SearchResult video = YouTubeAPI.searchVideo(Tools.argsToString(argso, " "));
                    event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannelById(args[0]));
                    Main.audioCore.load(event, video.getId().getVideoId());
                } catch (YouTubeAPI.NoResultException e){
                    event.getTextChannel().sendMessage(Message.ERROR(event, "No results found for: " + Tools.argsToString(args, " ")).build()).queue();
                }

            }

        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("troll", 0)
                .setPremium("premium.music.troll");
    }
}
