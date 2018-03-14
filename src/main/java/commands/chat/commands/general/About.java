package commands.chat.commands.general;
/*
    Created by nils on 10.02.2018 at 00:46.
    
    (c) nils 2018
*/

import commands.chat.core.Chat;
import commands.chat.core.ChatCommand;
import commands.chat.tools.Message;
import commands.chat.utils.GeneralData;
import core.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Static;

public class About implements ChatCommand {

    @Override
    public void action(MessageReceivedEvent event, String cmd, String[] args, String[] rawArgs) throws Exception {
        EmbedBuilder builder = Message.INFO(event);
        JDA jda = event.getJDA();


        builder .setTitle(":information_source: About " + jda.getSelfUser().getName() + "#" + jda.getSelfUser().getDiscriminator())
                .setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
                .addField("Id", jda.getSelfUser().getId(), true)
                .addField("Version", GeneralData.getVersion(), true)
                .addField("Last commit", "``" + GeneralData.getLastCommit() + "``", false)
                .addField("Commands executed", ""+GeneralData.getCommandsHandled(), true)
                .addField("Created by", "<@262951897290244096> (strukteon#7237)", true)
                .addField("Official repository", "[strukteon/MagnetBot](https://github.com/strukteon/MagnetBot)", false)
                .addField("API Wrapper", "JDA [View on GitHub](https://github.com/DV8FromTheWorld/JDA)", false)
                .addField("Java classes", Static.JAVA_CLASSES, true)
                .addField("Lines of code", Static.CODE_LINES, true)
                .addField("Servers", jda.getGuilds().size()+"", true)
                .addField("Members", jda.getUsers().size()+"", true)
                .addField("Voicechannels", jda.getVoiceChannels().size()+"", true)
                .addField("Textchannels", jda.getTextChannels().size()+"", true);
System.out.println(Main.commandsHandled);
        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return
                new Chat.CommandInfo("about", 0)
                        .setHelp("shows some infos about this bot");
    }
}
