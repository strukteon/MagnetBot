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
import java.util.*;

public class UserSQL {

    public List<Column> rowData;
    
    private UserSqlConnection connection;

    private String table;

    public UserSQL(UserSqlConnection connection){
        this.connection = connection;
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
        ResultSet set = connection.c.createStatement().executeQuery("select * from " + table);
        return true;
    }

    public boolean createTableIfNotExists() throws Exception {
        if (!tableExists(table)){
            String columnsSql = "";
            for (int i = 0; i < rowData.size(); i++) {
                if (i != 0)
                    columnsSql += ", ";
                columnsSql += rowData.get(i).variable + " " + rowData.get(i).datatype;
            }

            connection.c.createStatement().executeUpdate("create table " + table + " ( " + columnsSql + " )");
            return true;
        }
        return false;
    }


    public boolean updateColumns(boolean deleteMissing) throws Exception {

        boolean altered = false;

        ResultSet set = connection.c.createStatement().executeQuery("select column_name, data_type from information_schema.columns where table_name='" + table + "';");
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
                connection.c.createStatement().executeUpdate("alter table " + table + " drop column " + s);}

        //add remaining columns from rowdatatemp
        for (Column col : rowDataTemp)
            connection.c.createStatement().executeUpdate("alter table " + table + " add column " + col.variable + " " + col.datatype);

        return altered;

    }


    public boolean userExists(String key, String value) throws Exception {

        ResultSet set = connection.c.createStatement().executeQuery("select " + key + " from " + table + " where " + key + "='" + value + "'");

        while (set.next())
            if (set.getString(key).equals(value))
                return true;

        return false;
    }

    public boolean createUserIfNotExists(String key, String value) throws Exception {
        if (userExists(key, value))
            return false;
        else {
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

            connection.c.createStatement().executeUpdate("insert into " + table + " ( " + variables + " ) values ( " + values + " )");
            return true;
        }

    }

    public void updateUser(String key, String value, Column.Change... columns) throws Exception {
        if (userExists(key, value)) {
            String changes = "";

            for (Column.Change column : columns){
                if (!column.variable.equals(columns[0].variable))
                    changes += ", ";
                changes += column.variable + "='" + column.newValue + "'";
            }

            connection.c.createStatement().executeUpdate("update " + table + " set " + changes + " where " + key + "='" + value + "'");
        }
    }

    public HashMap<String, String> getUser(String key, String value) throws Exception {
        if (!userExists(key, value))
            return null;
        else {
            HashMap<String, String> map = new HashMap<>();
            ResultSet set = connection.c.createStatement().executeQuery("select * from " + table + " where " + key + "='" + value + "'");
            set.first();
            for (Column column : rowData) {
                String val = (set.getString(column.variable) != null ? set.getString(column.variable) : rowData.get(rowData.indexOf(column)).defaultValue);
                map.put(column.variable, val);
            }
            return map;
        }
    }

    public Thread disconnectThread(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection.c.close();
                    System.out.println("Disconnected.");
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
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
