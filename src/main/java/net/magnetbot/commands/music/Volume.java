package net.magnetbot.commands.music;
/*
    Created by nils on 08.02.2018 at 01:29.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.audio.AudioCore;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;

public class Volume implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        int volume = syntax.getAsInt("percent");
        if (volume < 0)
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "Minimum volume: 0%").build()).queue();
        else if (volume > 150)
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "Maximum volume: 150%").build()).queue();
        else {
            AudioCore.getGuildAudioPlayer(event.getGuild()).scheduler.setVolume(volume);
            event.getTextChannel().sendMessage(Message.INFO(event, "Volume set to " + volume + "%").build()).queue();
        }
    }

    @Override
    public Permission[] requiredUserPerms() {
        return new Permission[]{Permission.VOICE_MUTE_OTHERS};
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("volume", PermissionLevel.SUPPORTER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addElement("percent", SyntaxElementType.INT)
                        )
                        .setHelp("changes the volume of the media playback");
    }
}
