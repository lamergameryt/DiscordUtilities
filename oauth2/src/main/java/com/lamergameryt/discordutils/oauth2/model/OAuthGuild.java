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

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class OAuthGuild {
    private String id;
    private String name;
    private String icon;
    private boolean owner;
    private Integer permissions;
    private List<String> features;

    public List<OAuthPermission> getPermissionList() {
        List<OAuthPermission> permissionList = new LinkedList<>();
        for (OAuthPermission permission : OAuthPermission.values()) {
            if (permission.isIn(permissions)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isOwner() {
        return owner;
    }

    public List<String> getFeatures() {
        return features;
    }
}
