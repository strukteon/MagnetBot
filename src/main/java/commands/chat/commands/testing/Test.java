package commands.chat.commands.testing;
/*
    Created by nils on 12.03.2018 at 23:16.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Webhook;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;
import utils.Static;

public class Test implements ChatCommand {
    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
/*
        Webhook webhook = event.getGuild().getWebhooks().complete().get(1);
        WebhookClient client = webhook.newClient().build();

        WebhookMessageBuilder builder = new WebhookMessageBuilder();

        builder.addEmbeds(new EmbedBuilder().setDescription("some cool text").build())
                .setUsername("Magnet")
                .setAvatarUrl(event.getJDA().getSelfUser().getAvatarUrl());

        client.send(builder.build());
        client.close();
*/
    EmbedBuilder builder = new EmbedBuilder();
    builder
            .setDescription("***Thank you** for adding me to your server!* You can begin to  ``-m play`` some *music*, or type ``-m help`` to get a *list of every command* available with the bot. You can join our **[Support Server](https://discord.gg)** if you have any *questions*. Anyways, have fun using this bot!")
            .setColor(Static.Color.DISCORD_COLOR);
    event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("test", 3);
    }
}
