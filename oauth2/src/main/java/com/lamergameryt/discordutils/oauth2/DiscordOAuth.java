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
import com.lamergameryt.discordutils.oauth2.model.TokenResponse;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.lamergameryt.discordutils.oauth2.DiscordAPI.BASE_URI;

@SuppressWarnings("unused")
public class DiscordOAuth {
    private static final Gson gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
    private static final String GRANT_TYPE_AUTHORIZATION = "authorization_code";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private final Logger logger = LoggerFactory.getLogger(DiscordOAuth.class);
    private final String clientID;
    private final String clientSecret;
    private final String redirectUri;
    private final String[] scope;

    public DiscordOAuth(String clientID, String clientSecret, String redirectUri, String[] scope) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = scope;
    }

    private static TokenResponse toObject(String str) {
        return gson.fromJson(str, TokenResponse.class);
    }

    public String getAuthorizationURL(String state) {
        URIBuilder builder;
        try {
            builder = new URIBuilder(BASE_URI + "/oauth2/authorize");
        } catch (URISyntaxException e) {
            logger.error("Failed to initialize URIBuilder", e);
            return null;
        }

        builder.addParameter("response_type", "code");
        builder.addParameter("client_id", clientID);
        builder.addParameter("redirect_uri", redirectUri);
        if (state != null && state.length() > 0) {
            builder.addParameter("state", state);
        }

        return builder + "&scope=" + String.join("%20", scope);
    }

    public TokenResponse getTokens(String code) throws IOException {
        Connection request = Jsoup.connect(BASE_URI + "/oauth2/token")
                .data("client_id", clientID)
                .data("client_secret", clientSecret)
                .data("grant_type", GRANT_TYPE_AUTHORIZATION)
                .data("redirect_uri", redirectUri)
                .data("code", code)
                .data("scope", String.join(" ", scope))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .ignoreContentType(true);

        String response = request.post().body().text();

        return toObject(response);
    }

    public TokenResponse refreshTokens(String refresh_token) throws IOException {
        Connection request = Jsoup.connect(BASE_URI + "/oauth2/token")
                .data("client_id", clientID)
                .data("client_secret", clientSecret)
                .data("grant_type", GRANT_TYPE_REFRESH_TOKEN)
                .data("refresh_token", refresh_token)
                .ignoreContentType(true);

        String response = request.post().body().text();

        return toObject(response);
    }
}
