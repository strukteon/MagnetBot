package net.magnetbot.commands.general;
/*
    Created by nils on 03.01.2018 at 22:03.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.Importer;
import net.magnetbot.core.command.*;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.utils.Static;

public class Help implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String cmd = syntax.getAsString("command");
        if (cmd == null) {
            EmbedBuilder builder = shortMsg(Message.INFO(event, ":information_source: Help", "Look at my **[documentation](https://magnetbot.net/documentation)**!\nFor a more detailed description about a command, type ``"+ Static.PREFIX+"help <command>``"));
            event.getTextChannel().sendMessage(builder.build()).queue();
        } else {
            boolean exists = false;
            for (String section : CommandHandler.commands.keySet()) {
                for (Command c : CommandHandler.commands.get(section)) {
                    Chat.CommandInfo cmdInfo = c.commandInfo();
                    if (cmdInfo.command.equals(cmd)) {
                        exists = true;
                        EmbedBuilder builder = Message.INFO(event, ":information_source: Help", "Detailed informations about the command ``" + cmd + "``");
                        StringBuilder alias = new StringBuilder();
                        for (String s : cmdInfo.cmdAlias) {
                            if (alias.length() != 0)
                                alias.append("\n ");
                            alias.append(s);
                        }

                        StringBuilder syntax_ = new StringBuilder();
                        if (cmdInfo.syntaxBuilder == null)
                            cmdInfo.setSyntax(new SyntaxBuilder());

                        for (SyntaxBuilder b : cmdInfo.syntaxBuilder.getAlternateBuilders()) {
                            if (syntax_.length() != 0)
                                syntax_.append(" \n ");
                            syntax_.append(cmd).append(" ").append(b);
                        }

                        String perms = Chat.permLevel(cmdInfo.permissionLevel) + "\t\t\t(You **can" + (Chat.permissionLevel(event) >= cmdInfo.permissionLevel?"":"not") + "** execute it)";

                        builder.addField("Information", "``" + (!cmdInfo.help.isEmpty()?cmdInfo.help:"no description yet") + "``", false);
                        if (alias.length() != 0)
                            builder.addField("Alias", "``" + alias.toString() + "``", false);
                        builder
                                .addField("Syntax", "``" + syntax_ + "``", false)
                                .addField("Permission Level", perms, false)
                                .addField("Premium Only", "" + (cmdInfo.isPremium?"Yes":"No"), true);
                        event.getTextChannel().sendMessage(builder.build()).queue();
                    }
                }
            }
            if (!exists)
                event.getTextChannel().sendMessage(Message.ERROR(event, "Sorry, but the command ``" + cmd + "`` couldn't be found.", false).build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("help", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addOptionalElement("command", SyntaxElementType.STRING)
                        )
                        .setAlias("?")
                        .setHelp("shows this little helpy message");
    }

    private static EmbedBuilder msg(EmbedBuilder builder){
        for (String s : CommandHandler.commands.keySet()) {
            String out = "";
            for (Command c : CommandHandler.commands.get(s)) {
                Chat.CommandInfo cmdInfo = c.commandInfo();

                out += "**" + cmdInfo.command + "** - " + cmdInfo.help + " - *[" + Chat.permLevel(cmdInfo.permissionLevel).toUpperCase() + "|Lv." + cmdInfo.permissionLevel + "]*\n";
            }
            builder.addField(s.toUpperCase(), out, false);
        }
        builder.addField("", "Loaded a total of " + CommandHandler.size() + " commands.", false);
        return builder;
    }

    private static EmbedBuilder shortMsg(EmbedBuilder builder){
        for (String s : CommandHandler.commands.keySet()) {
            StringBuilder out = new StringBuilder();
            for (Command c : CommandHandler.commands.get(s)) {
                Chat.CommandInfo cmdInfo = c.commandInfo();
                if (out.length() != 0)
                    out.append(" ");
                out.append("``" + cmdInfo.command + "``");
            }
            builder.addField(s.toUpperCase(), out.toString(), false);
        }
        builder.addField("", "Loaded a total of ``" + CommandHandler.size() + "`` commands.", false);
        return builder;
    }

    private static String msgToXml(){
        StringBuilder out = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<commands>\n");
        for (String s : CommandHandler.commands.keySet()) {
            out.append("\t<section name=\"" + s + "\">\n\n");
            for (Command c : CommandHandler.commands.get(s)) {
                Chat.CommandInfo cmdInfo = c.commandInfo();
                try {
                    out.append("\t\t<command>\n")
                            .append("\t\t\t<name>" + cmdInfo.command + "</name>\n")
                            .append("\t\t\t<syntax>" + cmdInfo.command + " " + cmdInfo.syntaxBuilder.toString() + "</syntax>\n")
                            .append("\t\t\t<info>" + cmdInfo.help + "</info>\n")
                            .append("\t\t\t<premium>\n")
                            .append("\t\t\t\t<isPremium>" + cmdInfo.isPremium + "</isPremium>\n")
                            .append("\t\t\t</premium>\n")
                            .append("\t\t\t<permissionLevel>" + cmdInfo.permissionLevel + "</permissionLevel>\n")
                            .append("\t\t</command>\n\n");
                } catch (Exception e){
                    e.printStackTrace();
            }
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

        Importer.importCommands();
        System.out.println(msgToXml());

        /*StringBuilder out = new StringBuilder();
        for (MessageEmbed.Field f : msg.getFields()){
            out.append("#### " + f.getName() + "\n")
            .append("Command | Information | Permission Level\n---- | ---- | ----\n")
            .append(f.getValue().replace("|", "/").replace("-", "|") + "\n\n");
        }
        System.out.println(out);*/
    }

}
