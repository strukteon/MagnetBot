package commands.chat.utils;
/*
    Created by nils on 16.02.2018 at 17:24.
    
    (c) nils 2018
*/

import net.dv8tion.jda.core.entities.Role;
import utils.Static;
import utils.UserSQL;

import java.util.HashMap;

public class GuildData {

    private static UserSQL userSQL;

    public static void init(UserSQL usersql) throws Exception {
        userSQL = usersql;

        userSQL.setData(
                new UserSQL.Column("id", ""),
                new UserSQL.Column("autorole", ""),
                new UserSQL.Column("prefix", null)
        );

        userSQL.setTable("guilds");

        userSQL.createTableIfNotExists();

        userSQL.updateColumns(true);

    }

    public static void updateGuild(String id, UserSQL.Column.Change... columns) throws Exception {
        userSQL.createUserIfNotExists("id", id);
        userSQL.updateUser("id", id, columns);
    }

    public static HashMap<String, String> getGuild(String id) throws Exception {
        userSQL.createUserIfNotExists("id", id);
        return userSQL.getUser("id", id);
    }

    public static UserSQL getUserSQL() {
        return userSQL;
    }


    public static void setAutoRole(String id, String roleid) throws Exception {
        updateGuild(id, new UserSQL.Column.Change("autorole", roleid));
    }

    public static String getAutoRole(String id) throws Exception {

        HashMap<String, String> guild = getGuild(id);
        return guild.get("autorole");

    }


    public static void setPrefix(String id, String prefix) throws Exception {
        updateGuild(id, new UserSQL.Column.Change("prefix", prefix));
    }

    public static String getPrefix(String id) throws Exception {
        HashMap<String, String> guild = getGuild(id);
        return guild.get("prefix");
    }

}
