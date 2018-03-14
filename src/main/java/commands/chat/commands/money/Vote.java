package commands.chat.commands.money;
/*
    Created by nils on 13.02.2018 at 19:28.
    
    (c) nils 2018
*/

import com.google.api.client.util.DateTime;
import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.UserData;
import core.Main;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.http.client.utils.DateUtils;
import org.discordbots.api.client.entity.SimpleUser;
import utils.Static;
import utils.UserSQL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Vote implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        User u = event.getAuthor();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date lastVote = dateFormat.parse(UserData.getUser(u.getId()).get("lastvote"));
        System.out.println(lastVote.toString());
        Date now = new Date();

        if (lastVote.before(new Date(now.getTime() + 24 * 60 * 60 * 1000))){

            List<String> voters = Main.discordBotListAPI.getVoterIds("389016516261314570", 1);
            System.out.println(voters);
            System.out.println(u.getId());

            if (voters.contains(u.getId())){

                UserData.updateUser(u.getId(), new UserSQL.Column.Change("lastvote", dateFormat.format(now)));
                UserData.addMoney(u.getId(), Static.Money.VOTE_REWARD);

                event.getTextChannel().sendMessage(Message.INFO(event, ":gem: You sucessfully voted and received your rewards!\n ```+500 m$```\nVote again tomorrow to get another reward!").build()).queue();

            } else
                event.getTextChannel().sendMessage(Message.ERROR(event, "You haven't voted today! You can vote now on [discordbotlist.org](https://discordbots.org/bot/389016516261314570/vote)!", false).build()).queue();


        } else
            event.getTextChannel().sendMessage(Message.ERROR(event, "You already voted today! Vote again tomorrow!").build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("vote", 0)
                        .setHelp("vote for the bot on [discordbotlist.org](https://discordbots.org/bot/389016516261314570/vote) and get a reward");
    }
}
