package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String getByKey(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try(InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("app.properties")) {
            PROPERTIES.load(inputStream);
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

}
