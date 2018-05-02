package net.magnetbot.commands.money;
/*
    Created by nils on 07.04.2018 at 23:41.
    
    (c) nils 2018
*/

import com.google.api.client.util.DateTime;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.sql.UserSQL;

import java.util.Date;

public class DailyReward implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        int dailyReward = 300;
        UserSQL userSQL = UserSQL.fromUser(event.getAuthor());

        if (userSQL.getLastDaily().before(new Date(System.currentTimeMillis()-24*60*60*1000))){
            userSQL.setMoney(userSQL.getMoney() + dailyReward);
            event.getTextChannel().sendMessage(Message.INFO(event, "You got your daily reward! " + dailyReward + "m$ have been added to your account!").build()).queue();
            userSQL.setLastDaily(new Date());
        } else {
            long wait = System.currentTimeMillis() - userSQL.getLastDaily().getTime();
            long hours = wait/(60*60*1000);
            long minutes = (wait-hours*60*60*1000)/60/1000;
            event.getTextChannel().sendMessage(Message.INFO(event, "You already got your reward today! You can check in again in " + (hours>0?hours + "h ":"")+ minutes + "min!").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("daily", PermissionLevel.MEMBER)
                .setHelp("get your daily login reward");
    }
}
