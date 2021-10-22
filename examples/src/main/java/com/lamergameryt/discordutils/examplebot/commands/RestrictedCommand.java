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
import com.lamergameryt.discordutils.commands.CooldownScope;
import com.lamergameryt.discordutils.commands.SlashCommand;
import com.lamergameryt.discordutils.commands.annotations.CommandMarker;
import com.lamergameryt.discordutils.commands.annotations.Cooldown;

@CommandMarker
@Cooldown(duration = 10, scope = CooldownScope.CHANNEL)
public class RestrictedCommand extends SlashCommand {
    public RestrictedCommand() {
        super.name = "restricted";
        super.help = "This command is restricted by a role.";
        super.guilds = new String[]{"855393545049473035"};
        super.restrictedRoles = new String[]{"900820637248028672"};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.send("You can use this command as you have the role: <@&900820637248028672>!")
                .setEphemeral(true).queue();
    }
}
