package commands.chat.commands.admin;
/*
    Created by nils on 15.02.2018 at 22:31.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.UserData;
import core.tools.AutoComplete;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Permission implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event,"-m permission add <permission> [user]\n -m permission remove <permission> [user]").build()).queue();
        else {
            User u;

            if (args.length >= 3){
                Member m = AutoComplete.member(event.getGuild().getMembers(), args[2]);
                if (m != null)
                    u = m.getUser();
                else {
                    event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event,"-m permission add <permission> [user]\n -m permission remove <permission> [user]").build()).queue();
                    return;
                }
            } else {
                u = event.getAuthor();
            }
            if (args.length >= 2)
                switch (args[0]){

                    case "add":

                        if (!UserData.hasPermission(u.getId(), args[1])){

                            UserData.addPermission(u.getId(), args[1]);
                            event.getTextChannel().sendMessage(Message.INFO(event, "You have added the permission ``" + args[1] + "`` to the user " + u.getAsMention()).build()).queue();
                        } else {
                            event.getTextChannel().sendMessage(Message.INFO(event, "The user " + u.getAsMention() + "already has the permission ``" + args[1] + "``").build()).queue();
                        }

                        break;

                    case "remove":

                        if (UserData.hasPermission(u.getId(), args[1])){

                            UserData.removePermission(u.getId(), args[1]);
                            event.getTextChannel().sendMessage(Message.INFO(event, "You have removed the permission ``" + args[1] + "`` from the user " + u.getAsMention()).build()).queue();
                        } else {
                            event.getTextChannel().sendMessage(Message.INFO(event, "The user " + u.getAsMention() + "doesn't have the permission ``" + args[1] + "``").build()).queue();
                        }

                        break;

                    default:
                        event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event,"-m permission add <permission>\n-m permission remove <permission>").build()).queue();
                        break;

                }
            else
                event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event,"-m permission add <permission> [user]\n -m permission remove <permission> [user]").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("permission", 3)
                        .setHelp("change permissions for a specefic user");
    }
}
