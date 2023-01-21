package org.linear.linearbot.event.qq;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.bukkit.event.group.member.MiraiMemberLeaveEvent;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.linear.linearbot.bot.Bot;
import org.linear.linearbot.config.Config;
import org.linear.linearbot.event.server.ServerManager;
import org.linear.linearbot.event.server.ServerTps;
import org.linear.linearbot.tool.StringTool;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getPlayer;
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
        long senderID = e.getSenderID();

        pattern = Pattern.compile("<?xm.*");
        matcher = pattern.matcher(msg);
        if (matcher.find()){
            return;
        }

        pattern = Pattern.compile("\"ap.*");
        matcher = pattern.matcher(msg);
        if (matcher.find()){
            return;
        }

        if(msg.equals("/在线人数")) {
            if(!Config.Online()){
                return;
            }
            Bot.sendMsg("当前在线：" + "("+Bukkit.getServer().getOnlinePlayers().size()+"人)"+ServerManager.listOnlinePlayer(),groupID);
            return;
        }

        if(msg.equals("/tps")) {
            if(!Config.Online()){
                return;
            }
            ServerTps st = new ServerTps();
            Bot.sendMsg("当前tps：" + st.getTps() + "\n" + "当前MSPT：" + st.getMSPT(),groupID);
            return;
        }

        pattern = Pattern.compile("/申请白名单 .*");
        matcher = pattern.matcher(msg);
        if (matcher.find()) {
            if(!Config.WhiteList()){
                return;
            }
            String name = matcher.group().replace("/申请白名单 ", "");
            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            if (MiraiMC.getBind(uuid)!=0){
                Bot.sendMsg("此id已存在于白名单",groupID);
                return;
            }
            if (MiraiMC.getBind(senderID) != null){
                Bot.sendMsg("不可重复申请白名单",groupID);
                return;
            }
            ServerManager.sendCmd("whitelist add "+name);
            MiraiMC.addBind(uuid,senderID);
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
                UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
                MiraiMC.removeBind(uuid);
                Bot.sendMsg("成功移出白名单",groupID);
                return;
            }
        }

        pattern = Pattern.compile("/.*");
        matcher = pattern.matcher(msg);
        if(matcher.find()){
            if (!Config.SDC()){
                return;
            }
            String scmd = matcher.group().replace("/", "");
            String gcmd = Config.getCommandsYaml().getString(scmd);
            if(gcmd!=null) {
                ServerManager.sendCmd(gcmd);
                return;
            }
            return;
        }

        if (Config.SDR()){
            String back = Config.getReturnsYaml().getString(msg);
            if(back!=null){
                Bot.sendMsg(back,groupID);
                return;
            }
        }

        if(Config.getGroupQQs().contains(groupID)) {
            if (!Config.Forwarding()){
                return;
            }
            String name = StringTool.filterColor(e.getSenderName());
            String smsg = StringTool.filterColor(msg);
            Bukkit.broadcastMessage("§6" + "[" + e.getGroupName() + "]" + "§a" + name + "§f" + ":" + smsg);
        }

    }

    @EventHandler
    public void MemberLeaveEvent(MiraiMemberLeaveEvent event){
        long targetID = event.getTargetID();
        UUID uuid = MiraiMC.getBind(targetID);
        if(uuid.equals("")){
            return;
        }
        MiraiMC.removeBind(targetID);
        String name = getPlayer(uuid).getName();
        ServerManager.sendCmd("whitelist remove "+name);
    }


}
