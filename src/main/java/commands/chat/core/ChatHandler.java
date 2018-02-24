package commands.chat.core;
/*
    Created by nils on 30.12.2017 at 01:35.
    
    (c) nils 2017
*/

import commands.chat.tools.Message;
import commands.chat.utils.UserData;
import core.tools.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChatHandler {

    public static List<ChatCommand> chatCommands = new ArrayList<>();

    public static void handleInput(MessageReceivedEvent event, String full){

        String cmd = full.split(" ")[0];

        String[] args = full.replaceFirst(cmd + (full.split(" ").length > 1 ? " " : ""), "").split(" ");

        testCommands(event, full, cmd, args);

    }

    public static void testCommands(MessageReceivedEvent event, String full, String cmd, String[] args){

        for (ChatCommand command : chatCommands){

            if (command.execute(event, full, cmd, args)){
                try {
                    if (Chat.permissionLevel(event) >= command.permissionLevel() || UserData.hasPermission(event.getAuthor().getId(), command.premiumPermission()))
                        try {

                            command.action(event, full, cmd, args);

                        } catch (InsufficientPermissionException e) {
                            Permission missing = e.getPermission();
                            try {
                                event.getTextChannel().sendMessage(
                                        Message.ERROR(event, "Please give me the following Permission: ``" + missing.getName() + "``")
                                                .build()).queue();
                            } catch (InsufficientPermissionException error) {
                            }
                        } catch (Exception e) {
                            event.getTextChannel().sendMessage(Message.INTERNAL_ERROR(event, e).build()).queue();
                            e.printStackTrace();
                        }
                    else
                        event.getTextChannel().sendMessage(
                                Message.ERROR(event, "You do not have the required permissions to execute that command\n Needed Permission level: " +
                                        Chat.permLevel(command.permissionLevel()) + "\n Your Permission level: " + Chat.permLevel(Chat.permissionLevel(event)))
                                        .build()).queue();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    EmbedBuilder error = Message.INFO(event);
                        error.setTitle("**An internal error ocurred**")
                                .setDescription("tt")
                                .setColor(Color.RED)
                                .addField("Guild", event.getGuild().getName(), true)
                                .addField("Guild ID", event.getGuild().getId(), true)
                                .addField("Error Message", e.toString(), false)
                                .addField("Stacktrace", Tools.stacktraceToString(e.getStackTrace(), false), false);
                    event.getGuild().getOwner().getUser().openPrivateChannel().complete()
                            .sendMessage(error.build()).queue();
                    event.getTextChannel().sendMessage(Message.INTERNAL_ERROR(event, e).build()).queue();
                }
            }
        }
    }

    public ChatHandler addCommand(ChatCommand command){
        chatCommands.add(command);
        return this;
    }

}
