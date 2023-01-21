package org.linear.linearbot.bot;

import org.linear.linearbot.config.Config;
import me.dreamvoid.miraimc.api.MiraiBot;

public class Bot {
    public static void sendMsg(String msg,long groupID){
        MiraiBot.getBot(Config.getBotQQ()).getGroup(groupID).sendMessageMirai(msg);
    }
}
