package org.linear.linearbot.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.linear.linearbot.LinearBot;

public class QuickShopHook {

    public static Boolean hasQs;

    public static void hookQuickShop() {

        Plugin quickShop = Bukkit.getPluginManager().getPlugin("QuickShop");
        try {
            if (quickShop != null) {
                hasQs = true;
                LinearBot.INSTANCE.getLogger().info("QuickShop 关联成功");
            }else{
                hasQs = false;
                LinearBot.INSTANCE.getLogger().info("QuickShop 关联失败");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }}
}
