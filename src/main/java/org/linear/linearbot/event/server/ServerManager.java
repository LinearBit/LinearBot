package org.linear.linearbot.event.server;

import org.linear.linearbot.LinearBot;
import org.linear.linearbot.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ServerManager {

    public static String listOnlinePlayer() {
        List<String> onlinePlayer = new LinkedList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            onlinePlayer.add(p.getName());
        }
        return Arrays.toString(onlinePlayer.toArray()).replace("\\[|\\]", "");
    }

    public static List<String> msgList = new LinkedList<>();

    public static void sendCmd(String cmd) {
        if(!Config.CMD()){
            return;
        }
        new BukkitRunnable(){
            @Override
            public void run(){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
                //Bukkit.getScheduler().runTaskTimer(LinearBot.INSTANCE,0L,);

                /*Bukkit.getScheduler().runTaskTimer(LinearBot.INSTANCE, () -> {
                    // 读取消息
                    StringBuilder stringBuilder = new StringBuilder();
                    if (msgList.size() == 0) {
                        msgList.add("该命令为核心原版自带实现指令，无法获取对应返回值，请在后台插件是否执行成功。");
                    }
                    for (String msg : msgList) {
                        if (msgList.get(msgList.size() - 1).equalsIgnoreCase(msg)) {
                            stringBuilder.append(msg.replaceAll("§\\S", ""));
                        } else {
                            stringBuilder.append(msg.replaceAll("§\\S", "")).append("\n");
                        }
                    }
                    // 发送消息
                    Bot.sendMsg(stringBuilder.toString(),id);
                    msgList.clear();
                },0L,0);*/
            }
        }.runTask(LinearBot.INSTANCE);
    }
}
