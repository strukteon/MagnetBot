package net.magnetbot.commands.tools;
/*
    Created by nils on 23.04.2018 at 22:33.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Webhook;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.WebhookAction;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.Message;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.tools.Tools;

import java.util.List;

public class AsBot implements Command {
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        List<Webhook> webhooks = event.getGuild().getWebhooks().complete();
        if (webhooks.size() == 0) {
            event.getTextChannel().sendMessage(Message.ERROR(event, "There is no Webhook available on this Server. You create one by going to ``Serversettings->WebHooks->Create Webhook``", false).build()).queue();
            return;
        }
        Webhook webhook = webhooks.get(0);
        Emote e = event.getJDA().getGuildById("332942033398267916").getEmotesByName("electron", true).get(0);

        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setAvatarUrl(event.getAuthor().getEffectiveAvatarUrl());
        builder.setUsername(event.getMember().getEffectiveName());
        builder.append(Tools.listToString(syntax.getAsListString("msg")).replaceAll("\\[\\{electron}]", "<"+(e.isAnimated()?"a":"")+e.getAsMention()+e.getId()+">"));

        webhook.newClient().build().send(builder.build());
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("asbot", PermissionLevel.BOT_OWNER)
                .setSyntax(
                        new SyntaxBuilder()
                                .addElement("msg", SyntaxElementType.STRING, true)
                );
    }
}
