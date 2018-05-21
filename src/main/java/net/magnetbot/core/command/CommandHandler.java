package net.magnetbot.core.command;
/*
    Created by nils on 30.04.2018 at 17:52.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxHandler;
import net.magnetbot.core.command.syntax.SyntaxValidateException;
import net.magnetbot.core.sql.PremiumSQL;
import net.magnetbot.core.sql.UserSQL;
import net.magnetbot.core.tools.Tools;
import net.magnetbot.utils.Static;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandHandler {
    public static HashMap<String, List<Command>> commands = new HashMap<>();

    public static void handleInput(MessageReceivedEvent event, String full){

        String cmd = full.toLowerCase().split(" ")[0];

        String[] args = full.toLowerCase().replaceFirst(cmd + (full.split(" ").length > 1 ? " " : ""), "").split(" ");

        String[] rawArgs = full.replaceFirst(cmd + (full.split(" ").length > 1 ? " " : ""), "").split(" ");

        testCommands(event, cmd, args, rawArgs);
    }


    public static void testCommands(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs){
        try {
            for (List<Command> commandList : commands.values()) {
                for (Command command : commandList) {
                    Chat.CommandInfo cmdInfo = command.commandInfo();

                    if (cmdInfo.command.equals(cmd) || cmdInfo.cmdAlias.contains(cmd)) {

                        try {
                            UserSQL userSQL = UserSQL.fromUser(event.getAuthor());
                            userSQL.increaseCommandCount();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        boolean premiumOk = !cmdInfo.isPremium || PremiumSQL.fromUser(event.getAuthor()).isPremium();
                        boolean permsOk = Chat.permissionLevel(event) >= cmdInfo.permissionLevel;

                        if (premiumOk && permsOk) {

                            if (cmdInfo.syntaxBuilder == null)
                                cmdInfo.setSyntax(new SyntaxBuilder());


                            cmdInfo.syntaxBuilder.setErrorHandler(new SyntaxHandler() {
                                @Override
                                public boolean onException(SyntaxValidateException e) {
                                    event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, cmdInfo.syntaxBuilder.getAsHelp(cmd)).build()).queue();
                                    for (SyntaxValidateException ex : e.getExceptions())
                                        System.out.println(ex);
                                    return false;
                                }

                                @Override
                                public void onFinish(Syntax syntax) {
                                    try {
                                        command.action(event, syntax);
                                        event.getMessage().delete().queue();
                                    } catch (Exception e) {
                                        submitError(event, cmd, e);
                                        e.printStackTrace();
                                    }

                                }
                            }).build(cmd, rawArgs, event);

                        } else if (!permsOk && premiumOk)
                            event.getTextChannel().sendMessage(invalidPermLevel(event, Chat.permissionLevel(event), cmdInfo.permissionLevel).build()).queue();

                        else if (permsOk)
                            event.getTextChannel().sendMessage(
                                    Message.ERROR(event, "It seems like you need Premium to execute this command. You can get it by voting (``-m vote``) or by donating on [patron](https://patreon.com/strukteon)")
                                            .build()).queue();

                    }


                }
            }
        } catch (Exception e){
            e.printStackTrace();
            submitError(event, cmd, e);
        }
    }

    public static EmbedBuilder invalidPermLevel(MessageReceivedEvent event, int curLvl, int reqLvl){
        return Message.ERROR(event, "You **do not have the required permissions** to execute that command\n Required Permission level: ``" +
                Chat.permLevel(reqLvl) + "``\n Your Permission level: ``" + Chat.permLevel(curLvl) + "``", false);
    }

    private static void submitError(MessageReceivedEvent event, String cmd, Exception e){
        EmbedBuilder error = Message.INFO(event);
        error.setTitle("**An internal error ocurred**")
                .setDescription("Command: "+cmd)
                .setColor(Static.Color.RED)
                .addField("Guild", event.getGuild().getName(), true)
                .addField("Guild ID", event.getGuild().getId(), true)
                .addField("Error Message", e.toString(), false)
                .addField("Stacktrace", Tools.stacktraceToString(e.getStackTrace(), 4, false), false);
        event.getJDA().getUserById(Static.BOT_OWNER_ID).openPrivateChannel().complete().sendMessage(error.build()).queue();
        event.getTextChannel().sendMessage(Message.INTERNAL_ERROR(event, e).build()).queue();
        e.printStackTrace();
    }

    public CommandHandler addSection(String section, Command... chatCommands){
        CommandHandler.commands.put(section, Arrays.asList(chatCommands));
        return this;
    }

    public static int size(){
        int i = 0;
        for (String s : commands.keySet())
            for (Command c : commands.get(s))
                i++;
        return i;
    }

}
