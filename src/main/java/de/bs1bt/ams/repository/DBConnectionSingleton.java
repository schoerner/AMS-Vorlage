package de.bs1bt.ams.repository;

import de.bs1bt.ams.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionSingleton {
    private static Connection connection = null;
    private static final String url = Config.get("db.url");
    private static final String user = Config.get("db.user");
    private static final String password = Config.get("db.password");

    private DBConnectionSingleton() {}
    public static Connection getConnection() throws SQLException {
        if(null == connection || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}
