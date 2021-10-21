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

import com.lamergameryt.discordutils.commands.impl.CommandClientImpl;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SuppressWarnings("unused")
public class CommandClientBuilder {
    private Activity activity = Activity.playing("default");
    private OnlineStatus status = OnlineStatus.ONLINE;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final LinkedList<SlashCommand> commands = new LinkedList<>();

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

    public CommandClient build() {
        return new CommandClientImpl(activity, status, executor, commands);
    }
}
