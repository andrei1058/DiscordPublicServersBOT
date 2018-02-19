/*
 * MIT License
 *
 * Copyright (c) 2018 Andrei Dascalu
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

package com.andrei1058.discordpublicservers;

import com.andrei1058.discordpublicservers.customisation.Langs;
import com.andrei1058.discordpublicservers.customisation.Tags;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import static com.andrei1058.discordpublicservers.BOT.getBot;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;

public class Misc {

    public static String createInviteLink(Guild g) {
        String i = "";
        if (g.getDefaultChannel() != null) {
            if (PermissionUtil.checkPermission(g.getDefaultChannel(), g.getSelfMember(), Permission.MANAGE_CHANNEL)) {
                for (Invite i2 : g.getInvites().complete()) {
                    if (i2.getInviter() == getBot().getSelfUser()) {
                        if (i2.isTemporary()) {
                            if (PermissionUtil.checkPermission(g.getDefaultChannel(), g.getSelfMember(), Permission.CREATE_INSTANT_INVITE)) {
                                return g.getDefaultChannel().createInvite().setTemporary(false).complete().getURL();
                            }
                        } else {
                            return i2.getURL();
                        }
                    }
                }
            }
        }
        for (TextChannel tc : g.getTextChannels()) {
            if (PermissionUtil.checkPermission(tc, g.getSelfMember(), Permission.MANAGE_CHANNEL)) {
                for (Invite i2 : g.getInvites().complete()) {
                    if (i2.getInviter() == getBot().getSelfUser()) {
                        if (i2.isTemporary()) {
                            if (PermissionUtil.checkPermission(tc, g.getSelfMember(), Permission.CREATE_INSTANT_INVITE)) {
                                return g.getDefaultChannel().createInvite().setTemporary(false).complete().getURL();
                            }
                        } else {
                            return i2.getURL();
                        }
                    }
                }
            }
        }
        if (g.getDefaultChannel() != null) {
            if (PermissionUtil.checkPermission(g.getDefaultChannel(), g.getSelfMember(), Permission.CREATE_INSTANT_INVITE)) {
                return g.getDefaultChannel().createInvite().setTemporary(false).complete().getURL();
            }
        }
        for (TextChannel tc : g.getTextChannels()) {
            for (Invite i2 : g.getInvites().complete()) {
                if (i2.getInviter() == getBot().getSelfUser()) {
                    if (i2.isTemporary()) {
                        if (PermissionUtil.checkPermission(tc, g.getSelfMember(), Permission.CREATE_INSTANT_INVITE)) {
                            return g.getDefaultChannel().createInvite().setTemporary(false).complete().getURL();
                        }
                    } else {
                        return i2.getURL();
                    }
                }
            }
        }
        //todo nu pot genera invite link! serverul tau nu va fi vizibil pe site. va recomandam sa ne dati acces la manage channel ca sa nu spamam in audit log
        return i;
    }

    public static void startUpRefresh() {
        for (Guild g : getBot().getGuilds()) {
            if (getDatabase().isGuildExists(g.getId())) {
                if (!getDatabase().isShown(g.getId())) {
                    getDatabase().showGuild(g.getId());
                }
                getDatabase().updateGuildData(g.getIdLong(), g.getName(), g.getMembers().stream()
                                .filter(m -> !(m.getOnlineStatus() == OnlineStatus.OFFLINE || m.getOnlineStatus() == OnlineStatus.INVISIBLE)).toArray().length, g.getMembers().size(),
                        g.getMembers().stream().filter(m -> m.getUser().isBot()).toArray().length, g.getOwner().getUser().getIdLong(), g.getOwner().getEffectiveName(),
                        Misc.createInviteLink(g), g.getIconUrl());
            } else {
                getDatabase().addNewServer(g.getIdLong(), g.getName(), g.getMembers().stream().filter(m ->
                                !(m.getOnlineStatus() == OnlineStatus.OFFLINE || m.getOnlineStatus() == OnlineStatus.INVISIBLE)).toArray().length, g.getMembers().size(),
                        g.getMembers().stream().filter(m -> m.getUser().isBot()).toArray().length, g.getOwner().getUser().getIdLong(), g.getOwner().getEffectiveName(),
                        Misc.createInviteLink(g), g.getIconUrl(), Tags.GAMING.toString(), Langs.ENGLISH.toString());
            }
        }
    }
}
