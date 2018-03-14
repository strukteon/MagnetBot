package utils;
/*
    Created by nils on 04.03.2018 at 15:55.
    
    (c) nils 2018
*/

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class UserSqlConnection {
    public MysqlDataSource source;
    public Connection c;
    public UserSqlConnection(String sqluser, String password, String database, String server, int port) throws Exception {
        MysqlDataSource source = new MysqlDataSource();

        source.setUser(sqluser);
        source.setPassword(password);

        source.setServerName(server);
        source.setPort(port);
        source.setDatabaseName(database);

        c = source.getConnection();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Connection ctemp = source.getConnection();
                    c.close();
                    c = ctemp;
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }, 0, 500 * 1000);
    }
}
