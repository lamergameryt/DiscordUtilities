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

@SuppressWarnings("unused")
public enum OAuthPermission {
    CREATE_INSTANT_INVITE(0x00000001),
    KICK_MEMBERS(0x00000002),
    BAN_MEMBERS(0x00000004),
    ADMINISTRATOR(0x00000008),
    MANAGE_CHANNELS(0x00000010),
    MANAGE_GUILD(0x00000020),
    ADD_REACTIONS(0x00000040),
    VIEW_AUDIT_LOG(0x00000080),
    VIEW_CHANNEL(0x00000400),
    SEND_MESSAGES(0x00000800),
    SEND_TTS_MESSAGES(0x00001000),
    MANAGE_MESSAGES(0x00002000),
    EMBED_LINKS(0x00004000),
    ATTACH_FILES(0x00008000),
    READ_MESSAGE_HISTORY(0x00010000),
    MENTION_EVERYONE(0x00020000),
    USE_EXTERNAL_EMOJIS(0x00040000),
    CONNECT(0x00100000),
    SPEAK(0x00200000),
    MUTE_MEMBERS(0x00400000),
    DEAFEN_MEMBERS(0x00800000),
    MOVE_MEMBERS(0x01000000),
    USE_VAD(0x02000000),
    PRIORITY_SPEAKER(0x00000100),
    STREAM(0x00000200),
    CHANGE_NICKNAME(0x04000000),
    MANAGE_NICKNAMES(0x08000000),
    MANAGE_ROLES(0x10000000),
    MANAGE_WEBHOOKS(0x20000000),
    MANAGE_EMOJIS(0x40000000);

    private final int value;

    OAuthPermission(int value) {
        this.value = value;
    }

    boolean isIn(int permissions) {
        return (permissions & value) == value;
    }

    int value() {
        return value;
    }
}
