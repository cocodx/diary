package io.github.cocodx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author amazfit
 * @date 2022-08-01 下午8:49
 **/
public class PropertiesUtils {

    private final static Properties properties = new Properties();
    static {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getValue(String name){
        return String.valueOf(properties.get(name));
    }
}
