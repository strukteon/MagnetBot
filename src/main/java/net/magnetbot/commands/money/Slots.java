package net.magnetbot.commands.money;
/*
    Created by nils on 05.02.2018 at 03:17.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.sql.UserSQL;

public class Slots implements Command {

    private String[] slots = {":grapes:", ":watermelon:", ":cherries:", ":crown:"};

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        int money = syntax.getAsInt("money");
        UserSQL userSQL = UserSQL.fromUser(event.getAuthor());
        int currentMoney = userSQL.getMoney();

        if (currentMoney >= money) {
            int[] result = new int[3];

            for (int i = 0; i < 3; i++) {
                result[i] = (int) Math.floor(Math.random() * slots.length);
            }

            boolean win = (result[0] == result[1] && result[1] == result[2]);

            userSQL.setMoney(currentMoney + (win ? money * 10 : 0) - money);

            event.getTextChannel().sendMessage(Message.INFO(event, genSlots(result) + "\n" + (win ? "Yay! You won! :grin:\nYou got " + (money * 10 - money) + " m$" : "Sorry, you didn't win :cry:\nYou lost " + money + " m$")).build()).queue();
        } else
            event.getTextChannel().sendMessage(Message.ERROR(event, "You don't have enough money!").build()).queue();

    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("slots", PermissionLevel.MEMBER)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addElement("money", SyntaxElementType.INT)
                        )
                        .setHelp("spin that machine and win money");
    }

    private String genSlots(int[] result){
        System.out.println(slots.length);
        String s = "";
        for (int i = 0; i < 3; i++){
            if (i != 0)
                s += "   ";
            s += (result[i] - 1 < 0 ? slots[slots.length - 1] : slots[result[i]-1]);
        }
        s += "\n**";
        for (int i = 0; i < 3; i++){
            if (i != 0)
                s += " : ";
            s += slots[result[i]];
        }
        s += "**\n";
        for (int i = 0; i < 3; i++){
            if (i != 0)
                s += "   ";
            s += (result[i] +1 > slots.length-1 ? slots[0] : slots[result[i]+1]);
        }
        return s;
    }
}
