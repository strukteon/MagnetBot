package net.magnetbot.commands.admin;
/*
    Created by nils on 28.03.2018 at 21:39.
    
    (c) nils 2018
*/

import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.dv8tion.jda.core.Permission;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.utils.Static;

public class Kick implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        Member member = syntax.getAsMember("user");
        String reason = syntax.getAsString("reason");

        if (member.getRoles().get(0).getPosition() >= event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).getRoles().get(0).getPosition())

        if (reason != null){
            event.getGuild().getController().kick(member, reason).queue();
        } else
            event.getGuild().getController().kick(member).queue();

        EmbedBuilder b = new EmbedBuilder()
                .setColor(Static.Color.BLUE)
                .setTitle(":boot:  Kicked")
                .setDescription(event.getAuthor().getAsMention() + " kicked " + member.getUser().getAsMention());
        if (reason != null)
            b.addField("Reason:", reason, false);

        event.getTextChannel().sendMessage(b.build()).queue();
    }

    @Override
    public Permission[] requiredBotPerms() {
        return new Permission[]{Permission.KICK_MEMBERS};
    }

    @Override
    public Permission[] requiredUserPerms() {
        return new Permission[]{Permission.KICK_MEMBERS};
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("kick", PermissionLevel.SUPPORTER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("user", SyntaxElementType.MEMBER)
                                .addOptionalElement("reason", SyntaxElementType.STRING, true)
                )
                .setHelp("kick a user from the current server");
    }
}
