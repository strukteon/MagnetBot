package net.magnetbot.core.sql;
/*
    Created by nils on 30.04.2018 at 12:09.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.magnetbot.utils.Static;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserSQL {
    private static MySQL mySQL;
    private static String table = "users";
    private static String[] columns = {"id", "bio", "money", "permissions", "lastdaily"};

    private String userid;

    public static void init(MySQL mySQL){
        UserSQL.mySQL = mySQL;

        if (!mySQL.TABLE_EXISTS(table))
            mySQL.CREATE_TABLE(table, columns);

        mySQL.MATCH_COLUMNS(table, columns);
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
        mySQL.INSERT(table, "`id`, `bio`, `money`, `permissions`, `lastdaily`", String.format("'%s', 'no bio set', 0, '', '%s'", userid, Static.DATE_FORMAT.format(new Date(0))));
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


    public void setBio(String bio){
        mySQL.UPDATE(table, "bio='"+bio.replace("'", "\\'")+"'", "id='"+userid+"'");
    }

    public void setMoney(int money){
        mySQL.UPDATE(table, "money="+money, "id='"+userid+"'");
    }

    public void setLastDaily(Date date){
        mySQL.UPDATE(table, "lastdaily='"+Static.DATE_FORMAT.format(date)+"'", "id='"+userid+"'");
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
}
