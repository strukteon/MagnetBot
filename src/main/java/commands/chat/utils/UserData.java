package commands.chat.utils;
/*
    Created by nils on 11.02.2018 at 23:47.
    
    (c) nils 2018
*/

import utils.UserSQL;

import java.util.HashMap;

public class UserData {

    private static UserSQL userSQL;

    public static void init(UserSQL usersql) throws Exception {
        userSQL = usersql;

        userSQL.setData(
                new UserSQL.Column("id", ""),
                new UserSQL.Column("money", "0"),
                new UserSQL.Column("bio", "")
        );

        userSQL.setTable("users");

        userSQL.createTableIfNotExists();

        userSQL.updateColumns(true);

    }

    public static void updateUser(String id, UserSQL.Column.Change... columns) throws Exception {
        userSQL.createUserIfNotExists("id", id);
        userSQL.updateUser("id", id, columns);
    }

    public static HashMap<String, String> getUser(String id) throws Exception {
        return userSQL.getUser("id", id);
    }

    public static UserSQL getUserSQL() {
        return userSQL;
    }
}
