package net.magnetbot.commands.fun;
/*
    Created by nils on 09.03.2018 at 23:40.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.core.tools.Tools;

import java.net.URLEncoder;

public class Achievement implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        EmbedBuilder builder = Message.INFO(event);
        builder.setImage("https://www.minecraftskinstealer.com/achievement/a.php?i=2&h=" + URLEncoder.encode("Achievement get!", "UTF-8") + "&t=" +
                URLEncoder.encode(Tools.listToString(syntax.getAsListString("text"), " "), "UTF-8"));

        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("achievement", PermissionLevel.MEMBER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("text", SyntaxElementType.STRING, true)
                )
                .setHelp("create a minecraft achievement");
    }
}
