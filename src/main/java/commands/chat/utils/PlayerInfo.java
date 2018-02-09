package commands.chat.utils;
/*
    Created by nils on 30.12.2017 at 18:45.
    
    (c) nils 2017
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerInfo {

    public static EmbedBuilder getBuilder(Member member){

        EmbedBuilder builder = commands.chat.tools.Message.INFO(member);
        builder .setThumbnail(getAvatarUrl(member.getUser()))
                .setAuthor(getName(member.getUser()), "https://magnet.strukteon.tk/", getAvatarUrl(member.getUser()))
                .addField("Name", getName(member.getUser()), true)
                .addField("Nickname", getNickname(member), true)
                .addField("Color", getHexColor(member, true), true)
                .addField("Unique ID", getId(member.getUser()), true)
                .addField("Status", getOnlineStatus(member), true)
                .addField("Current game", getGame(member), true)
                .addField("Avatar URL", getAvatarUrl(member.getUser()), false)
                .addField("Account creation date", getCreationDate(member.getUser(), "DD. MM. YYYY hh:mm"), true)
                .addField("Server join date", getJoinDate(member, "DD. MM. YYYY hh:mm"), true)
                .addField("Is a bot", Boolean.toString(isBot(member.getUser())), true)
                .addField("Roles", getRoles(member), false)
                .addField("Permissions", getPermissions(member), false);
        return builder;

    }

    public static User getUser(Member member){
        return member.getUser();
    }

    public static String getJoinDate(Member member, String format){
        format = format.replace("YYYY", "" + member.getJoinDate().getYear());
        format = format.replace("MM", "" + member.getJoinDate().getMonth().getValue());
        format = format.replace("DD", "" + member.getJoinDate().getDayOfMonth());
        format = format.replace("hh", "" + member.getJoinDate().getHour());
        format = format.replace("mm", "" + member.getJoinDate().getMinute());
        format = format.replace("ss", "" + member.getJoinDate().getSecond());
        return format;
    }

    public static VoiceState getVoiceState(Member member){
        return member.getVoiceState();
    }

    public static String getGame(Member member){
        return member.getGame() != null ? member.getGame().getName() : "none";
    }

    public static String getOnlineStatus(Member member){
        return member.getOnlineStatus().name();
    }

    public static String getNickname(Member member){
        return member.getNickname() != null ? member.getNickname() : "none";
    }

    public static String getRoles(Member member){
        String s = "";
        for (Role r : member.getRoles()){
            if (s != "")
                s += ", ";
            s += r.getName();
        }
        return s;
    }

    public static boolean hasRole(Member member, Role role){
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        return hasRoles(member, roleList);
    }

    public static boolean hasRoles(Member member, List<Role> roles){
        Role[] rolesList = new Role[roles.size()];
        rolesList = roles.toArray(rolesList);
        return hasRoles(member, rolesList);
    }

    public static boolean hasRoles(Member member, Role[] roles){
        return member.getRoles().containsAll(Arrays.asList(roles));
    }

    public static Color getColor(Member member){
        return member.getColor();
    }

    public static String getHexColor(Member member, boolean uppercase){
        String color;
        if (member.getColor() != null)
            color = "#" + Integer.toHexString(member.getColor().getRGB()).substring(2);
        else
            color = "none";
        if (uppercase)
            return color.toUpperCase();
        else
            return color.toLowerCase();
    }

    public static String getPermissions(Member member){
        String s = "";
        for (Permission p : member.getPermissions()){
            if (s != "")
                s += ", ";
            s += p.getName();
        }
        return s;
    }

    public static boolean hasPermissions(Member member, Permissions permissions){
        List<Permissions> permissionsList = new ArrayList<>();
        permissionsList.add(permissions);
        return hasPermissions(member, permissionsList);
    }

    public static boolean hasPermissions(Member member, List<Permissions> permissions){
        Permissions[] permissionsList = new Permissions[permissions.size()];
        permissionsList = permissions.toArray(permissionsList);
        return hasPermissions(member, permissionsList);
    }

    public static boolean hasPermissions(Member member, Permissions[] permissions){
        return member.getPermissions().containsAll(Arrays.asList(permissions));
    }

    public static boolean isOwner(Member member){
        return member.isOwner();
    }

    public static String getDefaultChannel(Member member){
        return member.getDefaultChannel().getName();
    }

    public static String getName(User user){
        return user.getName();
    }

    public static String getId(User user){
        return user.getId();
    }

    public static String getCreationDate(User user, String format){
        format = format.replace("YYYY", "" + user.getCreationTime().getYear());
        format = format.replace("MM", "" + user.getCreationTime().getMonth().getValue());
        format = format.replace("DD", "" + user.getCreationTime().getDayOfMonth());
        format = format.replace("hh", "" + user.getCreationTime().getHour());
        format = format.replace("mm", "" + user.getCreationTime().getMinute());
        format = format.replace("ss", "" + user.getCreationTime().getSecond());
        return format;
    }

    public static String getAvatarUrl(User user){
        return user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();
    }

    public static String getDefaultAvatarUrl(User user){
        return user.getDefaultAvatarUrl();
    }

    public static boolean isBot(User user){
        return user.isBot();
    }

}
