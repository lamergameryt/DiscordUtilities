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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@SuppressWarnings("unused")
public interface CommandClient {
    List<SlashCommand> getCommands();

    ScheduledExecutorService getExecutorService();

    OffsetDateTime getCooldown(String commandName);

    int getRemainingCooldown(String commandName);

    void applyCooldown(String commandName, int seconds);

    void cleanCooldowns();
}
