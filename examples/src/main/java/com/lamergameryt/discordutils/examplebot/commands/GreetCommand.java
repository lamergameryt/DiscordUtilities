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
import com.lamergameryt.discordutils.commands.annotations.BotPermissions;
import com.lamergameryt.discordutils.commands.annotations.CommandMarker;
import com.lamergameryt.discordutils.commands.annotations.Cooldown;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@CommandMarker
@BotPermissions(permissions = {Permission.KICK_MEMBERS, Permission.BAN_MEMBERS})
@Cooldown(duration = 5)
public class GreetCommand extends SlashCommand {
    public GreetCommand() {
        super.name = "greet";
        super.help = "Greet a role!";
        super.guilds = new String[]{"855393545049473035"};
        super.options.add(new OptionData(OptionType.ROLE, "role", "The role to greet!"));
    }

    @Override
    protected boolean execute(CommandEvent event) {
        OptionMapping role = event.getOption("role");
        event.sendFormatted("Hello, %s.", role == null ? "world" : role.getAsRole().getName()).queue();
        return true;
    }
}
