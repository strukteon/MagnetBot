package commands.chat.commands.general;
/*
    Created by nils on 30.12.2017 at 04:07.
    
    (c) nils 2017
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.PlayerInfo;
import core.tools.AutoComplete;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WhoIs implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length != 0) {
            if (AutoComplete.member(event.getGuild().getMembers(), args[0]) != null) {

                Member member = AutoComplete.member(event.getGuild().getMembers(), args[0]);

                event.getTextChannel().sendMessage(PlayerInfo.getBuilder(member).build()).queue();

            } else {
                event.getTextChannel().sendMessage(Message.ERROR(event, "Could not find a member called ``" + args[0] + "``").build()).queue();
            }
        } else {
            event.getTextChannel().sendMessage(Message.ERROR(event, "Illegal arguments").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("whois", 0)
                        .setHelp("gives you some informations about an user");
    }

    private String getGame(Member member){
        if (member.getGame() == null)
            return "none";
        return member.getGame().getName();
    }
    private String getNickname(Member member){
        if (member.getNickname() == null)
            return "none";
        return member.getNickname();
    }
}
