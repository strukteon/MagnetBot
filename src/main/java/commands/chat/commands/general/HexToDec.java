package commands.chat.commands.general;
/*
    Created by nils on 04.03.2018 at 19:05.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HexToDec implements ChatCommand {
    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m hextodec <hexadecimal>").build()).queue();
        else {
            try {
                int parsed = Integer.parseInt(args[0], 16);
                event.getTextChannel().sendMessage(Message.INFO(event, "Hexadecimal :arrow_forward: Decimal", "The hexadecimal number ``" + args[0] + "`` equals the decimal number ``" + parsed + "``").build()).queue();
            } catch (NumberFormatException e) {
                event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m hextodec <hexadecimal>").build()).queue();
            }
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("hextodec", 0)
                .setAlias(
                        "hex2dec",
                        "hextoint",
                        "hex2int")
                .setHelp("convert a hexadecimal number to a decimal number");
    }
}
