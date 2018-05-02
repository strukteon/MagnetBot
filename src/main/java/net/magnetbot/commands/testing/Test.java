package net.magnetbot.commands.testing;
/*
    Created by nils on 12.03.2018 at 23:16.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.audio.AudioReceiveHandler;
import net.dv8tion.jda.core.audio.CombinedAudio;
import net.dv8tion.jda.core.audio.UserAudio;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.managers.AccountManager;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.utils.Static;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class Test implements Command {
    static OutputStream outputStream;
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        EmbedBuilder builder = Message.INFO();
        builder.setColor(Static.Color.GREEN)
                .setAuthor(event.getAuthor().getName() + "'s Poll", "https://magnetbot.net", event.getAuthor().getEffectiveAvatarUrl())
                .setDescription("Should I update the logo?")
        .addField("Choices",
                ":one: Yes\n:two: No\n:three: Maybe\n:four: Current logo is cool!", true);
        ;
        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("test", 0);
    }

    private class Hanad implements AudioReceiveHandler {

        @Override
        public boolean canReceiveCombined() {
            return true;
        }

        @Override
        public boolean canReceiveUser() {
            return false;
        }

        @Override
        public void handleCombinedAudio(CombinedAudio combinedAudio) {
            try {

                outputStream.write(combinedAudio.getAudioData(1.0));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void handleUserAudio(UserAudio userAudio) {

        }
    }
}
