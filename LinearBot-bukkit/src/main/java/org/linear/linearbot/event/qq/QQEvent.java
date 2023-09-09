package org.linear.linearbot.event.qq;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.bukkit.event.group.member.MiraiMemberLeaveEvent;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.linear.linearbot.LinearBot;
import org.linear.linearbot.bot.Bot;
import org.linear.linearbot.config.Config;
import org.linear.linearbot.event.server.ServerManager;
import org.linear.linearbot.event.server.ServerTps;
import org.linear.linearbot.tool.StringTool;
import org.linear.linearbot.config.Args;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QQEvent implements Listener {

    String Prefix = Args.Prefix();

    @EventHandler
    public void onFriendMessageReceive(MiraiFriendMessageEvent e){


        if(e.getMessage().equals(Prefix+"在线人数")) {
            if(!Config.Online()){
                return;
            }
            MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("当前在线：" + Bukkit.getServer().getOnlinePlayers()+"("+Bukkit.getServer().getOnlinePlayers().size()+"人)");
            return;
        }

        if(Config.getAdmins().contains(e.getSenderID())) {

            Pattern pattern;
            Matcher matcher;

            pattern = Pattern.compile(Prefix+"cmd .*");
            matcher = pattern.matcher(e.getMessage());
            if (matcher.find()) {
                if(!Config.CMD()){
                    return;
                }
                String cmd = Pattern.compile(Prefix+"cmd .*").matcher(e.getMessage()).group().replace(Prefix+"cmd ", "");
                ServerManager.sendCmd(cmd,e.getSenderID(),false);
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
        long senderID = e.getSenderID();
        String groupName = e.getGroup().getName();

        pattern = Pattern.compile("<?xm.*");
        matcher = pattern.matcher(msg);
        if (matcher.find()){
            Bukkit.broadcastMessage("§6" + "[" + groupName + "]" + "§f" + ":" + "不支持的消息类型，请在群聊中查看");
            return;
        }

        pattern = Pattern.compile("\"ap.*");
        matcher = pattern.matcher(msg);
        if (matcher.find()){
            Bukkit.broadcastMessage("§6" + "[" + groupName + "]" + "§f" + ":" + "不支持的消息类型，请在群聊中查看");
            return;
        }

        if(msg.equals(Prefix+"帮助")) {
            List<String> messages = new LinkedList<>();
            StringBuilder stringBuilder = new StringBuilder();
            messages.add("成员命令:");
            messages.add(Prefix+"在线人数 查看服务器当前在线人数");
            messages.add(Prefix+"tps 查看服务器当前tps");
            messages.add(Prefix+"申请白名单 为自己申请白名单");
            messages.add(Prefix+"删除白名单 删除自己的白名单");
            messages.add("管理命令:");
            messages.add(Prefix+"cmd 向服务器发送命令");
            messages.add(Prefix+"删除白名单 删除指定游戏id的白名单");
        for (String message : messages) {
                if (messages.get(messages.size() - 1).equalsIgnoreCase(message)) {
                    stringBuilder.append(message.replaceAll("§\\S", ""));
                } else {
                    stringBuilder.append(message.replaceAll("§\\S", "")).append("\n");
                }
            }
            Bot.sendMsg(stringBuilder.toString(),groupID);
        }

        if(msg.equals(Prefix+"在线人数")) {
            if(!Config.Online()){
                return;
            }
            Bot.sendMsg("当前在线：" + "("+Bukkit.getServer().getOnlinePlayers().size()+"人)"+ServerManager.listOnlinePlayer(),groupID);
            return;
        }

        if(msg.equals(Prefix+"tps")) {
            if(!Config.TPS()){
                return;
            }
            ServerTps st = new ServerTps();
            Bot.sendMsg("当前tps：" + st.getTps() + "\n" + "当前MSPT：" + st.getMSPT(),groupID);
            return;
        }

        pattern = Pattern.compile(Prefix + "申请白名单 .*");
        matcher = pattern.matcher(msg);
        if (matcher.find()) {
            if (!Config.WhiteList()) {
                return;
            }
            String PlayerName = matcher.group().replace(Prefix + "申请白名单 ", "");
            if (PlayerName.isEmpty()) {
                Bot.sendMsg("id不能为空", groupID);
                return;
            }
            new BukkitRunnable(){
                @Override
                public void run(){
                    if ((WhiteList.getBind(senderID) != null) || (WhiteList.getBind(PlayerName) != 0L)) {
                        Bot.sendMsg("玩家已存在", groupID);
                        return;
                    }
                    WhiteList.addBind(PlayerName, senderID);
                    Bot.sendMsg("成功申请白名单", groupID);
                }
            }.runTask(LinearBot.INSTANCE);
            return;
        }

        pattern = Pattern.compile(Prefix+".*");
        matcher = pattern.matcher(msg);
        if(matcher.find()){
            if (!Config.SDC()){
                return;
            }
            String scmd = matcher.group().replace(Prefix+"", "");
            String gcmd = Config.getCommandsYaml().getString("User."+scmd);
            if(gcmd!=null) {
                ServerManager.sendCmd(gcmd,groupID,false);
                return;
            }
        }

        if (Config.SDR()){
            String back = Config.getReturnsYaml().getString(msg);
            if(back!=null){
                Bot.sendMsg(back,groupID);
                return;
            }
        }

        if(Config.getAdmins().contains(senderID)) {

            pattern = Pattern.compile(Prefix+"cmd .*");
            matcher = pattern.matcher(msg);
            if (matcher.find()) {
                if(!Config.CMD()){
                    return;
                }
                String cmd = matcher.group().replace(Prefix+"cmd ", "");
                ServerManager.sendCmd(cmd,groupID,true);
                return;
            }

            pattern = Pattern.compile(Prefix+".*");
            matcher = pattern.matcher(msg);
            if(matcher.find()){
                if (!Config.SDC()){
                    return;
                }
                String scmd = matcher.group().replace(Prefix+"", "");
                String gcmd = Config.getCommandsYaml().getString("Admin."+scmd);
                if(gcmd!=null) {
                    ServerManager.sendCmd(gcmd,groupID,false);
                    return;
                }
            }

            pattern = Pattern.compile(Prefix + "删除白名单 .*");
            matcher = pattern.matcher(msg);
            if (matcher.find()) {
                if (!Config.WhiteList()) {
                    return;
                }
                String name = matcher.group().replace(Prefix + "删除白名单 ", "");
                if (name.isEmpty()) {
                    Bot.sendMsg("id不能为空", groupID);
                    return;
                }
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        long nameForId = WhiteList.getBind(name);
                        if (nameForId == 0L) {
                            Bot.sendMsg("尚未申请白名单", groupID);
                            return;
                        }
                        WhiteList.removeBind(name);
                        Bot.sendMsg("成功移出白名单", groupID);
                    }
                }.runTask(LinearBot.INSTANCE);
                return;
            }

        }

        pattern = Pattern.compile(Prefix + "删除白名单 .*");
        matcher = pattern.matcher(msg);
        if (matcher.find()) {
            if (!Config.WhiteList()) {
                return;
            }
            String name = matcher.group().replace(Prefix + "删除白名单 ", "");
            new BukkitRunnable(){
                @Override
                public void run(){
                    String idForName = WhiteList.getBind(senderID);
                    if (idForName == null || idForName.isEmpty()) {
                        Bot.sendMsg("您尚未申请白名单", groupID);
                        return;
                    }
                    if (name.isEmpty()) {
                        Bot.sendMsg("id不能为空", groupID);
                        return;
                    }
                    if (!idForName.equals(name)) {
                        Bot.sendMsg("你无权这样做", groupID);
                        return;
                    }
                    WhiteList.removeBind(name);
                    Bot.sendMsg("成功移出白名单", groupID);
                }
            }.runTask(LinearBot.INSTANCE);
            return;
        }

        if (!Config.Forwarding()){
            return;
        }

        if (Args.ForwardingMode()==1){
            pattern = Pattern.compile(Args.ForwardingPrefix()+".*");
            matcher = pattern.matcher(msg);
            if(!matcher.find()){
                return;
            }
            String name = StringTool.filterColor(e.getSenderName());
            String smsg = StringTool.filterColor(msg);
            Bukkit.broadcastMessage("§6" + "[" + groupName + "]" + "§a" + name + "§f" + ":" + smsg);
        }

        if(Config.getGroupQQs().contains(groupID)) {
            String name = StringTool.filterColor(e.getSenderName());
            String smsg = StringTool.filterColor(msg);
            Bukkit.broadcastMessage("§6" + "[" + groupName + "]" + "§a" + name + "§f" + ":" + smsg);
        }

    }

    @EventHandler
    public void MemberLeaveEvent(MiraiMemberLeaveEvent event){
        long targetID = event.getTargetID();
        String id = WhiteList.getBind(targetID);
        if(id == null){
            return;
        }
        WhiteList.removeBind(targetID);
    }


}
