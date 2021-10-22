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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lamergameryt.discordutils.oauth2.model.OAuthConnection;
import com.lamergameryt.discordutils.oauth2.model.OAuthGuild;
import com.lamergameryt.discordutils.oauth2.model.OAuthUser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class DiscordAPI {
    public static final String BASE_URI = "https://discord.com/api";
    private static final Gson gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
    private final String accessToken;

    public DiscordAPI(String accessToken) {
        this.accessToken = accessToken;
    }

    private static <T> T toObject(String str, Class<T> clazz) {
        return gson.fromJson(str, clazz);
    }

    private Connection setHeaders(Connection request) {
        request = request.header("Authorization", "Bearer " + accessToken)
                .header("User-Agent", "LamerGamerYT-OAuth2");

        return request;
    }

    private String handleGet(String path) throws IOException {
        Connection request = Jsoup.connect(BASE_URI + path).ignoreContentType(true);
        request = setHeaders(request);

        return request.get().body().text();
    }

    public OAuthUser fetchUser() throws IOException {
        return toObject(handleGet("/users/@me"), OAuthUser.class);
    }

    public List<OAuthGuild> fetchGuilds() throws IOException {
        return Arrays.asList(toObject(handleGet("/users/@me/guilds"), OAuthGuild[].class));
    }

    public List<OAuthConnection> fetchConnections() throws IOException {
        return Arrays.asList(toObject(handleGet("/users/@me/connections"), OAuthConnection[].class));
    }
}
