package net.magnetbot.commands.settings;
/*
    Created by nils on 19.02.2018 at 22:27.
    
    (c) nils 2018
*/
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.sql.GuildSQL;

public class Prefix implements Command {

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        String prefix = syntax.getAsString("prefix");
        GuildSQL guildSQL = GuildSQL.fromGuild(event.getGuild());
        if (prefix == null){
            prefix = guildSQL.getPrefix();
            if (prefix.equals(""))
                event.getTextChannel().sendMessage(Message.INFO(event, "There is no custom prefix set for this server.\nYou can set one with ``-m prefix <prefix>``").build()).queue();
            else
                event.getTextChannel().sendMessage(Message.INFO(event, "The custom prefix set on this server is: ``" + prefix + Character.toString((char)8199) + "``").build()).queue();
        }
        else {
            guildSQL.setPrefix(prefix);
            event.getTextChannel().sendMessage(Message.INFO(event, "The custom prefix has been set to: ``" + prefix + "``").build()).queue();
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("prefix", PermissionLevel.ADMIN)
                        .setSyntax(
                                new SyntaxBuilder()
                                        .addOptionalElement("prefix", SyntaxElementType.STRING)
                        )
                        .setHelp("set a custom prefix for this server");
    }
}
