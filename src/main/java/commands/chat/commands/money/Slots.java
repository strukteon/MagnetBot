package commands.chat.commands.money;
/*
    Created by nils on 05.02.2018 at 03:17.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.UserData;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

public class Slots implements ChatCommand {

    private String[] slots = {":grapes:", ":watermelon:", ":cherries:", ":crown:", ":100:", ":four_leaf_clover:", ":bell:", ":strawberry:", ":gem:" };

    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("slots");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m slots <money>").build()).queue();
        else {
            try {
                int money = Integer.parseInt(args[0]);
                int currentMoney = UserData.getMoney(event.getAuthor().getId());

                if (currentMoney >= money) {
                    int[] result = new int[3];

                    for (int i = 0; i < 3; i++) {
                        result[i] = (int) Math.floor(Math.random() * slots.length);
                    }

                    boolean win = (result[0] == result[1] && result[1] == result[2]);

                    UserData.addMoney(event.getAuthor().getId(), (win ? money * 10 : 0) - money );

                    event.getTextChannel().sendMessage(Message.INFO(event, genSlots(result) + "\n" + (win ? "Yay! You won! :grin:\nYou got " + ((win ? money * 10 : 0) - money) + " m$" : "Sorry, you didn't win :cry:\nYou lost " + money + " m$")).build()).queue();
                } else
                    event.getTextChannel().sendMessage(Message.ERROR(event, "You don't have enough money!").build()).queue();
            } catch (NumberFormatException e) {
                event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m slots <money>").build()).queue();
            }
        }

    }

    @Override
    public String premiumPermission() {
        return null;
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

    @Override
    public int permissionLevel() {
        return 0;
    }
}
