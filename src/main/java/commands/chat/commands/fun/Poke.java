package commands.chat.commands.fun;
/*
    Created by nils on 30.01.2018 at 20:19.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import core.tools.AutoComplete;
import net.dv8tion.jda.core.entities.Member;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Poke implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("poke");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) {
        if ((AutoComplete.member(event.getGuild().getMembers(), args[0]) != null)) {
            Member m = AutoComplete.member(event.getGuild().getMembers(), args[0]);
            PrivateChannel dm = m.getUser().openPrivateChannel().complete();

            dm.sendMessage(Message.INFO_RAW(":point_left: " + event.getAuthor().getAsMention() + " poked you!").build()).queue();

            dm.close().queue();
            event.getTextChannel().sendMessage(Message.INFO(event, ":information_source: Poked **" + m.getEffectiveName() + "**").build()).queue();
        } else
            event.getTextChannel().sendMessage(Message.ERROR(event, "Player " + args[0] + " not found").build()).queue();
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
