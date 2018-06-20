package net.magnetbot.commands.admin;
/*
    Created by nils on 07.02.2018 at 18:11.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;

import java.util.List;

public class Clear implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {

        int num = syntax.getAsInt("amount");

        if (num > 100){
            event.getTextChannel().sendMessage(net.magnetbot.core.command.Message.ERROR(event, "You can only delete a maximum of 100 Messages at a time!", false).build()).queue();
        } else if (num < 1){
            event.getTextChannel().sendMessage("Why are you doing this?").queue();
        }

        MessageHistory history = event.getTextChannel().getHistory();

        List<Message> messages = history.retrievePast(num).complete();

        for (int i = 0; i < messages.size(); i++){
            if (i != messages.size()-1){
                messages.get(i).delete().queue();
            } else {
                messages.get(i).delete().queue((response) -> event.getTextChannel().sendMessage(net.magnetbot.core.command.Message.INFO(event, "Deleted ``" + messages.size() + "`` Messages").build()).queue());
            }
        }
    }

    @Override
    public Permission[] requiredBotPerms() {
        return new Permission[]{Permission.MESSAGE_MANAGE};
    }

    @Override
    public Permission[] requiredUserPerms() {
        return new Permission[]{Permission.MESSAGE_MANAGE};
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("clear", PermissionLevel.SUPPORTER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addElement("amount", SyntaxElementType.INT)
                        )
                        .setHelp("clear a specific amount of messages in the current channel");
    }
}
