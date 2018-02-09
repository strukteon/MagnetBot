package commands.chat.commands.general;
/*
    Created by nils on 30.12.2017 at 03:21.
    
    (c) nils 2017
*/

import commands.chat.core.ChatCommand;
import commands.chat.utils.PlayerInfo;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WhoAmI implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("whoami");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {
        event.getTextChannel().sendMessage(PlayerInfo.getBuilder(event.getMember()).build()).queue();
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 0;
    }

    private String getGame(Member author){
        if (author.getGame() == null)
            return "none";
        return author.getGame().getName();
    }
    private String getNickname(Member author){
        if (author.getNickname() == null)
            return "none";
        return author.getNickname();
    }
}
