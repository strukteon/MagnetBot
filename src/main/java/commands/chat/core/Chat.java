package commands.chat.core;
/*
    Created by nils on 30.12.2017 at 02:48.
    
    (c) nils 2017
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Static;

import java.awt.*;
import java.sql.Statement;

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

}
