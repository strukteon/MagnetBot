package net.magnetbot.core.sql;
/*
    Created by nils on 30.04.2018 at 12:09.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.magnetbot.MagnetBot;
import net.magnetbot.utils.Static;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserSQL {
    private static MySQL mySQL;
    private static String table = "users";
    private static String[] columns = {"id", "bio", "money", "permissions", "lastdaily", "badges", "command_count"};
    private static String[] columnTypes = {"text", "bigint", "text", "text", "text", "int"};

    private String userid;

    public static void init(MySQL mySQL){
        UserSQL.mySQL = mySQL;

        if (!mySQL.TABLE_EXISTS(table))
            mySQL.CREATE_TABLE(table, columns);

        mySQL.MATCH_COLUMNS(table, columns, columnTypes);
    }


    private UserSQL(String userid){
        this.userid = userid;
        if (!exists())
            create();
    }


    public static UserSQL fromUserId(String userid){
        return new UserSQL(userid);
    }


    public static UserSQL fromUser(User user){
        return fromUserId(user.getId());
    }


    public static UserSQL fromMember(Member member){
        return fromUser(member.getUser());
    }


    public boolean exists(){
        return mySQL.SELECT("*", table, "id='"+userid+"'").size() != 0;
    }

    public void create(){
        mySQL.INSERT(table, "`id`, `bio`, `money`, `permissions`, `lastdaily`, `badges`, `command_count`", String.format("'%s', 'no bio set', 0, '', '%s', '', 0", userid, Static.DATE_FORMAT.format(new Date(0))));
    }


    public String getBio(){
        return mySQL.SELECT("*", table, "id='"+userid+"'").get("bio");
    }

    public Date getLastDaily(){
        try {
            return Static.DATE_FORMAT.parse(mySQL.SELECT("*", table, "id='"+userid+"'").get("lastdaily"));
        } catch (ParseException e) {
            return new Date(0L);
        }
    }

    public int getMoney(){
        try {
            return Integer.parseInt(mySQL.SELECT("*", table, "id='" + userid + "'").get("money"));
        } catch (NumberFormatException e){
            return 0;
        }
    }

    public List<String> getPermissions(){
        String perms = mySQL.SELECT("*", table, "id='"+userid+"'").get("permissions");
        return Arrays.asList(perms.split(" "));
    }

    public List<Badge> getBadges(){
        List<Badge> badges = new ArrayList<>();
        String content = mySQL.SELECT("*", table, "id='"+userid+"'").get("badges");
        if (!"".equals(content))
            Arrays.asList(mySQL.SELECT("*", table, "id='"+userid+"'").get("badges").split(" "))
                    .forEach(s -> badges.add(Badge.valueOf(s)));
        return badges;
    }

    public long getCommandCount(){
        return Long.parseLong(mySQL.SELECT("*", table, "id='"+userid+"'").get("command_count"));
    }


    public void increaseCommandCount(){
        mySQL.UPDATE(table, "command_count=command_count+1", "id='"+userid+"'");
    }

    public void setBio(String bio){
        mySQL.UPDATE(table, "bio='"+bio.replace("'", "\\'")+"'", "id='"+userid+"'");
    }

    public void setMoney(int money){
        mySQL.UPDATE(table, "money="+money, "id='"+userid+"'");
    }

    public void setLastDaily(Date date){
        mySQL.UPDATE(table, "lastdaily='"+Static.DATE_FORMAT.format(date)+"'", "id='"+userid+"'");
    }

    public void setBadges(List<Badge> badges){
        StringBuilder b = new StringBuilder();
        badges.forEach(badge -> b.append(badge.name()).append(" "));
        b.deleteCharAt(b.length()-1);
        mySQL.UPDATE(table, "badges='"+b.toString()+"'", "id='"+userid+"'");
    }

    public void setPermissions(List<String> permissions){
        StringBuilder perms = new StringBuilder();
        permissions.forEach(s -> perms.append(s+" "));
        mySQL.UPDATE(table, "permissions='"+perms.toString()+"'", "id='"+userid+"'");
    }

    public boolean hasPermission(String permission){
        return getPermissions().contains(permission);
    }

    public void addPermission(String permission){
        if (!hasPermission(permission)) {
            List<String> permissions = getPermissions();
            permissions.add(permission);
            setPermissions(permissions);
        }
    }

    public void removePermission(String permission){
        if (hasPermission(permission)) {
            List<String> permissions = getPermissions();
            permissions.remove(permission);
            setPermissions(permissions);
        }
    }

    public enum Badge {
        DEVELOPER("Developer", "One of the core developers of the bot", Static.Emotes.BADGE_DEVELOPER),
        STAFF("Staff", "Staff member of the bot", Static.Emotes.BADGE_STAFF),
        BETA_USER("Beta user", "Used this bot before june 2018", Static.Emotes.BADGE_BETA);

        private String title;
        private String description;
        private String emoteId;

        Badge(String title, String description, String emoteId){
            this.title = title;
            this.description = description;
            this.emoteId = emoteId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Emote getEmote(){
            return MagnetBot.getJDA().getEmoteById(emoteId);
        }
    }

}
