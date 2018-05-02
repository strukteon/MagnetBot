package net.magnetbot.core.command;
/*
    Created by nils on 30.04.2018 at 19:22.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.magnetbot.core.command.syntax.SyntaxBuilder;
import net.magnetbot.utils.Static;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Chat {

    public static int permissionLevel(MessageReceivedEvent event){
        Member member = event.getMember();
        if (member.getUser().getId().equals(Static.BOT_OWNER_ID))
            return PermissionLevel.BOT_OWNER;
        if (member.isOwner()){
            return PermissionLevel.OWNER;
        } else if (member.getPermissions(event.getTextChannel()).contains(Permission.ADMINISTRATOR)){
            return PermissionLevel.ADMIN;
        } else {
            return PermissionLevel.MEMBER;
        }
    }

    public static String permLevel(int level){
        return getName(level);
    }

    public static Map<Integer, String> names;

    public static String getName(int pLvl){
        names.entrySet().forEach(integerStringEntry -> System.out.println(integerStringEntry.getKey() + ": " + integerStringEntry.getValue()));
        return names.get(pLvl);
    }


    public static class CommandInfo {

        public final String command;
        public List<String> cmdAlias = new ArrayList<>();

        public String help;
        public SyntaxBuilder syntaxBuilder = null;

        public final int permissionLevel;

        public boolean isPremium = false;

        public CommandInfo(String command, int permissionLevel){
            this(command, permissionLevel, "");
        }

        public CommandInfo(String command, int permissionLevel, String help){
            this.help = help;
            this.command = command;
            this.permissionLevel = permissionLevel;
        }

        public CommandInfo setHelp(String message){
            this.help = message;
            return this;
        }

        public CommandInfo setAlias(String... alias){
            cmdAlias = Arrays.asList(alias);
            return this;
        }

        public CommandInfo setAlias(List<String> alias){
            this.cmdAlias = alias;
            return this;
        }

        public CommandInfo setSyntax(SyntaxBuilder builder){
            this.syntaxBuilder = builder;
            return this;
        }

        public CommandInfo setPremium(boolean isPremium){
            this.isPremium = isPremium;
            return this;
        }

    }

}
