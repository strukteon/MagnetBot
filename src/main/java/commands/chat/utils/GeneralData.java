package commands.chat.utils;
/*
    Created by nils on 16.02.2018 at 17:24.
    
    (c) nils 2018
*/

import utils.UserSQL;

import java.util.HashMap;

public class GeneralData {

    private static UserSQL userSQL;

    public static void init(UserSQL usersql) throws Exception {
        userSQL = usersql;

        userSQL.setData(
                new UserSQL.Column("bot", "magnet"),
                new UserSQL.Column("build", ""),
                new UserSQL.Column("lastchanges", ""),
                new UserSQL.Column("commandshandled", "0")
        );

        userSQL.setTable("general");

        userSQL.createTableIfNotExists();

        userSQL.updateColumns(true);

    }

    public static void updateSettings(UserSQL.Column.Change... columns) throws Exception {
        userSQL.createUserIfNotExists("bot", "magnet");
        userSQL.updateUser("bot", "magnet", columns);
    }

    public static HashMap<String, String> getSettings() throws Exception {
        userSQL.createUserIfNotExists("bot", "magnet");
        return userSQL.getUser("bot", "magnet");
    }

    public static UserSQL getUserSQL() {
        return userSQL;
    }


    public static int getBuild() throws Exception {
        HashMap<String, String> settings = getSettings();
        return Integer.parseInt(settings.get("build"));
    }

    public static String getVersion() throws Exception {
        String build = "" + getBuild();
        String version = build.substring(0, build.length()-2) + "." + build.substring(build.length()-2, build.length()-1) + "." + build.substring(build.length()-1, build.length());
        return version;
    }

    public static String getLastCommit() throws Exception {
        HashMap<String, String> settings = getSettings();
        return settings.get("lastchanges");
    }

    public static int getCommandsHandled() throws Exception {
        HashMap<String, String> settings = getSettings();
        return Integer.parseInt(settings.get("commandshandled"));
    }

    public static void addCommandsHandled(int count) throws Exception {
        int all = getCommandsHandled() + count;
        updateSettings(new UserSQL.Column.Change("commandshandled", ""+all));
    }

}
