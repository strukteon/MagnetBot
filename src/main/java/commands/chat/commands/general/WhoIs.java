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
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("whois");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {
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
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 0;
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
