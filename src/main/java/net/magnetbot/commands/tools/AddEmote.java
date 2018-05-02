package net.magnetbot.commands.tools;
/*
    Created by nils on 20.04.2018 at 18:17.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.core.command.Chat;
import net.magnetbot.core.command.Command;
import net.magnetbot.core.command.PermissionLevel;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.tools.Tools;

import java.util.HashMap;
import java.util.List;

public class AddEmote extends ListenerAdapter implements Command{
    private static HashMap<String, ReactionInfo> reactions = new HashMap<>();
    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        Emote e = null;
        Message m = event.getTextChannel().getMessageById(syntax.getAsString("messageid")).complete();
        if (m == null){
            event.getTextChannel().sendMessage(net.magnetbot.core.command.Message.WRONG_SYNTAX(event, "The message with id ``" + syntax.getAsString("messageid") +
            "`` couldn't be found. Please check if the message is in this channel and try again.\nSyntax:\n"+syntax.getHelp()).build()).queue();
            return;
        }
        switch (syntax.getExecutedBuilder()){
            case 0:
                Guild g = event.getJDA().getGuildById(syntax.getAsString("guildid"));
                if (g == null){
                    event.getTextChannel().sendMessage(net.magnetbot.core.command.Message.WRONG_SYNTAX(event, "The guild with id ``" + syntax.getAsString("guildid") +
                            "`` couldn't be found. Please check if I am in the guild.\nSyntax:\n"+syntax.getHelp()).build()).queue();
                    return;
                }
                List<Emote> emotes = g.getEmotesByName(Tools.listToString(syntax.getAsListString("emotename"), " "), true);
                if (emotes.size() == 0){
                    event.getTextChannel().sendMessage(net.magnetbot.core.command.Message.WRONG_SYNTAX(event, "There do not exist any emotes with the name ``" + Tools.listToString(syntax.getAsListString("emotename"), " ") +
                            "``.\nSyntax:\n"+syntax.getHelp()).build()).queue();
                    return;
                }
                e = emotes.get(0);
                m.addReaction(e).queue();
                break;
            case 1:
                e = event.getJDA().getEmoteById(syntax.getAsString("emoteid"));
                if (e == null){
                    event.getTextChannel().sendMessage(net.magnetbot.core.command.Message.WRONG_SYNTAX(event, "The emote with id ``" + syntax.getAsString("emoteid") +
                            "`` couldn't be found. Please check if I am in the guild of the emote.\nSyntax:\n"+syntax.getHelp()).build()).queue();
                    return;
                }
                m.addReaction(e).queue();
                break;
        }
        reactions.put(m.getId(), new ReactionInfo(e, event.getAuthor().getId()));
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (reactions.containsKey(event.getMessageId())){
            ReactionInfo info = reactions.get(event.getMessageId());

            if (info.userId.equals(event.getUser().getId()) && info.emote.getId().equals(event.getReactionEmote().getId())){
                event.getReaction().removeReaction(event.getJDA().getSelfUser()).queue();
                reactions.remove(event.getMessageId());
            }

        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("addemote", PermissionLevel.MEMBER)
                .setAlias("emoteadd", "emote")
                .setHelp("Let this bot react to a message so you can also do so")
                .setSyntax(
                        new SyntaxBuilder(
                                new SyntaxBuilder()
                                        .addElement("messageid", SyntaxElementType.ID)
                                        .addElement("guildid", SyntaxElementType.ID)
                                        .addElement("emotename", SyntaxElementType.STRING, true),
                                new SyntaxBuilder()
                                        .addElement("messageid", SyntaxElementType.ID)
                                        .addElement("emoteid", SyntaxElementType.ID)
                        )
                );
    }

    private static class ReactionInfo {
        Emote emote;
        String userId;
        private ReactionInfo(Emote emote, String userId){
            this.emote = emote;
            this.userId = userId;
        }
    }
}
