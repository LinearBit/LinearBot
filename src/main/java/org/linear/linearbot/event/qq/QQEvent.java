package org.linear.linearbot.event.qq;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.linear.linearbot.bot.Bot;
import org.linear.linearbot.config.Config;
import org.linear.linearbot.event.server.ServerManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.reloadWhitelist;

public class QQEvent implements Listener {
    @EventHandler
    public void onFriendMessageReceive(MiraiFriendMessageEvent e){

        if(e.getMessage().equals("/在线人数")) {
            if(!Config.Online()){
                return;
            }
            MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("当前在线：" + Bukkit.getServer().getOnlinePlayers()+"("+Bukkit.getServer().getOnlinePlayers().size()+"人)");
            return;
        }

        if(Config.getAdmins().contains(e.getSenderID())) {

            Pattern pattern;
            Matcher matcher;

            pattern = Pattern.compile("/cmd .*");
            matcher = pattern.matcher(e.getMessage());
            if (matcher.find()) {
                if(!Config.CMD()){
                    return;
                }
                String cmd = Pattern.compile("/cmd .*").matcher(e.getMessage()).group().replace("/cmd ", "");
                ServerManager.sendCmd(cmd);
                Bot.sendMsg("已发送指令至服务器",e.getSenderID());
            }

            return;
        }

        Bukkit.broadcastMessage("§6"+"[私聊消息]"+"§a"+e.getSenderName()+"§f"+":"+e.getMessage());
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){

        Pattern pattern;
        Matcher matcher;

        String msg = e.getMessage();
        long groupID= e.getGroupID();

        if(msg.equals("/在线人数")) {
            if(!Config.Online()){
                return;
            }
            Bot.sendMsg("当前在线：" + "("+Bukkit.getServer().getOnlinePlayers().size()+"人)"+ServerManager.listOnlinePlayer(),groupID);
            return;
        }

        pattern = Pattern.compile("/申请白名单 .*");
        matcher = pattern.matcher(msg);
        if (matcher.find()) {
            if(!Config.WhiteList()){
                return;
            }
            String name = matcher.group().replace("/申请白名单 ", "");
            ServerManager.sendCmd("whitelist add "+name);
            reloadWhitelist();
            Bot.sendMsg("成功申请白名单",groupID);
            return;
        }

        if(Config.getAdmins().contains(e.getSenderID())) {

            pattern = Pattern.compile("/cmd .*");
            matcher = pattern.matcher(msg);
            if (matcher.find()) {
                if(!Config.CMD()){
                    return;
                }
                String cmd = matcher.group().replace("/cmd ", "");
                ServerManager.sendCmd(cmd);
                Bot.sendMsg("已发送指令至服务器",groupID);
                return;
            }

            pattern = Pattern.compile("/删除白名单 .*");
            matcher = pattern.matcher(msg);
            if (matcher.find()) {
                if(!Config.WhiteList()){
                    return;
                }
                String name = matcher.group().replace("/删除白名单 ", "");
                ServerManager.sendCmd("whitelist remove "+name);
                Bot.sendMsg("成功移出白名单",groupID);
                return;
            }
        }

        if(Config.getGroupQQs().contains(groupID)) {
            if (!Config.Forwarding()){
                return;
            }
            Bukkit.broadcastMessage("§6" + "[" + e.getGroupName() + "]" + "§a" + e.getSenderName() + "§f" + ":" + msg);
        }

    }

}
