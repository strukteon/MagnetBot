package net.magnetbot.core.command.syntax;
/*
    Created by nils on 02.04.2018 at 17:52.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.tools.Tools;

import java.util.List;

public class SyntaxValidator {

    public Object validate(String toValidate, SyntaxElement element, MessageReceivedEvent event) throws SyntaxValidateException {
        if (toValidate.isEmpty())
            throw new SyntaxValidateException(SyntaxValidateException.Cause.EMPTY);
        switch (element.getType()){
            case INT:
                return validateInt(toValidate);
            case STRING:
                return validateString(toValidate);
            case LONG:
                return validateLong(toValidate);
            case ID:
                return validateId(toValidate);
            case USER:
                return validateUser(toValidate, event.getJDA());
            case MEMBER:
                return validateMember(toValidate, event.getGuild());
            case GUILD:
                return validateGuild(toValidate, event.getJDA());
            case TEXTCHANNEL:
                return validateTextChannel(toValidate, event.getGuild());
            case VOICECHANNEL:
                return validateVoiceChannel(toValidate, event.getGuild());
            case ROLE:
                return validateRole(toValidate, event.getGuild());
            case STRING_OF_LIST:
                return validateSubCommand(toValidate, ((SyntaxElement.SubCommand) element).getPossibilities());
            default:
                throw new SyntaxValidateException(SyntaxValidateException.Cause.UNDEFINED);
        }

    }


    public int validateInt(String toValidate) throws SyntaxValidateException {
        try {
            return Integer.parseInt(toValidate);
        } catch (Exception e){
            throw new SyntaxValidateException(SyntaxValidateException.Cause.INVALID);
        }
    }

    public long validateLong(String toValidate) throws SyntaxValidateException {
        try {
            return Long.parseLong(toValidate);
        } catch (Exception e){
            throw new SyntaxValidateException(SyntaxValidateException.Cause.INVALID);
        }
    }

    public String validateString(String toValidate) throws SyntaxValidateException {
        if (toValidate.isEmpty())
            throw new SyntaxValidateException(SyntaxValidateException.Cause.EMPTY);
        return toValidate;
    }

    public String validateId(String toValidate) throws SyntaxValidateException {
        if (!toValidate.matches("[0-9]+"))
            throw new SyntaxValidateException(SyntaxValidateException.Cause.INVALID);
        return toValidate;
    }

    public User validateUser(String toValidate, JDA jda) throws SyntaxValidateException {
        if (!Tools.isMention(toValidate) || !Tools.isId(toValidate))
            throw new SyntaxValidateException(SyntaxValidateException.Cause.INVALID);
        User user = jda.getUserById(Tools.mentionToId(toValidate));
        if (user == null)
            throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
        return user;
    }

    public Member validateMember(String toValidate, Guild guild) throws SyntaxValidateException {
        Member member = null;
        if (Tools.isMention(toValidate))
            member = guild.getMemberById(Tools.mentionToId(toValidate));
        if (member == null)
            if (Tools.isMention(toValidate))
                throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
            else {
                List<Member> possibles = guild.getMembersByName(toValidate, true);
                if (possibles.size() == 0) {
                    for (Member m : guild.getMembers())
                        if (m.getEffectiveName().toLowerCase().contains(toValidate.toLowerCase()))
                            return m;
                    throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
                }
                member = possibles.get(0);
            }
        return member;
    }

    public Guild validateGuild(String toValidate, JDA jda) throws SyntaxValidateException {
        if (!Tools.isId(toValidate))
            throw new SyntaxValidateException(SyntaxValidateException.Cause.INVALID);
        Guild guild = jda.getGuildById(toValidate);
        if (guild == null)
            throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
        return guild;
    }

    public TextChannel validateTextChannel(String toValidate, Guild guild) throws SyntaxValidateException {
        TextChannel textChannel = null;
        if (Tools.isChannelMention(toValidate) || Tools.isId(toValidate))
            textChannel = guild.getTextChannelById(Tools.channelMentionToId(toValidate));
        if (textChannel == null)
            if (Tools.isMention(toValidate) || Tools.isId(toValidate))
                throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
            else {
                List<TextChannel> possibles = guild.getTextChannelsByName(toValidate, true);
                if (possibles.size() == 0) {
                    for (TextChannel c : guild.getTextChannels())
                        if (c.getName().toLowerCase().contains(toValidate.toLowerCase()))
                            return c;
                    throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
                }
                textChannel = possibles.get(0);
            }
        return textChannel;
    }

    public VoiceChannel validateVoiceChannel(String toValidate, Guild guild) throws SyntaxValidateException {
        VoiceChannel voiceChannel = null;
        if (Tools.isId(toValidate))
            voiceChannel = guild.getVoiceChannelById(Tools.channelMentionToId(toValidate));
        if (voiceChannel == null)
            if (Tools.isId(toValidate))
                throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
            else {
                List<VoiceChannel> possibles = guild.getVoiceChannelsByName(toValidate, true);
                if (possibles.size() == 0) {
                    for (VoiceChannel c : guild.getVoiceChannels())
                        if (c.getName().toLowerCase().contains(toValidate.toLowerCase()))
                            return c;
                    throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
                }
                voiceChannel = possibles.get(0);
            }
        return voiceChannel;
    }

    public Role validateRole(String toValidate, Guild guild) throws SyntaxValidateException {
        Role role = null;
        if (Tools.isRoleMention(toValidate) || Tools.isId(toValidate))
            role = guild.getRoleById(Tools.roleToId(toValidate));
        if (role == null)
            if (Tools.isMention(toValidate) || Tools.isId(toValidate))
                throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
            else {
                List<Role> possibles = guild.getRolesByName(toValidate, true);
                if (possibles.size() == 0) {
                    for (Role r : guild.getRoles())
                        if (r.getName().contains(toValidate))
                            return r;
                    throw new SyntaxValidateException(SyntaxValidateException.Cause.NOT_FOUND);
                }
                role = possibles.get(0);
            }
        return role;
    }

    public SubCommand validateSubCommand(String toValidate, List<String> possibilities) throws SyntaxValidateException {
        if (possibilities.contains(toValidate))
            return new SubCommand(toValidate, possibilities.indexOf(toValidate));
        throw new SyntaxValidateException(SyntaxValidateException.Cause.INVALID);
    }
}
