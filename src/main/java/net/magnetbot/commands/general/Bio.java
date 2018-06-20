package net.magnetbot.commands.general;
/*
    Created by nils on 01.02.2018 at 21:51.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.core.sql.UserSQL;
import net.magnetbot.core.tools.Tools;

public class Bio implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        UserSQL userSQL = UserSQL.fromUser(event.getAuthor());
        userSQL.setBio(Tools.listToString(syntax.getAsListString("text"), " "));

        event.getTextChannel().sendMessage(Message.INFO(event, "Your bio has been set to:\n``" + Tools.listToString(syntax.getAsListString("text"), " ") + "``").build()).queue();


    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("bio", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addElement("text", SyntaxElementType.STRING, true)
                        )
                        .setHelp("set a bio that will be shown in your profile");
    }
}
