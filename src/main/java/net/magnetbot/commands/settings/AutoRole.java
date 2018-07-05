package net.magnetbot.commands.settings;
/*
    Created by nils on 25.02.2018 at 16:14.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.core.sql.GuildSQL;

public class AutoRole implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        if (syntax.getExecutedBuilder() == 0){
            Role r = syntax.getAsRole("role");
            if (r.getPosition() < event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).getRoles().get(0).getPosition()) {
                GuildSQL.fromGuild(event.getGuild()).setAutoRole(r.getId());
                event.getTextChannel().sendMessage(Message.INFO(event, "Auto Role has been set to ``" + r.getName() + "`` !").build()).queue();
            } else {
                event.getTextChannel().sendMessage(Message.ERROR(event, "My Role has to be higher than the Auto Role").build()).queue();
            }
        } else {
            GuildSQL.fromGuild(event.getGuild()).setAutoRole("");
            event.getTextChannel().sendMessage(Message.INFO(event, "Auto Role has been disabled!").build()).queue();
        }
    }

    @Override
    public Permission[] requiredBotPerms() {
        return new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    public Permission[] requiredUserPerms() {
       return new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("autorole", PermissionLevel.ADMIN)
                        .setSyntax(
                                new SyntaxBuilder(
                                        new SyntaxBuilder().addElement("role", SyntaxElementType.ROLE),
                                        new SyntaxBuilder().addSubcommand("mode", "disable")
                                )
                        )
                    .setHelp("set a role every member will get when joining this server");
    }

    public static class Listener extends ListenerAdapter {

        @Override
        public void onGuildMemberJoin(GuildMemberJoinEvent event) {
            Role r = null;
            try {
                r = GuildSQL.fromGuild(event.getGuild()).getAutoRole();
                if (r != null) {
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), r).queue();
                }


            } catch (IllegalArgumentException e) { }

            catch (HierarchyException e){
                event.getGuild().getDefaultChannel().sendMessage("Sorry, but I am not able to auto-role your members.\nPlease put my role above **" + r.getName() + "**, and it will work again.").queue();
            }


        }
    }

}
