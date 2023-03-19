package org.linear.linearbot.config;

public class Args {

    public static int ForwardingMode(){
        return Config.getConfigYaml().getInt("Forwarding.mode");
    }

    public static int WhitelistMode(){
        return Config.getConfigYaml().getInt("Whitelist.mode");
    }

    public static String ForwardingPrefix(){
        return Config.getConfigYaml().getString("Forwarding.prefix");
    }

    public static String Prefix(){
        return Config.getConfigYaml().getString("Prefix");
    }

}
