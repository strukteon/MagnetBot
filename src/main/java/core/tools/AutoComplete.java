package core.tools;
/*
    Created by nils on 30.12.2017 at 02:09.
    
    (c) nils 2017
*/

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class AutoComplete {

    public static Guild guild(List<Guild> guilds, String s){
        Guild[] _guilds = new Guild[guilds.size()];
        _guilds = guilds.toArray(_guilds);
        return guild(_guilds, s);
    }

    public static Guild guild(Guild[] guilds, String s){
        for (Guild g : guilds){
            if (g.getName().toLowerCase().contains(s.toLowerCase())){
                return g;
            }
        }
        return null;
    }


    public static Member member(List<Member> members, String s){
        Member[] _members = new Member[members.size()];
        _members = members.toArray(_members);
        return member(_members, s);
    }

    public static Member member(Member[] members, String s){
        if (s.equals("") || s == null)
            return null;
        for (Member m : members){
            if (m.getUser().getName().toLowerCase().contains(s.toLowerCase())){
                return m;
            } else if (s.replaceFirst("!", "").equals(m.getAsMention()))
                return m;
            else if (m.getNickname() != null)
                if (m.getNickname().toLowerCase().contains(s.toLowerCase()))
                    return m;
        }
        return null;
    }


    public static Role role(List<Role> roles, String s){
        Role[] _roles = new Role[roles.size()];
        _roles = roles.toArray(_roles);
        return role(_roles, s);
    }

    public static Role role(Role[] roles, String s){
        if (s.equals("") || s == null)
            return null;
        for (Role r : roles){
            if (r.getName().toLowerCase().contains(s) || r.getAsMention().equals(s) ){
                return r;
            }
        }
        return null;
    }

}
