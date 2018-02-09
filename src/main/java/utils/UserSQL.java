package utils;
/*
    Created by nils on 01.02.2018 at 19:31.
    
    (c) nils 2018
*/

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserSQL {

    private static MysqlDataSource source = new MysqlDataSource();

    public static void login(String sqluser, String password, String database, String server){
        login(sqluser, password, database, server, 3306);
    }

    public static void login(String sqluser, String password, String database, String server, int port){

        source.setUser(sqluser);
        source.setPassword(password);

        source.setServerName(server);
        source.setPort(port);
        source.setDatabaseName(database);

    }

    public static boolean userExists(String userid){
        try (Connection c = source.getConnection();) {
            ResultSet set = c.createStatement().executeQuery("select userid from users where userid='"+ userid + "'");

            while (set.next()){
                if (set.getString("userid").equals(userid))
                    return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static UserSQL createUser(String userid){
        try (Connection c = source.getConnection();) {
            c.createStatement().executeUpdate("insert into users (userid, money, bio) values ( '" + userid + "', 0, '``Set a bio with -m bio <bio>``' )");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return new UserSQL();
    }

    public static String getBio(String userid){
        try (Connection c = source.getConnection();) {
            ResultSet set = c.createStatement().executeQuery("select bio from users where userid='" + userid + "'");
            set.first();
            return set.getString(1);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    public static void setBio(String userid, String bio){
        try (Connection c = source.getConnection();) {
            c.createStatement().executeUpdate("update users set bio='" + bio + "' where userid='" + userid + "'");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static int getMoney(String userid){
        try (Connection c = source.getConnection();) {
            ResultSet set = c.createStatement().executeQuery("select money from users where userid='" + userid + "'");
            set.first();
            return set.getInt(1);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void setMoney(String userid, int money){
        try (Connection c = source.getConnection();) {
            c.createStatement().executeUpdate("update users set money=" + money + " where userid='" + userid + "'");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
