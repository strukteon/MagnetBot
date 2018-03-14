package commands.chat.core;
/*
    Created by nils on 30.12.2017 at 02:48.
    
    (c) nils 2017
*/

import commands.chat.utils.UserData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Static;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chat {

    public static int permissionLevel(MessageReceivedEvent event){
        Member member = event.getMember();
        if (member.getUser().getId().equals(Static.BOT_OWNER_ID))
            return 3;
        if (member.isOwner()){
            return 2;
        } else if (member.getPermissions(event.getTextChannel()).contains(Permission.ADMINISTRATOR)){
            return 1;
        } else {
            return 0;
        }
    }

    public static String permLevel(int level){
        switch (level){
            case -1:
                return "Guest";
            case 0:
                return "Member";
            case 1:
                return "Administrator";
            case 2:
                return "Server Owner";
            case 3:
                return "Bot Owner";
            default:
                return "Invalid Permission level";
        }
    }

    public static class CommandInfo {

        public final String command;
        public List<String> cmdAlias = new ArrayList<>();

        public String help;
        public String syntax;

        public final int permissionLevel;

        public boolean isPremium = false;
        public String premiumPermission = "*";
        public String premiumPermissionName = "[none]";

        public CommandInfo(String command, int permissionLevel){
            this(command, permissionLevel, "");
        }

        public CommandInfo(String command, int permissionLevel, String help){
            this.help = help;
            this.command = command;
            this.permissionLevel = permissionLevel;
            this.syntax = Static.PREFIX + command;
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

        public CommandInfo setPremium(String premiumPermission, String premiumPermissionName){
            this.isPremium = true;
            this.premiumPermission = premiumPermission;
            this.premiumPermissionName = premiumPermissionName;
            return this;
        }

        public CommandInfo setPremium(String premiumPermission){
            return setPremium(premiumPermission, premiumPermission);
        }

    }

}
