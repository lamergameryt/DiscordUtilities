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
import com.lamergameryt.discordutils.commands.annotations.Cooldown;

@CommandMarker
@Cooldown(duration = 5)
public class HelloSubCommand extends SlashCommand {
    public HelloSubCommand() {
        super.name = "hello";
        super.help = "Returns 👋";
        super.skip = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.send("👋").queue();
    }
}
