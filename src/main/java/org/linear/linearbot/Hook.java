package org.linear.linearbot;

import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Hook {

    public static Boolean hasAuthMe;
    public static AuthMeApi authMeApi;
    public static void hookPlugins() {

        Plugin authMe = Bukkit.getPluginManager().getPlugin("AuthMe");
        try {
            if (authMe != null) {
                hasAuthMe = true;
                authMeApi = AuthMeApi.getInstance();
                LinearBot.INSTANCE.getLogger().info("AuthMe 关联成功");
                try {
                } catch (Throwable e) {
                    hasAuthMe = false;
                    LinearBot.INSTANCE.getLogger().info("AuthMe 关联失败");
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

/*
        Plugin res = getServer().getPluginManager().getPlugin("Residence");
        if (res != null) {
            ResidenceApi resAPI = Residence.getAPI();
        }
*/
    }

}
