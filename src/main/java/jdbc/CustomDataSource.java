package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    private CustomDataSource(String driver, String password, String name, String url) {
        this.driver = driver;
        this.password = password;
        this.name = name;
        this.url = url;
    }

    public static CustomDataSource getInstance() {
        if(instance == null) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("app");
            instance = new CustomDataSource(
                    resourceBundle.getString("postgres.driver"),
                    resourceBundle.getString("postgres.password"),
                    resourceBundle.getString("postgres.name"),
                    resourceBundle.getString("postgres.url")
            );
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(name, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}