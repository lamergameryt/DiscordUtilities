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

package com.lamergameryt.discordutils.commands;

import com.lamergameryt.discordutils.commands.annotations.BotPermissions;
import com.lamergameryt.discordutils.commands.annotations.Cooldown;
import com.lamergameryt.discordutils.commands.annotations.UserPermissions;
import com.lamergameryt.discordutils.commands.impl.CommandClientImpl;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class CommandClientBuilder {
    private final Logger logger = JDALogger.getLog(CommandClientBuilder.class);
    private boolean sync = true;
    private Activity activity = Activity.playing("default");
    private OnlineStatus status = OnlineStatus.ONLINE;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final LinkedList<SlashCommand> commands = new LinkedList<>();
    private final ArrayList<String> commandPackages = new ArrayList<>();

    public CommandClientBuilder setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public CommandClientBuilder setStatus(OnlineStatus status) {
        this.status = status;
        return this;
    }

    public CommandClientBuilder setExecutor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public CommandClientBuilder addCommand(SlashCommand command) {
        this.commands.add(command);
        return this;
    }

    public CommandClientBuilder addCommands(SlashCommand... commands) {
        Collections.addAll(this.commands, commands);
        return this;
    }

    public CommandClientBuilder addCommandPackage(String qualifiedPackage) {
        this.commandPackages.add(qualifiedPackage);
        return this;
    }

    public CommandClientBuilder syncCommands(boolean sync) {
        this.sync = sync;
        return this;
    }

    public CommandClient build() {
        for (String qualifiedPackage : commandPackages) {
            if (qualifiedPackage != null) {
                new Reflections(qualifiedPackage).getSubTypesOf(SlashCommand.class).forEach(command -> {
                    try {
                        SlashCommand instance = command.getDeclaredConstructor().newInstance();
                        if (instance.isSkip()) return;

                        Cooldown cooldown = instance.getClass().getAnnotation(Cooldown.class);
                        if (cooldown != null) {
                            if (cooldown.duration() != 0)
                                instance.cooldown = cooldown.duration();

                            if (cooldown.scope() != CooldownScope.USER)
                                instance.cooldownScope = cooldown.scope();
                        }

                        BotPermissions botPermissions = instance.getClass().getAnnotation(BotPermissions.class);
                        if (botPermissions != null) {
                            if (botPermissions.permissions().length != 0)
                                instance.botPermissions = botPermissions.permissions();
                        }

                        UserPermissions userPermissions = instance.getClass().getAnnotation(UserPermissions.class);
                        if (userPermissions != null) {
                            if (userPermissions.permissions().length != 0)
                                instance.userPermissions = userPermissions.permissions();
                        }

                        commands.add(instance);
                    } catch (Exception e) {
                        logger.error("Unable to load command " + command.getName(), e);
                    }
                });
            }
        }

        return new CommandClientImpl(activity, status, executor, commands, sync);
    }
}
