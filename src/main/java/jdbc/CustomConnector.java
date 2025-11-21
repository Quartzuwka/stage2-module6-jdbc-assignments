package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class CustomConnector {
    public Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    private CustomConnector() {}

    public static Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }



}

