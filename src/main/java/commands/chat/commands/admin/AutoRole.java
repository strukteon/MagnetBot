package commands.chat.commands.admin;
/*
    Created by nils on 25.02.2018 at 16:14.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.GuildData;
import core.tools.AutoComplete;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class AutoRole implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        if (args.length == 1 && args[0].equals(""))
            event.getTextChannel().sendMessage(Message.WRONG_SYNTAX(event, "-m autorole <role>").build()).queue();
        else {
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
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("autorole", 1)
                    .setHelp("set a role every member will get when joining this server");
    }

    public static class Listener extends ListenerAdapter {

        @Override
        public void onGuildMemberJoin(GuildMemberJoinEvent event) {
            try {
                Role r = event.getGuild().getRoleById(GuildData.getAutoRole(event.getGuild().getId()));
                System.out.println(r.getName());
                if (r == null) {
                } else {
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), r).queue();
                }


            } catch (IllegalArgumentException e) {

            } catch (Exception e){
                e.printStackTrace();
                EmbedBuilder error = new EmbedBuilder();
                error.setTitle("**An internal error ocurred**")
                        .setDescription("tt")
                        .setColor(Color.RED)
                        .addField("Guild", event.getGuild().getName(), true)
                        .addField("Guild ID", event.getGuild().getId(), true)
                        .addField("Error Message", e.toString(), false)
                        .addField("Stacktrace", ""/*Tools.stacktraceToString(e.getStackTrace(), false)*/, false);
                event.getGuild().getOwner().getUser().openPrivateChannel().complete()
                        .sendMessage(error.build()).queue();
            }


        }
    }

}
