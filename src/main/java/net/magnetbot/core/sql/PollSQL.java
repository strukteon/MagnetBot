package net.magnetbot.core.sql;
/*
    Created by nils on 01.05.2018 at 01:16.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.magnetbot.MagnetBot;
import net.magnetbot.commands.tools.Poll;
import net.magnetbot.core.tools.Tools;
import net.magnetbot.utils.Static;

import javax.tools.Tool;
import java.util.*;
import java.util.regex.Pattern;

public class PollSQL {
    private static MySQL mySQL;
    private static String table = "polls";
    private static String[] columns = {"id", "msgid", "question", "options", "votes"};

    private String userid;

    public static void init(MySQL mySQL){
        PollSQL.mySQL = mySQL;

        if (!mySQL.TABLE_EXISTS(table))
            mySQL.CREATE_TABLE(table, columns);

        mySQL.MATCH_COLUMNS(table, columns);
    }


    private PollSQL(String userid){
        this.userid = userid;
    }


    public static PollSQL fromUserId(String userid){
        return new PollSQL(userid);
    }

    public static PollSQL fromUser(User user){
        return fromUserId(user.getId());
    }

    public static PollSQL fromMember(Member member){
        return fromUser(member.getUser());
    }

    public static PollSQL createNew(String userid, String msgid, String question, List<String> options){
        mySQL.INSERT(table, "`id`, `msgid`, `question`, `options`, `votes`", String.format("'%s', '%s', '%s', '%s', ''", userid, msgid, question.replace('\'', '"'), Tools.listToString(options, "|").replace('\'', '"')));
        return fromUserId(userid);
    }

    public static PollSQL fromMessageId(String msgid){
        return PollSQL.fromUserId(mySQL.SELECT("*", table, "msgid='"+msgid+"'").get("id"));
    }


    public boolean exists(){
        return mySQL.SELECT("*", table, "id='"+userid+"'").size() != 0;
    }


    public User getAuthor(){
        return MagnetBot.getJDA().getUserById(userid);
    }

    public String getQuestion(){
        return mySQL.SELECT("*", table, "id='"+userid+"'").get("question");
    }

    public List<String> getOptions(){
        return Arrays.asList(mySQL.SELECT("*", table, "id='"+userid+"'").get("options").split(Pattern.quote("|")));
    }

    public HashMap<Integer, List<String>> getVotes(){
        HashMap<Integer, List<String>> votes = new HashMap<>();
        try {
            for (String us : mySQL.SELECT("*", table, "id='" + userid + "'").get("votes").split(" ")) {
                String[] split = us.split(":");
                int pos = Integer.parseInt(split[0]);
                String userid = split[1];
                if (!votes.containsKey(pos))
                    votes.put(pos, new ArrayList<>());
                List<String> voters = votes.get(pos);
                voters.add(userid);
            }
        } catch (NumberFormatException ignored) { }
        return votes;
    }

    public String getMessageId(){
        return mySQL.SELECT("*", table, "id='"+userid+"'").get("msgid");
    }

    public void addVote(int pos, String uid){
        HashMap<Integer, List<String>> votes = getVotes();
        if (!votes.containsKey(pos))
            votes.put(pos, new ArrayList<>());
        List<String> voters = votes.get(pos);
        voters.add(uid);
        StringBuilder out = new StringBuilder();
        for (Map.Entry<Integer, List<String>> entry :votes.entrySet())
            for (String user : entry.getValue())
                out.append(entry.getKey() + ":" + user + " ");
        mySQL.UPDATE(table, "`votes`='"+out.toString()+"'", "id='"+userid+"'");
    }

    public boolean hasVoted(String userid){
        HashMap<Integer, List<String>> votes = getVotes();
        for (List<String> users : votes.values())
            if (users.contains(userid))
                return true;
        return false;
    }

    public void close(){
        mySQL.DELETE(table, "id='"+userid+"'");
    }
}
