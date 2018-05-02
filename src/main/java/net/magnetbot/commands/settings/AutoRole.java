package net.magnetbot.commands.settings;
/*
    Created by nils on 25.02.2018 at 16:14.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.core.sql.GuildSQL;
import net.magnetbot.utils.Static;

import java.awt.*;

public class AutoRole implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        Role r = syntax.getAsRole("role");
        if (r.getPosition() < event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).getRoles().get(0).getPosition()) {
            GuildSQL.fromGuild(event.getGuild()).setAutoRole(r.getId());
            event.getTextChannel().sendMessage(Message.INFO(event, "Auto Role has been set to ``" + r.getName() + "`` !").build()).queue();
        } else {
            event.getTextChannel().sendMessage(Message.ERROR(event, "My Role has to be higher than the Auto Role").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("autorole", PermissionLevel.ADMIN)
                        .setSyntax(
                                new SyntaxBuilder().addElement("role", SyntaxElementType.ROLE)
                        )
                    .setHelp("set a role every member will get when joining this server");
    }

    public static class Listener extends ListenerAdapter {

        @Override
        public void onGuildMemberJoin(GuildMemberJoinEvent event) {
            try {
                Role r = GuildSQL.fromGuild(event.getGuild()).getAutoRole();
                if (r != null) {
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), r).queue();
                }


            } catch (IllegalArgumentException e) { }

            catch (Exception e){
                e.printStackTrace();
                EmbedBuilder error = new EmbedBuilder();
                error.setTitle("**An internal error ocurred**")
                        .setDescription("tt")
                        .setColor(Color.RED)
                        .addField("Guild", event.getGuild().getName(), true)
                        .addField("Guild ID", event.getGuild().getId(), true)
                        .addField("Error Message", e.toString(), false)
                        .addField("Stacktrace", ""/*tools.stacktraceToString(e.getStackTrace(), false)*/, false);
                event.getJDA().getUserById(Static.BOT_OWNER_ID).openPrivateChannel().complete()
                        .sendMessage(error.build()).queue();
            }


        }
    }

}
