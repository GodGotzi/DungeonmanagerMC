package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public abstract class FileManager {

    private final YamlConfiguration yamlConfiguration;
    private String forPath;

    public FileManager(String forPath, YamlConfiguration yamlConfiguration) {
        this.forPath = forPath;
        this.yamlConfiguration = yamlConfiguration;
    }

    public FileManager(YamlConfiguration yamlConfiguration) {
        this.yamlConfiguration = yamlConfiguration;
    }

    public abstract void initialize();

    //String List
    public List<String> getStringList(Object obj, String path) {
        if(forPath == null) {
            return yamlConfiguration.getStringList(obj.toString() + "." + path);
        }
        return yamlConfiguration.getStringList(forPath + "." + obj.toString() + "." + path);
    }

    public List<String> getStringList(String path) {
        return yamlConfiguration.getStringList(path);
    }
    //String List End

    //int
    public int getInt(String path) {
        return yamlConfiguration.getInt(path);
    }

    public int getInt(Object obj, String path) {
        if(forPath == null) {
            return yamlConfiguration.getInt(obj.toString() + "." + path);
        }
        return yamlConfiguration.getInt(forPath + "." + obj.toString() + "." + path);
    }
    //int End

    public double getDouble(String path) {
        return yamlConfiguration.getDouble(path);
    }

    public double getDouble(Object obj, String path) {
        if(forPath == null) {
            return yamlConfiguration.getDouble(obj.toString() + "." + path);
        }
        return yamlConfiguration.getDouble(forPath + "." + obj.toString() + "." + path);
    }

    //String with Color
    public String getStringNoColor(Object obj, String path) {
        if(forPath == null) {
            return  yamlConfiguration.getString(obj.toString() + "." + path);
        }
        return yamlConfiguration.getString(forPath + "." + obj.toString() + "." + path);
    }

    public String getStringNoColor(String path) {
        return yamlConfiguration.getString(path);
    }
    //String with Color End


    //String
    public String getString(Object obj, String path) {
        if(forPath == null) {
            return  Utils.color(yamlConfiguration.getString(obj.toString() + "." + path));
        }
        return Utils.color(yamlConfiguration.getString(forPath + "." + obj.toString() + "." + path));
    }

    public String getString(String path) {
        return Utils.color(yamlConfiguration.getString(path));
    }

    //boolean
    public boolean getBoolean(Object obj, String path) {
        if(forPath == null) {
            return yamlConfiguration.getBoolean(obj.toString() + "." + path);
        }
        return yamlConfiguration.getBoolean(forPath + "." + obj.toString() + "." + path);
    }

    public boolean getBoolean(String path) {
        return yamlConfiguration.getBoolean(path);
    }
    //boolean End



    public ConfigurationSection getConfigSection(String path) {
        return yamlConfiguration.getConfigurationSection(path);
    }

    public YamlConfiguration getYamlConfiguration() {
        return yamlConfiguration;
    }
}
