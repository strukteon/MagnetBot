package net.magnetbot.commands.admin;
/*
    Created by nils on 28.03.2018 at 19:04.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.Syntax;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.core.command.syntax.SyntaxElementType;
import net.magnetbot.utils.Static;

public class Ban implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {

        int days = syntax.getAsInt("days");
        Member member = syntax.getAsMember("user");
        String reason = syntax.getAsString("reason");

        if (reason != null)
            event.getGuild().getController().ban(member, days, reason).queue();
        else
            event.getGuild().getController().ban(member, days).queue();

        EmbedBuilder b = new EmbedBuilder()
                .setColor(Static.Color.BLUE)
                .setTitle(":no_entry_sign:  Banned")
                .setDescription(event.getAuthor().getAsMention() + " banned " + member.getUser().getAsMention() + " for **" + days + " days**");
        if (reason != null)
            b.addField("Reason:", reason, false);

        event.getTextChannel().sendMessage(b.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("ban", PermissionLevel.SUPPORTER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("user", SyntaxElementType.MEMBER)
                                .addElement("days", SyntaxElementType.INT)
                                .addOptionalElement("reason", SyntaxElementType.STRING, true)
                )
                .setHelp("ban a user from the current server");
    }
}
