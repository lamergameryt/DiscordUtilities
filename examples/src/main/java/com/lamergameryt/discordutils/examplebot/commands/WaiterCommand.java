/*
 * Copyright 2021 Harsh Patil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lamergameryt.discordutils.examplebot.commands;

import com.lamergameryt.discordutils.commands.CommandEvent;
import com.lamergameryt.discordutils.commands.SlashCommand;
import com.lamergameryt.discordutils.commands.annotations.CommandMarker;
import com.lamergameryt.discordutils.commons.EventWaiter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

@CommandMarker
public class WaiterCommand extends SlashCommand {
    public WaiterCommand() {
        super.name = "waiter";
        super.help = "I will wait for your message!";
        super.guilds = new String[]{"855393545049473035"};
    }

    @Override
    protected boolean execute(CommandEvent event) {
        event.send(event.getUser().getAsMention() + " send your message below!").setEphemeral(true).queue();
        EventWaiter.of(GuildMessageReceivedEvent.class)
                .withCondition(e -> e.getAuthor().getId().equals(event.getUser().getId()))
                .setTimeout(10, TimeUnit.SECONDS)
                .onTimeout(() -> event.getTextChannel().sendMessage(event.getUser().getAsMention() + " you failed to reply within 10 seconds.").queue())
                .onAction(e -> e.getChannel().sendMessage("You said: " + e.getMessage().getContentRaw()).queue())
                .submit();
        return true;
    }
}
