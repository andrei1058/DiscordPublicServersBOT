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

package com.andrei1058.discordpublicservers.commands.service;

import com.andrei1058.discordpublicservers.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;

public class UnBanServer extends Command {

    public UnBanServer(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g, String s) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) /* todo msg can't write on this channel */
            return;
        if (!sender.getUser().getId().equalsIgnoreCase(getConfig().getOwnerID())) return;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(getConfig().getColor());
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            if (s.isEmpty()){
                eb.setTitle("Sorry");
                eb.setDescription("Insufficient arguments!\nUsage: 00unbanServer id");
                c.sendMessage(eb.build()).queue();
                return;
            }
            try {
                Long.parseLong(args[0]);
            } catch (Exception e){
                eb.setTitle("Sorry");
                eb.setDescription("args 1 must be an id");
                c.sendMessage(eb.build()).queue();
                return;
            }
            if (!getDatabase().isGuildBanned(args[0])){
                eb.setTitle("Sorry");
                eb.setDescription("This server isn't banned!");
                c.sendMessage(eb.build()).queue();
                return;
            }
            if (args.length > 0){
                eb.setTitle("Server unbanned");
                eb.setDescription(args[0]+" was unbanned.");
                c.sendMessage(eb.build()).queue();
                getDatabase().unbanGuild(Long.valueOf(args[0]));
            } else {
                eb.setTitle("Sorry");
                eb.setDescription("Insufficient args!");
                c.sendMessage(eb.build()).queue();
            }
        } else {
            if (s.isEmpty()){
                c.sendMessage("Sorry :frowning:\nInsufficient arguments!\nUsage: 00unbanServer id").queue();
                return;
            }
            try {
                Long.parseLong(args[0]);
            } catch (Exception e){
                c.sendMessage("Sorry :frowning:\nargs 1 must be an id").queue();
                return;
            }
            if (!getDatabase().isGuildBanned(args[0])){
                c.sendMessage("Sorry :frowning:\nThis server isn't banned!").queue();
                return;
            }
            if (args.length > 2){
                c.sendMessage("Server unbanned\n"+args[0]+" was unbanned!").queue();
                getDatabase().unbanGuild(Long.valueOf(args[0]));
            } else {
                c.sendMessage("Insufficient args!").queue();
            }
        }
    }
}
