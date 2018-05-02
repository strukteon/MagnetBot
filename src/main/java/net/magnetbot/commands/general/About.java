package net.magnetbot.commands.general;
/*
    Created by nils on 10.02.2018 at 00:46.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Guild;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class About implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        EmbedBuilder builder = Message.INFO(event);
        JDA jda = event.getJDA();

        int userCount = 0;
        for (Guild g : jda.getGuilds())
            userCount += g.getMembers().size();

        int playing = 0;
        int listeners = 0;
        for (Guild g : jda.getGuilds())
            if (g.getAudioManager().isConnected()) {
                playing++;
                listeners += g.getAudioManager().getConnectedChannel().getMembers().size()-1;
            }

        builder .setAuthor(jda.getSelfUser().getName(), "https://magnetbot.net", jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("I'm **Magnet**, a **feature-rich discord bot**. I can **play music**, provide **administration tools** and even more.\n" +
                        "If you need support on something, feel free to **join our Server: https://discord.gg/uAT7uUb**\n\n" +
                        "Here are some Informations about me:\n" +
                        "I am **running on " + jda.getGuilds().size() + " Servers**, on which are **" + userCount + " Users**. **" +
                        jda.getUsers().size() + "** are online (" + ( 100 * jda.getUsers().size() / userCount) + "%).\n" +
                        "I am currently playing in **" + playing + " VoiceChannels** and **" + listeners + " are listening**." +
                        "" +
                        "\n\n\n\u00A9 [strukteon](https://strukteon.me)#7237\n" +
                        "\tWebsite: https://magnetbot.net/\n" +
                        "\tPoll: https://discordbots.org/bot/389016516261314570/vote\n" +
                        "\tGitHub: https://github.com/strukteon/MagnetBot\n" +
                        "\tInvite me: https://magnetbot.net/redirect?rel=invite");
        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("about", PermissionLevel.MEMBER)
                        .setAlias("info")
                        .setHelp("shows some infos about this bot");
    }
}
