package commands.chat.commands.admin;
/*
    Created by nils on 25.02.2018 at 16:14.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.GuildData;
import core.tools.AutoComplete;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class AutoRole implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("autorole");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m autorole <role>").build()).queue();
        else {
            System.out.println(event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).getRoles().get(0));
            Role r = AutoComplete.role(event.getGuild().getRoles(), args[0]);
            if (r == null)
                event.getTextChannel().sendMessage(Message.ERROR(event, "Role ``" + args[0] + "`` couldn't be found").build()).queue();
            else {
                if (r.getPosition() < event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).getRoles().get(0).getPosition()) {
                    GuildData.setAutoRole(event.getGuild().getId(), r.getId());
                    event.getTextChannel().sendMessage(Message.INFO(event, "Auto Role has been set to ``" + r.getName() + "`` !").build()).queue();
                } else {
                    event.getTextChannel().sendMessage(Message.ERROR(event, "My Role has to be higher than the Auto Role").build()).queue();
                }
            }
        }
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 1;
    }

    public static class Listener extends ListenerAdapter {

        @Override
        public void onGuildMemberJoin(GuildMemberJoinEvent event) {
            try {
                Role r = event.getGuild().getRoleById(GuildData.getAutoRole(event.getGuild().getId()));
                System.out.println(r.getName());
                if (r == null){ }
                else {
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), r).queue();
                }


            } catch (Exception e){
                e.printStackTrace();
            }


        }
    }

}
