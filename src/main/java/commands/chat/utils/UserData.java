package commands.chat.utils;
/*
    Created by nils on 11.02.2018 at 23:47.
    
    (c) nils 2018
*/

import utils.UserSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserData {

    private static UserSQL userSQL;

    public static void init(UserSQL usersql) throws Exception {
        userSQL = usersql;

        userSQL.setData(
                new UserSQL.Column("id", ""),
                new UserSQL.Column("money", "0"),
                new UserSQL.Column("bio", "no bio set"),
                new UserSQL.Column("lastvote", "01.01.2000 00:00:00"),
                new UserSQL.Column("permissions", "")
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

    public static List<String> getPermissions(String userid) throws Exception {
        HashMap<String, String> user = getUser(userid);
        String[] arr = user.get("permissions").split(" ");

        if (arr.length == 0)
            return new ArrayList<>();
        else
            return new ArrayList<>(Arrays.asList(arr));

    }

    public static boolean hasPermission(String userid, String permission) throws Exception {
        List<String> perms = UserData.getPermissions(userid);

        for (String s : perms){
            if(s.endsWith("*")){
                if (permission.startsWith(s.replace(".*", "")))
                    return true;
            } else if (s.equals(permission)){
                return true;
            }
        }
        return false;
    }

    public static void addPermission(String userid, String permission) throws Exception {

        if (!hasPermission(userid, permission)){

            List<String> perms = getPermissions(userid);

            String permsOut = "";
            for (String s : perms){
                if (perms.indexOf(s) != 0)
                    permsOut += " ";
                permsOut += s;
            }

            permsOut += " " + permission;

            updateUser(userid, new UserSQL.Column.Change("permissions", permsOut));

        }


    }

    public static void removePermission(String userid, String permission) throws Exception {

        if (hasPermission(userid, permission)){

            List<String> perms = getPermissions(userid);

            String permsOut = "";
            for (String s : perms){
                if (!s.equals(permission)) {
                    if (!permsOut.equals(""))
                        permsOut += " ";
                    permsOut += s;
                }
            }

            updateUser(userid, new UserSQL.Column.Change("permissions", permsOut));

        }

    }
}
