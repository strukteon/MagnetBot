package net.magnetbot.core.tools;
/*
    Created by nils on 07.02.2018 at 02:21.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class Tools {

    public static String argsToString(String[] args, String filler){
        StringBuilder out = new StringBuilder();
        for (String s : args){
            if (out.length() != 0)
                out.append(filler);
            out.append(s);
        }
        return out.toString();
    }

    public static String listToString(List<String> list){
        return listToString(list, " ");
    }

    public static String listToString(List<String> list, String filler){
        StringBuilder out = new StringBuilder();
        for (String s : list){
            if (out.length() != 0)
                out.append(filler);
            out.append(s);
        }
        return out.toString();
    }

    public static boolean isUrl(String url){
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static String stacktraceToString(StackTraceElement[] stackTrace, int limit, boolean indent){
        StringBuilder out = new StringBuilder();
        int len = (stackTrace.length > limit ? limit : stackTrace.length);
        for (int i = 0; i < len; i++ ){
            if (out.length() != 0)
                out.append("\n");
            out.append((indent?"    ":"") + "at " + stackTrace[i].toString());
        }
        return out.toString();
    }

    public static String coolify(String s){
        String out = "";
        for (String str : s.split("")){
            if (str.equals(" "))
                out += " :large_blue_diamond:";
            else
                out += " :regional_indicator_" + str.toLowerCase() + ":";
        }
        return out;
    }

    public static boolean canModify(Role r){
        return ! (r.getPosition() >= r.getGuild().getMemberById(r.getJDA().getSelfUser().getId()).getRoles().get(0).getPosition());
    }

    public static boolean isId(String toCheck){
        return toCheck.matches("\\d{18}");
    }

    public static boolean isMention(String toCheck){
        return toCheck.matches("<@(!)?\\d{18}>");
    }

    public static boolean isChannelMention(String toCheck){
        return toCheck.matches("<#(!)?\\d{18}>");
    }

    public static boolean isRoleMention(String toCheck){
        return toCheck.matches("<@&\\d{18}>");
    }

    public static String mentionToId(String mention){
        return mention.replaceAll("<@(!)?|>", "");
    }

    public static String channelMentionToId(String mention){
        return mention.replaceAll("<#(!)?|>", "");
    }

    public static String roleToId(String mention){
        return mention.replaceAll("<#&|>", "");
    }

    public static String getIntName(int i){
        if (i < 0 || i > 9)
            return "invalid";
        String[] intWords = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        return intWords[i];
    }

}
