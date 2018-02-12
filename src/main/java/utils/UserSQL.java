package utils;
/*
    Created by nils on 01.02.2018 at 19:31.
    
    (c) nils 2018
*/

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import commands.chat.commands.music.Connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserSQL {

    public List<Column> rowData;

    private MysqlDataSource source;
    private static MysqlDataSource srource;

    private String table;

    public UserSQL(MysqlDataSource source){
        this.source = source;
    }

    public static MysqlDataSource login(String sqluser, String password, String database, String server){
        return login(sqluser, password, database, server, 3306);
    }

    public static MysqlDataSource login(String sqluser, String password, String database, String server, int port){
        MysqlDataSource source = new MysqlDataSource();

        source.setUser(sqluser);
        source.setPassword(password);

        source.setServerName(server);
        source.setPort(port);
        source.setDatabaseName(database);

        return source;
    }



    public void setData(Column... columns){
        this.rowData = new ArrayList<>(Arrays.asList(columns));
    }


    public UserSQL setTable(String table) throws Exception {
        this.table = table;
        Column[] columns = new Column[rowData.size()];
        columns = rowData.toArray(columns);
        createTableIfNotExists();
        return this;
    }


    public boolean tableExists(String table) throws Exception {
        try (Connection c = source.getConnection()) {
            ResultSet set = c.createStatement().executeQuery("select * from " + table);
            return true;
        } catch (MySQLSyntaxErrorException e) {
            return false;
        }
    }

    public boolean createTableIfNotExists() throws Exception {
        if (!tableExists(table)){
            try (Connection c = source.getConnection()) {
                String columnsSql = "";
                for (int i = 0; i < rowData.size(); i++) {
                    if (i != 0)
                        columnsSql += ", ";
                    columnsSql += rowData.get(i).variable + " " + rowData.get(i).datatype;
                }

                c.createStatement().executeUpdate("create table " + table + " ( " + columnsSql + " )");
                return true;
            }
        }
        return false;
    }


    public boolean updateColumns(boolean deleteMissing) throws Exception {

        boolean altered = false;

        try (Connection c = source.getConnection()) {
            ResultSet set = c.createStatement().executeQuery("select column_name, data_type from information_schema.columns where table_name='" + table + "';");
            HashMap<String, String> columnsOnline = new HashMap<>();
            while (set.next()){
                columnsOnline.put(set.getString("column_name"), set.getString("data_type"));
            }

            // remove column from both list if they overlap

            List<Column> rowDataTemp = new ArrayList<>(rowData);

            for (int i = 0; i < rowData.size(); i++){

                if ((columnsOnline.get(rowData.get(i).variable) == null ? "" : columnsOnline.get(rowData.get(i).variable)).equals(rowData.get(i).datatype)){
                    columnsOnline.remove(rowData.get(i).variable);
                    rowDataTemp.remove(rowData.get(i));
                }

            }

            altered = rowDataTemp.size() != 0 || ( columnsOnline.size() != 0 && deleteMissing );

            // remove remaining columns from columnsonline
            if (deleteMissing)
                for (String s : columnsOnline.keySet()){
                    c.createStatement().executeUpdate("alter table " + table + " drop column " + s);}

            //add remaining columns from rowdatatemp
            for (Column col : rowDataTemp)
                c.createStatement().executeUpdate("alter table " + table + " add column " + col.variable + " " + col.datatype);

            return altered;
        }
    }


    public boolean userExists(String key, String value) throws Exception {

        try (Connection c = source.getConnection()) {

            ResultSet set = c.createStatement().executeQuery("select " + key + " from " + table + " where " + key + "='" + value + "'");

            while (set.next()){
                if (set.getString(key).equals(value))
                    return true;
            }
        }

        return false;
    }

    public boolean createUserIfNotExists(String key, String value) throws Exception {
        if (userExists(key, value))
            return false;
        else
            try (Connection c = source.getConnection()) {
                String variables = "";
                for (Column column : rowData){
                    if (!column.equals(rowData.get(0)))
                        variables += ", ";
                    variables += column.variable;
                }

                String values = "";
                for (Column column : rowData){
                    if (!column.equals(rowData.get(0)))
                        values += ", ";
                    if (column.variable.equals(key))
                        values += value;
                    else
                        values += "'" + column.defaultValue + "'";
                }

                c.createStatement().executeUpdate("insert into " + table + " ( " + variables + " ) values ( " + values + " )");
                return true;
            }

    }

    public void updateUser(String key, String value, Column.Change... columns) throws Exception {
        if (!userExists(key, value))
            return;
        else
            try (Connection c = source.getConnection()) {
                String changes = "";

                for (Column.Change column : columns){
                    if (!column.variable.equals(columns[0].variable))
                        changes += ", ";
                    changes += column.variable + "='" + column.newValue + "'";
                }

                c.createStatement().executeUpdate("update " + table + " set " + changes + " where " + key + "='" + value + "'");
            }
    }

    public HashMap<String, String> getUser(String key, String value) throws Exception {
        if (!userExists(key, value))
            return null;
        else
            try (Connection c = source.getConnection()) {
                HashMap<String, String> map = new HashMap<>();
                ResultSet set = c.createStatement().executeQuery("select * from " + table + " where " + key + "='" + value + "'");
                set.first();
                for (Column column : rowData){
                    map.put(column.variable, set.getString(column.variable));
                }
                return map;
            }
    }



    public static class Column {
        String variable;
        String datatype = "text";
        String defaultValue;

        public Column(String variable){
            this(variable, "");
        }

        public Column(String variable,String defaultValue){
            this.variable = variable;
            this.datatype = datatype;
            this.defaultValue = defaultValue;
        }

        public static class Change {
            String variable;
            String newValue;
            public Change(String variable, String newValue){
                this.variable = variable;
                this.newValue = newValue;
            }
        }

    }

}
