package org.linear.linearbot.event.server;

import org.linear.linearbot.bot.Bot;
import org.linear.linearbot.config.Config;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class ServerEvent implements Listener{

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!Config.Forwarding()){
            return;
        }
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("[服务器]"+event.getPlayer().getDisplayName()+":"+event.getMessage(),groupID);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if (!Config.JoinAndLeave()){
            return;
        }
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("玩家"+event.getPlayer().getDisplayName()+"加入游戏",groupID);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if (!Config.JoinAndLeave()){
            return;
        }
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("玩家"+event.getPlayer().getDisplayName()+"退出游戏",groupID);
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
        ServerManager.sendCmd("msg "+name+" "+msg);
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("玩家"+name+msg,groupID);
        }
    }
}
