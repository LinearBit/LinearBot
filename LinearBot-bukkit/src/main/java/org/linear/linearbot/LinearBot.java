package org.linear.linearbot;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.linear.linearbot.bot.Bot;
import org.linear.linearbot.command.Commands;
import org.linear.linearbot.config.Config;
import org.linear.linearbot.event.qq.QQEvent;
import org.linear.linearbot.event.server.QsChatEvent;
import org.linear.linearbot.event.server.QsHikariChatEvent;
import org.linear.linearbot.event.server.ServerEvent;
import org.linear.linearbot.hook.AuthMeHook;
import org.linear.linearbot.hook.GriefDefenderHook;
import org.linear.linearbot.hook.QuickShopHook;
import org.linear.linearbot.hook.ResidenceHook;
import org.linear.linearbot.metrics.Metrics;

import java.util.List;

public final class LinearBot extends JavaPlugin implements Listener{

    public static LinearBot INSTANCE;

    @Override
    public void onEnable() {

        INSTANCE = this;


        Config.createConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        AuthMeHook.hookAuthme();
        ResidenceHook.hookRes();
        QuickShopHook.hookQuickShop();
        GriefDefenderHook.hookGriefDefender();
        getLogger().info("关联插件连接完毕");
        Bukkit.getPluginManager().registerEvents(new ServerEvent(), this);
        if (QuickShopHook.hasQs) Bukkit.getPluginManager().registerEvents(new QsChatEvent(),this);
        if (QuickShopHook.hasQsHikari) Bukkit.getPluginManager().registerEvents(new QsHikariChatEvent(),this);
        getLogger().info("服务器事件监听器注册完毕");
        Bukkit.getPluginManager().registerEvents(new QQEvent(), this);
        getLogger().info("QQ事件监听器注册完毕");
        Bukkit.getServer().getPluginCommand("linearbot").setExecutor(new Commands());
        getLogger().info("命令注册完毕");

        // All you have to do is adding the following two lines in your onEnable method.
        // You can find the plugin ids of your plugins on the page https://bstats.org/what-is-my-plugin-id
        int pluginId = 17137; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        getLogger().info( "LinearBot已启动");
        /*List<Long> groups = Config.getGroupQQ();
        for (long groupID : groups) {
            Bot.sendMsg("插件已启动", groupID);
        }*/
        runAfterDone();
    }

    @Override
    public void onDisable() {
        getLogger().info("LinearBot已关闭");
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups) {
            Bot.sendMsg("LinearBot已关闭", groupID);
        }
    }

    public static void say(String s) {
        CommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(s);
    }

    public void runAfterDone() {
        Runnable thread = () -> {
            List<Long> groups = Config.getGroupQQs();
            for (long groupID : groups) {
                Bot.sendMsg("LinearBot已启动", groupID);
            }
        };
        INSTANCE.getServer().getScheduler().runTaskLaterAsynchronously(INSTANCE, thread, 200);
    }

}


