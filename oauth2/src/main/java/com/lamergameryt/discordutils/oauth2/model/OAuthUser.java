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

package com.lamergameryt.discordutils.oauth2.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OAuthUser {
    private String id;
    private String username;
    private String avatar;
    private String discriminator;
    private Boolean bot;
    private Boolean system;
    @SerializedName("mfa_enabled")
    private Boolean mfaEnabled;
    private String locale;
    private Boolean verified;
    private String email;
    private Long flags;
    @SerializedName("premium_type")
    private Integer premiumType;

    public String getFullUsername() {
        return username + "#" + discriminator;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public Boolean getBot() {
        return bot;
    }

    public Boolean getSystem() {
        return system;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public String getLocale() {
        return locale;
    }

    public Boolean getVerified() {
        return verified;
    }

    public String getEmail() {
        return email;
    }

    public Long getFlags() {
        return flags;
    }

    public Integer getPremiumType() {
        return premiumType;
    }
}
