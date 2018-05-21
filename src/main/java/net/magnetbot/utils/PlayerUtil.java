package net.magnetbot.utils;
/*
    Created by nils on 20.05.2018 at 00:40.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.User;
import net.magnetbot.MagnetBot;
import net.magnetbot.core.sql.PremiumSQL;

public class PlayerUtil {

    public static String getUserAndDiscrim(String id){
        return getUserAndDiscrim(MagnetBot.getJDA().getUserById(id));
    }

    public static String getUserAndDiscrim(User u){
        PremiumSQL sql = PremiumSQL.fromUserId(u.getId());
        return u.getName() + "#" + u.getDiscriminator();
    }

    public static boolean isDev(String id){
        return Static.DEVELOPERS.contains(id);
    }

}
