package commands.chat.commands.general;
/*
    Created by nils on 03.01.2018 at 22:03.
    
    (c) nils 2018
*/

import com.google.api.services.youtube.model.LiveChatPollItem;
import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.core.ChatHandler;
import commands.chat.tools.Message;
import core.Importer;
import core.Main;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Emoji;

public class Help implements ChatCommand{

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals("")) {
            EmbedBuilder builder = msg(Message.INFO(event, Emoji.INFO + " Help", "Look at my **[documentation](https://magnet.strukteon.me/documentation)**!\nFor a more detailed description about a command, type ``-m help <command>``"));

            event.getTextChannel().sendMessage(builder.build()).queue();
        } else {
            boolean exists = false;
            for (String section : ChatHandler.chatCommands.keySet()) {
                for (ChatCommand c : ChatHandler.chatCommands.get(section)) {
                    Chat.CommandInfo cmdInfo = c.commandInfo();

                    if (cmdInfo.command.equals(args[0])){
                        exists = true;
                        EmbedBuilder builder = Message.INFO(event, Emoji.INFO + " Help", "Detailed informations about the command ``" + args[0] + "``");

                        StringBuilder alias = new StringBuilder();
                        for (String s : cmdInfo.cmdAlias){
                            if (alias.length() != 0)
                                alias.append("\n ");
                            alias.append(s);
                        }

                        builder.addField("Information", "``" + cmdInfo.help + "``", false);

                        if (!alias.toString().equals(""))
                            builder.addField("Alias", "``" + alias.toString() + "``", false);
                        builder
                                .addField("Syntax", "``" + cmdInfo.syntax + "``", false)
                                .addField("Permission Level", Chat.permLevel(cmdInfo.permissionLevel), false)
                                .addField("Premium Only", ""+cmdInfo.isPremium, true);
                        if (cmdInfo.isPremium)
                            builder.addField("Premium Permission", cmdInfo.premiumPermissionName, true);
                        event.getTextChannel().sendMessage(builder.build()).queue();
                    }
                }
            }
            if (!exists)
                event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m help\n -m help <command>").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("help", 0)
                        .setAlias("?")
                        .setHelp("shows this little helpy message");
    }

    private static EmbedBuilder msg(EmbedBuilder builder){
        for (String s : ChatHandler.chatCommands.keySet()) {
            String out = "";
            for (ChatCommand c : ChatHandler.chatCommands.get(s)) {
                Chat.CommandInfo cmdInfo = c.commandInfo();

                out += "**" + cmdInfo.command + "** - " + cmdInfo.help + " - *[" + Chat.permLevel(cmdInfo.permissionLevel).toUpperCase() + "|Lv." + cmdInfo.permissionLevel + "]*\n";
            }
            builder.addField(s.toUpperCase(), out, false);
        }
                builder.addField("", "Loaded a total of " + ChatHandler.size() + " commands.", false);
        return builder;
    }

    private static String msgToXml(){
        StringBuilder out = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<commands>\n");
        for (String s : ChatHandler.chatCommands.keySet()) {
            out.append("\t<section name=\"" + s + "\">\n\n");
            for (ChatCommand c : ChatHandler.chatCommands.get(s)) {
                Chat.CommandInfo cmdInfo = c.commandInfo();
                out .append("\t\t<command>\n")
                    .append("\t\t\t<name>" + cmdInfo.command + "</name>\n")
                    .append("\t\t\t<syntax>" + cmdInfo.syntax + "</syntax>\n")
                    .append("\t\t\t<info>" + cmdInfo.help + "</info>\n")
                    .append("\t\t\t<premium>\n")
                    .append("\t\t\t\t<isPremium>" + cmdInfo.isPremium  +"</isPremium>\n")
                    .append("\t\t\t\t<permission>" + cmdInfo.premiumPermission + "</permission>\n")
                    .append("\t\t\t\t<name>" + cmdInfo.premiumPermissionName + "</name>\n")
                    .append("\t\t\t</premium>\n")
                    .append("\t\t\t<permissionLevel>" + cmdInfo.permissionLevel + "</permissionLevel>\n")
                    .append("\t\t</command>\n\n");
            }
            out.append("\t</section>\n\n");
        }
        out.append("</commands>");

        return out.toString();
    }

    /**
     * get the markdown of the message for github
     * @param args
     */

    public static void main(String[] args){
        Importer.importChatCommands();
        //System.out.println(msgToXml());

        EmbedBuilder msg = msg(new EmbedBuilder());
        String out = "";
        for (MessageEmbed.Field f : msg.getFields()){
            out += "#### " + f.getName() + "\n";
            out +="Command | Information | Permission Level\n---- | ---- | ----\n";
            out += f.getValue().replace("|", "/").replace("-", "|") + "\n\n";
        }
        System.out.println(out);
    }

}
