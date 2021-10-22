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

package com.lamergameryt.discordutils.oauth2;

import java.io.IOException;
import java.util.Scanner;

public class OAuthHandler {
    public static void main(String[] args) {
        DiscordOAuth handler = new DiscordOAuth(
                System.getenv("CLIENT_ID"),
                System.getenv("CLIENT_SECRET"),
                System.getenv("REDIRECT_URI"),
                new String[]{"identify", "email"}
        );


        String authUrl = handler.getAuthorizationURL(null);
        System.out.println("Authorize using: " + authUrl);

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your code: ");
        String code = sc.nextLine();

        try {
            System.out.println("Access token: " + handler.getTokens(code).getAccessToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
