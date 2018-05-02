package net.magnetbot.commands.tools;
/*
    Created by nils on 23.04.2018 at 23:01.
    
    (c) nils 2018
*/


import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.magnetbot.commands.admin.Permission;
import net.magnetbot.core.command.*;
import net.magnetbot.core.command.syntax.*;
import net.magnetbot.core.sql.PollSQL;
import net.magnetbot.core.tools.Tools;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Poll extends ListenerAdapter implements Command {
    private static final String[] digits = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private static final String[] digitsReaction = {"\u0031\u20E3", "\u0032\u20E3", "\u0033\u20E3", "\u0034\u20E3", "\u0035\u20E3", "\u0036\u20E3", "\u0037\u20E3", "\u0038\u20E3", "\u0039\u20E3"};

    @Override
    public void action(MessageReceivedEvent event, Syntax syntax) throws Exception {
        switch (syntax.getExecutedBuilder()){
            case 0:
                if (PollSQL.fromUser(event.getAuthor()).exists()){
                    event.getTextChannel().sendMessage(Message.INFO(event, "You already have an open poll. Close it to open another one.").build()).queue();
                    return;
                }
                String[] splitted = Tools.listToString(syntax.getAsListString("question|answer1|answer2[|answer3...]")).split("\\|");

                if (splitted.length == 0){
                    event.getTextChannel().sendMessage(Message.WRONG_SYNTAX("Syntax: ``" + syntax.getHelp() + "``").build()).queue();
                    return;
                } else if (splitted.length < 3){
                    event.getTextChannel().sendMessage(Message.WRONG_SYNTAX("You have to specify at least 2 answers!").build()).queue();
                    return;
                } else if (splitted.length > 10){
                    event.getTextChannel().sendMessage(Message.WRONG_SYNTAX("You can specify a maximum of 9 answers!").build()).queue();
                    return;
                }
                String question = splitted[0];
                List<String> options = Arrays.asList(Arrays.copyOfRange(splitted, 1, splitted.length));

                StringBuilder optionsStr = new StringBuilder();
                for (int i = 0; i < options.size(); i++){
                    if (optionsStr.length() != 0)
                        optionsStr.append("\n");
                    optionsStr.append(":"+digits[i]+": - "+options.get(i)+" **Votes: ``0``**");
                }

                EmbedBuilder builder = new EmbedBuilder();
                builder
                        .setAuthor(event.getAuthor().getName() + "'s Poll", "https://magnetbot.net", event.getAuthor().getEffectiveAvatarUrl())
                        .setDescription(question)
                        .addField("Options", optionsStr.toString(), false);

                net.dv8tion.jda.core.entities.Message msg = event.getTextChannel().sendMessage(builder.build()).complete();

                PollSQL.createNew(event.getAuthor().getId(), msg.getId(), question, options);

                for (int i = 0; i < options.size(); i++) {
                    msg.addReaction(digitsReaction[i]).queue();
                }
                break;

            case 1:
                if (!PollSQL.fromUser(event.getAuthor()).exists()){
                    event.getTextChannel().sendMessage(Message.INFO(event, "You do not have an open poll.").build()).queue();
                    return;
                }
                PollSQL.fromUser(event.getAuthor()).close();
                event.getTextChannel().sendMessage(Message.INFO(event, "Your poll has been closed!").build()).queue();
                break;
            /*case 3:
                if (Chat.permissionLevel(event)>= PermissionLevel.BOT_ADMIN){
                    User u = syntax.getAsUser("user");
                    if (!PollSQL.fromUser(event.getAuthor()).exists()){
                        event.getTextChannel().sendMessage(Message.INFO(event,  u.getName() + " does not have an open poll.").build()).queue();
                        return;
                    }
                    PollSQL.fromUser(event.getAuthor()).close();
                    event.getTextChannel().sendMessage(Message.INFO(event, u.getName() + "'s vote has been closed!").build()).queue();
                } else {
                    event.getTextChannel().sendMessage(CommandHandler.invalidPermLevel(event, Chat.permissionLevel(event), PermissionLevel.BOT_ADMIN).build()).queue();
                }*/
        }
    }

    @Override
    public Chat.CommandInfo commandInfo() {
        return new Chat.CommandInfo("poll", PermissionLevel.MEMBER)
                .setSyntax(
                        new SyntaxBuilder(
                                new SyntaxBuilder()
                                        .addSubcommand("action", "create")
                                        .addElement("question|answer1|answer2[|answer3...]", SyntaxElementType.STRING, true)
                                ,
                                new SyntaxBuilder()
                                        .addSubcommand("action", "close")/*,
                                new SyntaxBuilder()
                                        .addSubcommand("action", "forceclose")
                                        .addElement("user", SyntaxElementType.USER)*/
                        )
                );
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!( event.getUser().getId().equals(event.getJDA().getSelfUser().getId()) || event.getUser().isFake() || event.getUser().isBot() )) {
            PollSQL pollSQL = PollSQL.fromMessageId(event.getMessageId());
            if (pollSQL.exists()) {
                User author = pollSQL.getAuthor();
                List<String> options = pollSQL.getOptions();
                String question = pollSQL.getQuestion();
                List<String> digits = Arrays.asList(digitsReaction);

                if (digits.contains(event.getReactionEmote().getName()) && !pollSQL.hasVoted(event.getUser().getId())) {
                    pollSQL.addVote(digits.indexOf(event.getReactionEmote().getName()), event.getUser().getId());
                    HashMap<Integer, List<String>> votes = pollSQL.getVotes();
                    
                    StringBuilder optionsStr = new StringBuilder();
                    for (int i = 0; i < options.size(); i++){
                        if (optionsStr.length() != 0)
                            optionsStr.append("\n");
                        optionsStr.append(":"+Poll.digits[i]+": - "+options.get(i)+" **Votes: ``"+(votes.get(i) != null ? votes.get(i).size() : 0)+"``**");
                    }

                    EmbedBuilder builder = new EmbedBuilder();
                    builder
                            .setAuthor(author.getName() + "'s Poll", "https://magnetbot.net", author.getEffectiveAvatarUrl())
                            .setDescription(question)
                            .addField("Options", optionsStr.toString(), false);
                    event.getTextChannel().getMessageById(event.getMessageId()).complete().editMessage(builder.build()).queue();
                }
                event.getReaction().removeReaction(event.getUser()).queue();
            }
        }
    }
}
