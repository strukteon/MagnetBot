package net.magnetbot.commands.admin;
/*
    Created by nils on 28.03.2018 at 22:04.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.utils.Static;

public class Unban implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {

        User user = syntax.getAsUser("user");

        for (Guild.Ban ban : event.getGuild().getBanList().complete())
            if (ban.getUser().getId().equals(user.getId())) {
                event.getGuild().getController().unban(user).queue();

                EmbedBuilder b = new EmbedBuilder()
                        .setColor(Static.Color.BLUE)
                        .setTitle(":ballot_box_with_check:  Unbanned")
                        .setDescription(event.getAuthor().getAsMention() + " unbanned " + user.getAsMention());

                event.getTextChannel().sendMessage(b.build()).queue();
                return;
            }

        EmbedBuilder b = Message.ERROR(event, "Cannot unban a user that isn't banned!");

        event.getTextChannel().sendMessage(b.build()).queue();
    }

    @Override
    public Permission[] requiredBotPerms() {
        return new Ban().requiredBotPerms();
    }

    @Override
    public Permission[] requiredUserPerms() {
       return new Ban().requiredUserPerms();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("unban", PermissionLevel.SUPPORTER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("user", SyntaxElementType.USER)
                )
                .setHelp("unban a user on the current server");
    }
}
