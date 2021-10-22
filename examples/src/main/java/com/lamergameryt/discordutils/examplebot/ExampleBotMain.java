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

package com.lamergameryt.discordutils.examplebot;

import com.lamergameryt.discordutils.commands.CommandClientBuilder;
import com.lamergameryt.discordutils.commons.EventWaiter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class ExampleBotMain {
    public static void main(String[] args) {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setActivity(Activity.watching("DiscordUtilites Example"));
        builder.setStatus(OnlineStatus.IDLE);

        builder.addCommandPackage("com.lamergameryt.discordutils.examplebot.commands");

        try {
            JDABuilder.createDefault(System.getenv("BOT_TOKEN"))
                    .addEventListeners(
                            builder.build(),
                            new EventWaiter()    // Required to use EventWaiter.
                    ).build();
        } catch (LoginException e) {
            System.out.println("An invalid token was provided.");
        }
    }
}
