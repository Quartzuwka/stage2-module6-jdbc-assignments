package jdbc;

import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    // Приватный конструктор
    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.name = name;

        try {
            // Загружаем драйвер (важно для старых версий JDBC)
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Реализация Singleton с Double-Checked Locking и чтением пропертей
    public static CustomDataSource getInstance() {
        if (instance == null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    instance = new CustomDataSource(
                            PropertiesUtil.getByKey("h2.driver"),
                            PropertiesUtil.getByKey("h2.url"),
                            PropertiesUtil.getByKey("h2.password"),
                            PropertiesUtil.getByKey("h2.name")
                    );
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, name, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // --- Обязательные методы-заглушки интерфейса DataSource ---

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("Not a wrapper");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
    public class PropertiesUtil {

        private static final Properties PROPERTIES = new Properties();

        static {
            loadProperties();
        }

        public static String getByKey(String key) {
            return PROPERTIES.getProperty(key);
        }

        // In CustomDataSource.java inside PropertiesUtil class

        private static void loadProperties() {
            // Use explicit checks
            try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("app.properties")) {
                if (inputStream == null) {
                    // This will clearly tell you if the file is not found in the classpath
                    throw new RuntimeException("CRITICAL ERROR: 'app.properties' not found in classpath. Try Rebuilding the Project.");
                }
                PROPERTIES.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load app.properties", e);
            }
        }


    }
}
