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

        builder .setAuthor(jda.getSelfUser().getName(), "https://magnetbot.net", jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("I'm **Magnet**, a **feature-rich discord bot**. I can **play music**, provide **administration tools** and even more.\n" +
                        "If you need support on something, feel free to **join our Server: https://discord.gg/uAT7uUb**\n\n" +
                        "Here are some Informations about me:\n" +
                        "If you would like to know my **Statistics**, type ``m.stats``." +
                        "\n\n\n\u00A9 [strukteon](https://strukteon.me)#7237\n" +
                        "\tWebsite: [magnetbot.net](https://magnetbot.net/)\n" +
                        "\tVote: [discordbots.org](https://discordbots.org/bot/389016516261314570/vote)\n" +
                        "\tGitHub: [strukteon/MagnetBot](https://github.com/strukteon/MagnetBot)\n" +
                        "\tInvite me: [Click here](https://magnetbot.net/redirect?rel=invite)");
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
