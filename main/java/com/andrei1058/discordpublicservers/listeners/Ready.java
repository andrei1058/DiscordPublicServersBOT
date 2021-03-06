/*
 * MIT License
 *
 * Copyright (c) 2018 Andrei Dascălu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.andrei1058.discordpublicservers.listeners;

import com.andrei1058.discordpublicservers.misc.Misc;
import net.dv8tion.jda.client.events.relationship.FriendRequestReceivedEvent;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


import static com.andrei1058.discordpublicservers.BOT.getBot;
import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;

public class Ready extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent e){
        switch (getConfig().getStatusType().toLowerCase()){
            case "playing":
                getBot().getPresence().setPresence(Game.playing(getConfig().getStatusMsg().replace("{servers}",String.valueOf(getBot().getGuilds().size()))), true);
                for (Guild g : getBot().getGuilds()){
                    System.out.println(g.getName());
                }
                break;
            case "listening":
                getBot().getPresence().setPresence(Game.listening(getConfig().getStatusMsg().replace("{servers}",String.valueOf(getBot().getGuilds().size()))), true);
                break;
            case "watching":
                getBot().getPresence().setPresence(Game.watching(getConfig().getStatusMsg().replace("{servers}",String.valueOf(getBot().getGuilds().size()))), true);
                break;
            case "streaming":
                getBot().getPresence().setPresence(Game.streaming(getConfig().getStatusMsg(), getConfig().getStreamLink().replace("{servers}",String.valueOf(getBot().getGuilds().size()))), true);
                break;
        }
        getBot().getPresence().setStatus(getConfig().getStatus());
        if (getDatabase() != null) {
            Misc.guildsRefresh();
            Misc.checkPremiumExpire();
        }
    }

    @Override
    public void onFriendRequestReceived(FriendRequestReceivedEvent e){
        if (e.getUser().getId().equalsIgnoreCase(getConfig().getOwnerID())){
            e.getFriendRequest().accept();
            e.getUser().openPrivateChannel().complete().sendMessage("Hello daddy :heart:").queue();
        }
    }
}
