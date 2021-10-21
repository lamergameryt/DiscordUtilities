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

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import net.dv8tion.jda.internal.interactions.CommandInteractionImpl;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CommandEvent extends SlashCommandEvent {
    private final CommandClient client;

    public CommandEvent(SlashCommandEvent event, CommandClient client) {
        super(event.getJDA(), event.getResponseNumber(), (CommandInteractionImpl) event.getInteraction());
        this.client = client;
    }

    @NotNull
    public ReplyAction send(@NotNull String content) {
        return this.reply(content.replaceAll("@everyone", "@\u0435veryone")
                .replaceAll("@here", "@h\u0435re"));
    }

    @NotNull
    public ReplyAction sendFormatted(@NotNull String content, Object... args) {
        return this.replyFormat(content.replaceAll("@everyone", "@\u0435veryone")
                .replaceAll("@here", "@h\u0435re"), args);
    }

    public InteractionHook hook() {
        return this.getHook();
    }

    public CommandClient getClient() {
        return client;
    }
}
