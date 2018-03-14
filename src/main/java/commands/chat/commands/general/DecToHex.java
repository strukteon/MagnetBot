package commands.chat.commands.general;
/*
    Created by nils on 04.03.2018 at 19:28.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DecToHex implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m dectohex <decimal>").build()).queue();
        else {
            try {
                String parsed = Integer.toHexString(Integer.parseInt(args[0]));
                event.getTextChannel().sendMessage(Message.INFO(event, "Decimal :arrow_forward: Hexadecimal", "The decimal number ``" + args[0] + "`` equals the hexadecimal number ``" + parsed + "``").build()).queue();
            } catch (NumberFormatException e) {
                event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m dectohex <decimal>").build()).queue();
            }
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("dectohex", 0)
                .setAlias(
                        "dec2hex",
                        "inttohex",
                        "int2hex")
                .setHelp("convert a decimal number to a hexadecimal number");
    }

}
