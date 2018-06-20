package net.magnetbot.core.sql;
/*
    Created by nils on 30.04.2018 at 19:37.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.magnetbot.utils.Static;

import java.text.ParseException;
import java.util.Date;

public class PremiumSQL {
    private static MySQL mySQL;
    private static String table = "premiums";
    private static String[] columns = {"id", "premiumtype", "streak", "lastvote"};

    private String userid;

    public static void init(MySQL mySQL){
        PremiumSQL.mySQL = mySQL;

        if (!mySQL.TABLE_EXISTS(table))
            mySQL.CREATE_TABLE(table, columns);

        mySQL.MATCH_COLUMNS(table, columns);
    }


    private PremiumSQL(String userid){
        this.userid = userid;
        if (!exists())
            create();
    }


    public static PremiumSQL fromUserId(String userid){
        return new PremiumSQL(userid);
    }

    public static PremiumSQL fromUser(User user){
        return fromUserId(user.getId());
    }

    public static PremiumSQL fromMember(Member member){
        return fromUser(member.getUser());
    }


    public boolean exists(){
        return mySQL.SELECT("*", table, "id='"+userid+"'").size() != 0;
    }

    public void create(){
        mySQL.INSERT(table, "`id`, `premiumtype`, `streak`, `lastvote`", String.format("'%s', 'none', 0, '%s'", userid, Static.DATE_FORMAT.format(new Date(0))));
    }


    public PremiumType getPremiumType(){
        String premiumType = mySQL.SELECT("*", table, "id='"+userid+"'").get("premiumtype");
        if ("patron".equals(premiumType))
            return PremiumType.PATRON;
        else if ("voter".equals(premiumType))
            return PremiumType.VOTER;
        return PremiumType.NONE;
    }

    public boolean isPremium(){
        return !getPremiumType().equals(PremiumType.NONE);
    }

    public boolean isPatron(){
        return getPremiumType().equals(PremiumType.PATRON);
    }

    public int getStreak(){
        try {
            return Integer.parseInt(mySQL.SELECT("*", table, "id='" + userid + "'").get("streak"));
        } catch (NumberFormatException e){
            return 0;
        }
    }

    public Date getLastVote(){
        try {
            return Static.DATE_FORMAT.parse(mySQL.SELECT("*", table, "id='" + userid + "'").get("lastvote"));
        } catch (ParseException e) {
            return new Date(0);
        }
    }


    public enum PremiumType {
        NONE,
        VOTER,
        PATRON
    }

}
