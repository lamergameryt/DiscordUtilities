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

package com.lamergameryt.discordutils.commands.impl;

import com.lamergameryt.discordutils.commands.CommandClient;
import com.lamergameryt.discordutils.commands.CommandEvent;
import com.lamergameryt.discordutils.commands.SlashCommand;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class CommandClientImpl implements CommandClient, EventListener {
    private final Logger logger = JDALogger.getLog(CommandClientImpl.class);
    private final Activity activity;
    private final OnlineStatus status;
    private final ScheduledExecutorService executor;
    private final LinkedList<SlashCommand> commands;
    private final HashMap<String, OffsetDateTime> cooldowns = new HashMap<>();

    public CommandClientImpl(Activity activity, OnlineStatus status, ScheduledExecutorService executor,
                             LinkedList<SlashCommand> commands) {
        this.activity = activity;
        this.status = status;
        this.executor = executor;
        this.commands = commands;
    }

    @Override
    public List<SlashCommand> getCommands() {
        return commands;
    }

    @Override
    public ScheduledExecutorService getExecutorService() {
        return executor;
    }

    @Override
    public OffsetDateTime getCooldown(String commandName) {
        return cooldowns.get(commandName);
    }

    @Override
    public int getRemainingCooldown(String commandName) {
        if (cooldowns.containsKey(commandName)) {
            int time = (int) Math.ceil(OffsetDateTime.now().until(cooldowns.get(commandName), ChronoUnit.MILLIS) / 1000D);
            if (time <= 0) {
                cooldowns.remove(commandName);
                return 0;
            }
            return time;
        }
        return 0;
    }

    @Override
    public void applyCooldown(String commandName, int seconds) {
        cooldowns.put(commandName, OffsetDateTime.now().plusSeconds(seconds));
    }

    @Override
    public void cleanCooldowns() {
        OffsetDateTime now = OffsetDateTime.now();
        cooldowns.keySet().stream().filter((str) -> (cooldowns.get(str).isBefore(now)))
                .collect(Collectors.toList()).forEach(cooldowns::remove);
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent)
            onReady((ReadyEvent) event);
        else if (event instanceof SlashCommandEvent)
            onCommand((SlashCommandEvent) event);
    }

    private void onReady(ReadyEvent event) {
        event.getJDA().getPresence().setPresence(status, activity);
        CommandListUpdateAction action = event.getJDA().updateCommands();
        for (SlashCommand command : commands) {
            if (command.isSkip()) continue;

            if (command.getGuilds().length == 0) {
                action = action.addCommands(command.getData());
                continue;
            }

            for (String guild : command.getGuilds()) {
                Guild g = event.getJDA().getGuildById(guild);
                if (g == null) continue;

                command.upsertGuild(g);
            }

            logger.info("The command '" + command.getName() + "' was loaded successfully.");
        }

        action.queue();
    }

    private void onCommand(SlashCommandEvent event) {
        for (SlashCommand command : commands) {
            if (command.getName().equals(event.getName())) {
                command.run(new CommandEvent(event, this));
                break;
            }
        }
    }
}
