package commands.chat.commands.music;
/*
    Created by nils on 08.02.2018 at 01:29.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Volume implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m volume <percent>").build()).queue();
        else {
            try {
                int volume = Integer.parseInt(args[0]);
                if (volume < 0)
                    event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "Minimum volume: 0%").build()).queue();
                else if (volume > 150)
                    event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "Maximum volume: 150%").build()).queue();
                else {
                    Main.audioCore.getGuildAudioPlayer(event.getGuild()).scheduler.setVolume(volume);
                    event.getTextChannel().sendMessage(Message.INFO(event, "Volume set to " + Integer.parseInt(args[0]) + "%").build()).queue();
                }
            } catch (NumberFormatException e){
                event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m volume <percent>").build()).queue();
            }
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("volume", 1)
                     //   .setPremium("premium.music.volume")
                        .setHelp("changes the volume of the media playback");
    }
}
