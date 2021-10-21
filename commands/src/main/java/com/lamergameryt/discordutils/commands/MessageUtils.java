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

import java.util.ArrayList;

@SuppressWarnings("unused")
public class MessageUtils {
    public static ArrayList<String> splitMessage(String message) {
        ArrayList<String> messages = new ArrayList<>();
        if (message != null) {
            message = message.replace("@everyone", "@\u0435veryone")
                    .replace("@here", "@h\u0435re").trim();
            while (message.length() > 2000) {
                int leeway = 2000 - (message.length() % 2000);
                int index = message.lastIndexOf("\n", 2000);

                if (index < leeway)
                    index = message.lastIndexOf(" ", 2000);

                if (index < leeway)
                    index = 2000;
                String temp = message.substring(0, index).trim();

                if (!temp.equals(""))
                    messages.add(temp);
                message = message.substring(index).trim();
            }

            if (!message.equals(""))
                messages.add(message);
        }

        return messages;
    }
}
