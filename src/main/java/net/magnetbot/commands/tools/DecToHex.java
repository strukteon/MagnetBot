package net.magnetbot.commands.tools;
/*
    Created by nils on 04.03.2018 at 19:28.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DecToHex implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String parsed = Integer.toHexString(syntax.getAsInt("decimal"));
        event.getTextChannel().sendMessage(Message.INFO(event, "Decimal :arrow_forward: Hexadecimal", "The decimal number ``" + syntax.getAsInt("decimal") + "`` equals the hexadecimal number ``" + parsed + "``").build()).queue();

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("dectohex", PermissionLevel.MEMBER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("decimal", SyntaxElementType.INT)
                )
                .setAlias(
                        "dec2hex",
                        "inttohex",
                        "int2hex")
                .setHelp("convert a decimal number to a hexadecimal number");
    }

}
