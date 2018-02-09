package commands.chat.commands.music;
/*
    Created by nils on 08.02.2018 at 00:31.
    
    (c) nils 2018
*/

import audio.youtube.YouTubeAPI;
import com.google.api.services.youtube.model.SearchResult;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import core.tools.Tools;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Static;

public class Playlist implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("playlist");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, Static.PREFIX + "play <videourl>\n " + Static.PREFIX + "play <youtubequery>").build()).queue();
        else {

            if (event.getMember().getVoiceState().getChannel() == null)
                event.getTextChannel().sendMessage(Message.ERROR(event, "You have to be connected to a VoiceChannel!").build()).queue();
            else
            if (args[0].startsWith("http://") || args[0].startsWith("https://"))
                Main.audioCore.load(event, args[0]);
            else {
                try {
                    SearchResult playlist = YouTubeAPI.searchPlaylist(Tools.argsToString(args, " "));

                    Main.audioCore.load(event, playlist.getId().getPlaylistId());
                } catch (YouTubeAPI.NoResultException e){
                    event.getTextChannel().sendMessage(Message.ERROR(event, "No results found for: " + Tools.argsToString(args, " ")).build()).queue();
                }

            }

        }
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
