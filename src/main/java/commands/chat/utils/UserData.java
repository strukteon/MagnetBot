package commands.chat.utils;
/*
    Created by nils on 11.02.2018 at 23:47.
    
    (c) nils 2018
*/

import utils.UserSQL;

import java.util.HashMap;
import java.util.List;

public class UserData {

    private static UserSQL userSQL;

    public static void init(UserSQL usersql) throws Exception {
        userSQL = usersql;

        userSQL.setData(
                new UserSQL.Column("id", ""),
                new UserSQL.Column("money", "0"),
                new UserSQL.Column("bio", ""),
                new UserSQL.Column("lastvote", "01.01.2000 00:00:00")
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
        userSQL.createUserIfNotExists("id", id);
        return userSQL.getUser("id", id);
    }

    public static UserSQL getUserSQL() {
        return userSQL;
    }


    public static int getMoney(String id) throws Exception {
        try {
            return Integer.parseInt(getUser(id).get("money"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void addMoney(String id, int money) throws Exception {
        int current;
        try {
            current = Integer.parseInt(getUser(id).get("money"));
        } catch (NumberFormatException e) {
            current = 0;
        }

        updateUser(id, new UserSQL.Column.Change("money", "" + ( current + money )));
    }


    // Permissions

    public static List<String> getPermissions(String userid){

    }

    public static boolean hasPermission(String userid, String permission){



    }
}
