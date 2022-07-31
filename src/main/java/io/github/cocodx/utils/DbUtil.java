package io.github.cocodx.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author amazfit
 * @date 2022-08-01 上午6:05
 **/
public class DbUtil {

    private static String dbUrl = "jdbc:mysql://localhost:3306/db_diary?characterEncoding=utf-8&useSSL=false&serverTimeZone=GMT%2B8";
    private static String dbUserName="root";
    private static String dbPassword="password";
    private static String dbDriverName="com.mysql.cj.jdbc.Driver";

    public static Connection connection() throws ClassNotFoundException, SQLException {
        Class.forName(dbDriverName);
        Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        return connection;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection!=null){
            connection.close();
        }
    }

    public static void main(String[] args) {
        try{
            connection();
            System.out.println("connection success");
        }catch (Exception e){
            System.out.println("connection failed");
        }
    }

}
