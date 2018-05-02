package net.magnetbot.commands.testing;
/*
    Created by nils on 29.03.2018 at 17:24.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.managers.AccountManager;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.tools.Tools;

public class Broadcast implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String msg = Tools.listToString(syntax.getAsListString("msg"));
        StringBuilder error = new StringBuilder();
        int successes = 0;
        for (Guild g : event.getJDA().getGuilds()){
            try {
                g.getDefaultChannel().sendMessage(Message.INFO(msg).setTitle(":mega: Broadcast").setFooter("Sent by " + event.getAuthor().getName(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue();
                successes++;
            } catch (InsufficientPermissionException e){
                if (error.length() != 0)
                    error.append("\n");
                error.append(g.getName() + ": ``" + e.getLocalizedMessage() + "``");
            }
        }
        EmbedBuilder response = Message.INFO(event, "Successfully sent the broadcast in " + successes + " Guilds.");
        if (error.length() > 0)
            response.addField("Errors:", error.toString(), true);
        event.getTextChannel().sendMessage(response.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("broadcast", PermissionLevel.BOT_OWNER)
                .setHelp("Send a message on every server this bot is on")
                .setAlias("bc")
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("msg", SyntaxElementType.STRING, true)
                );
    }


}
