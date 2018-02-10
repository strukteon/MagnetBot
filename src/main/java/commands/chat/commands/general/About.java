package commands.chat.commands.general;
/*
    Created by nils on 10.02.2018 at 00:46.
    
    (c) nils 2018
*/

import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class About implements ChatCommand {
    @Override
    public boolean execute(MessageReceivedEvent event, String full, String cmd, String[] args) {
        return cmd.equals("about");
    }

    @Override
    public void action(MessageReceivedEvent event, String full, String cmd, String[] args) throws Exception {
        EmbedBuilder builder = Message.INFO(event);
        JDA jda = event.getJDA();


        builder .setTitle(":information_source: About " + jda.getSelfUser().getName() + "#" + jda.getSelfUser().getDiscriminator())
                .setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
                .addField("Id", jda.getSelfUser().getId(), false)
                .addField("Created by", "<@262951897290244096> (strukteon#7237)", false)
                .addField("Official repository", "[strukteon/MagnetBot](https://github.com/strukteon/MagnetBot)", false)
                .addField("API Wrapper", "JDA [View on GitHub](https://github.com/DV8FromTheWorld/JDA)", false)
                .addField("Servers", jda.getGuilds().size()+"", true)
                .addField("Members", jda.getUsers().size()+"", true)
                .addField("Voicechannels", jda.getVoiceChannels().size()+"", true)
                .addField("Textchannels", jda.getTextChannels().size()+"", true);

        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String premiumPermission() {
        return null;
    }

    @Override
    public int permissionLevel() {
        return 0;
    }
}
