package org.linear.linearbot.event.server;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.linear.linearbot.bot.Bot;
import org.linear.linearbot.config.Args;
import org.linear.linearbot.config.Config;
import org.linear.linearbot.event.qq.WhiteList;
import org.linear.linearbot.hook.AuthMeHook;
import org.linear.linearbot.hook.GriefDefenderHook;
import org.linear.linearbot.hook.QuickShopHook;
import org.linear.linearbot.hook.ResidenceHook;
import org.linear.linearbot.tool.StringTool;

import java.util.List;

import static org.linear.linearbot.hook.ResidenceHook.resChatApi;

public class ServerEvent implements Listener{

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!Config.Forwarding()){
            return;
        }
        String text = Config.getFormat("QQ");
        String name = StringTool.filterColor(event.getPlayer().getDisplayName());
        String message = StringTool.filterColor(event.getMessage());
        if (AuthMeHook.hasAuthMe) {if (!AuthMeHook.authMeApi.isAuthenticated(event.getPlayer())) {return;} }
        if (ResidenceHook.hasRes) {if (resChatApi.getPlayerChannel(event.getPlayer().getName()) != null) {return;}}
        if (QuickShopHook.hasQs) {if (event.getPlayer() == QsChatEvent.getQsSender() && event.getMessage() == QsChatEvent.getQsMessage()) {return;}}
        if (QuickShopHook.hasQsHikari) {if (event.getPlayer() == QsHikariChatEvent.getQsSender() && event.getMessage() == QsHikariChatEvent.getQsMessage()) {return;}}
        if (GriefDefenderHook.hasGriefDefender) {if (GDClaimEvent.getGDMessage() == event.getMessage()){return;}}
        text = text.replace("%player_name%",name).replace("%msg%",message);
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg(text,groupID);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        String name = StringTool.filterColor(player.getDisplayName());

        String realName = StringTool.filterColor(player.getName());


        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("玩家"+name+"加入游戏",groupID);
        }

        if(Config.WhiteList()){
            if (WhiteList.getBind(realName)==0){
                player.kickPlayer(Config.getConfigYaml().getString("Whitelist.kickMsg"));
                for (long groupID : groups){
                    Bot.sendMsg("玩家"+name+"因为未在白名单中被踢出",groupID);
                }
                return;
            }
        }

        if (!Config.JoinAndLeave()){
            return;
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        String name = StringTool.filterColor(event.getPlayer().getDisplayName());

        if (!Config.JoinAndLeave()){
            return;
        }
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("玩家"+name+"退出游戏",groupID);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if(!Config.DieReport()){
            return;
        }
        Player player=event.getEntity();
        String name= player.getName();
        Location location=player.getLocation();
        int x= (int) location.getX();
        int y= (int) location.getY();
        int z= (int) location.getZ();
        String msg = "死在了"+location.getWorld().getName()+"世界"+"("+x+","+y+","+z+")";
        ServerManager.sendCmd("msg "+name+" "+msg,0,false);
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("玩家"+name+msg,groupID);
        }
    }
}
