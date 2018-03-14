package commands.chat.core;
/*
    Created by nils on 30.12.2017 at 01:35.
    
    (c) nils 2017
*/

import commands.chat.tools.Message;
import commands.chat.utils.UserData;
import core.Main;
import core.tools.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import utils.Static;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ChatHandler {

    public static HashMap<String, List<ChatCommand>> chatCommands = new HashMap<>();

    public static void handleInput(MessageReceivedEvent event, String full){

        String cmd = full.split(" ")[0];

        String[] args = full.toLowerCase().replaceFirst(cmd + (full.split(" ").length > 1 ? " " : ""), "").split(" ");

        String[] rawArgs = full.replaceFirst(cmd + (full.split(" ").length > 1 ? " " : ""), "").split(" ");

        testCommands(event, cmd, args, rawArgs);
    }

    public static void testCommands(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs){
        for (String key : chatCommands.keySet())
            for (ChatCommand command : chatCommands.get(key)){
                Chat.CommandInfo cmdInfo = command.commandInfo();
                if (cmdInfo.command.equals(cmd) || cmdInfo.cmdAlias.contains(cmd)){
                    Main.commandsHandled++;
                    try {
                        boolean premiumOk = !cmdInfo.isPremium || UserData.hasPermission(event.getAuthor().getId(), cmdInfo.premiumPermission);
                        boolean permsOk = Chat.permissionLevel(event) >= cmdInfo.permissionLevel;
                        if (permsOk && premiumOk)
                            try {

                                command.action(event, cmd, args, rawArgs);

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
                            if (!permsOk && premiumOk)
                                event.getTextChannel().sendMessage(
                                    Message.ERROR(event, "You do not have the required permissions to execute that command\n Needed Permission level: " +
                                            Chat.permLevel(cmdInfo.permissionLevel) + "\n Your Permission level: " + Chat.permLevel(Chat.permissionLevel(event)))
                                            .build()).queue();
                            else if (permsOk && !premiumOk)
                                event.getTextChannel().sendMessage(
                                        Message.ERROR(event, "You do not have the required premium permission to execute that command\n Needed Permission: " +
                                                cmdInfo.premiumPermissionName + " [" + cmdInfo.premiumPermission +"]")
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
                                    .addField("Stacktrace", ""/*Tools.stacktraceToString(e.getStackTrace(), false)*/, false);
                        event.getJDA().getUserById(Static.BOT_OWNER_ID).openPrivateChannel().complete()
                                .sendMessage(error.build()).queue();
                        event.getTextChannel().sendMessage(Message.INTERNAL_ERROR(event, e).build()).queue();
                    }
                }
            }
    }

    public ChatHandler addSection(String section, ChatCommand... chatCommands){
        this.chatCommands.put(section, Arrays.asList(chatCommands));
        return this;
    }

    public static int size(){
        int i = 0;
        for (String s : chatCommands.keySet())
            for (ChatCommand c : chatCommands.get(s))
                i++;
        return i;
    }

}
