package net.magnetbot.commands.admin;
/*
    Created by nils on 15.02.2018 at 22:31.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.core.sql.UserSQL;

public class Permission implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        Member m = syntax.getAsMember("user");
        User u = null;
        if (m == null)
            u = event.getAuthor();
        else
            u = m.getUser();

        UserSQL userSQL = UserSQL.fromUser(event.getAuthor());

        switch (syntax.getAsString("subcmd")){

            case "add":

                if (!userSQL.hasPermission(syntax.getAsString("permission"))){

                    userSQL.addPermission(syntax.getAsString("permission"));
                    event.getTextChannel().sendMessage(Message.INFO(event, "You have added the permission ``" + syntax.getAsString("permission") + "`` to the user " + u.getAsMention()).build()).queue();
                } else {
                    event.getTextChannel().sendMessage(Message.INFO(event, "The user " + u.getAsMention() + "already has the permission ``" + syntax.getAsString("permission") + "``").build()).queue();
                }

                break;

            case "remove":

                if (userSQL.hasPermission(syntax.getAsString("permission"))){

                    userSQL.removePermission(syntax.getAsString("permission"));
                    event.getTextChannel().sendMessage(Message.INFO(event, "You have removed the permission ``" + syntax.getAsString("permission") + "`` from the user " + u.getAsMention()).build()).queue();
                } else {
                    event.getTextChannel().sendMessage(Message.INFO(event, "The user " + u.getAsMention() + "doesn't have the permission ``" + syntax.getAsString("permission") + "``").build()).queue();
                }

                break;

            default:
                event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event,"-m permission add <permission>\n-m permission remove <permission>").build()).queue();
                break;

        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("permission", PermissionLevel.BOT_ADMIN)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addElement("subcmd", SyntaxElementType.STRING)
                                        .addElement("permission", SyntaxElementType.STRING)
                                        .addOptionalElement("user", SyntaxElementType.MEMBER)
                        )
                        .setHelp("change permissions for a specefic user");
    }
}
