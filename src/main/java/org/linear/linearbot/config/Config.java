package org.linear.linearbot.config;

import org.linear.linearbot.LinearBot;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config {
    private static final LinearBot INSTANCE = LinearBot.INSTANCE;
    private static final File botFile = new File(INSTANCE.getDataFolder(), "bot.yml");
    private static final File configFile = new File(INSTANCE.getDataFolder(), "config.yml");
    private static YamlConfiguration bot;
    private static YamlConfiguration config;

    public static void createConfig(){
        File[] allFile = {botFile,configFile};
        for (File file : allFile) {
            if (!file.exists()) {
                INSTANCE.saveResource(file.getName(), true);
            }
        }
        loadConfig();
        if (!Config.getBotYamlVersion().equals("1.1")){
            INSTANCE.saveResource(botFile.getName(), true);
        }
        if (!Config.getConfigYamlVersion().equals("1.1")){
            INSTANCE.saveResource(configFile.getName(), true);
        }
    }

    public static String getBotYamlVersion(){
        return getBotYaml().getString("Ver");
    }

    public static String getConfigYamlVersion(){
        return getConfigYaml().getString("Ver");
    }

    public static void loadConfig(){
        bot = YamlConfiguration.loadConfiguration(botFile);
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static YamlConfiguration getBotYaml(){
        return bot;
    }

    public static YamlConfiguration getConfigYaml() {
        return config;
    }
    public static long getBotQQ() {
        return getBotYaml().getLong("Bot.QQ");
    }

    public static List<Long> getGroupQQs(){
        return getBotYaml().getLongList("Groups");
    }

    public static List<Long> getAdmins() {
        return Config.getBotYaml().getLongList("Admins");
    }

    public static boolean DieReport(){
        return  getConfigYaml().getBoolean("DieReport");
    }

    public static boolean Forwarding(){
        return  getConfigYaml().getBoolean("Forwarding");
    }

    public static boolean WhiteList(){
        return  getConfigYaml().getBoolean("WhiteList");
    }

    public static boolean JoinAndLeave(){
        return  getConfigYaml().getBoolean("JoinAndLeave");
    }

    public static boolean CMD(){
        return  getConfigYaml().getBoolean("CMD");
    }

    public static boolean Online(){
        return  getConfigYaml().getBoolean("Online");
    }

    public static boolean Stats(){
        return  getConfigYaml().getBoolean("Stats");
    }
}
