package configurationmanagement;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.Properties;

interface Configuration {
    Configuration clone();
}

class AppConfig implements Configuration {

    private Properties properties;

    public AppConfig(final Properties properties){
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public AppConfig clone() {
        final AppConfig appConfig = new AppConfig(new Properties());
        appConfig.properties.putAll(this.properties);
        return appConfig;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "properties=" + properties +
                '}';
    }
}

class ConfigurationManager {
    private AppConfig baseAppConfig;

    public ConfigurationManager(final AppConfig baseAppConfig) {
        this.baseAppConfig = baseAppConfig;
    }

    public AppConfig getAppConfig() {
        final AppConfig appConfig = this.baseAppConfig.clone();
        appConfig.getProperties().put("added", "added");
        return appConfig;
    }
}


public class ConfigurationManagementSystem {
    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.put("base", "base");
        final AppConfig baseAppConfig = new AppConfig(properties);

        final ConfigurationManager configurationManager = new ConfigurationManager(baseAppConfig);

        System.out.println(configurationManager.getAppConfig());
    }
}
