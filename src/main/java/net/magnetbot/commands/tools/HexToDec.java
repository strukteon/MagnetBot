package net.magnetbot.commands.tools;
/*
    Created by nils on 04.03.2018 at 19:05.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HexToDec implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        try {
            int parsed = Integer.parseInt(syntax.getAsString("hexadecimal"), 16);
            event.getTextChannel().sendMessage(Message.INFO(event, "Hexadecimal :arrow_forward: Decimal", "The hexadecimal number ``" + syntax.getAsString("hexadecimal") + "`` equals the decimal number ``" + parsed + "``").build()).queue();
        } catch (NumberFormatException e) {
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m hextodec <hexadecimal>").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("hextodec", PermissionLevel.MEMBER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("hexadecimal", SyntaxElementType.STRING)
                )
                .setAlias(
                        "hex2dec",
                        "hextoint",
                        "hex2int")
                .setHelp("convert a hexadecimal number to a decimal number");
    }
}
