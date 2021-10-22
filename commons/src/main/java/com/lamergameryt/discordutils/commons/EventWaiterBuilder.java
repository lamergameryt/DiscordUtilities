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

package com.lamergameryt.discordutils.commons;

import net.dv8tion.jda.api.events.GenericEvent;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class EventWaiterBuilder<T extends GenericEvent> {
    private final Class<T> classType;
    private Predicate<T> condition;
    private Consumer<T> action;
    private long timeout = -1;
    private TimeUnit timeoutUnit = null;
    private Runnable timeoutAction = null;

    public EventWaiterBuilder(Class<T> classType) {
        this.classType = classType;
    }

    public EventWaiterBuilder<T> withCondition(Predicate<T> condition) {
        this.condition = condition;
        return this;
    }

    public EventWaiterBuilder<T> setTimeout(long timeout, TimeUnit timeoutUnit) {
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
        return this;
    }

    public EventWaiterBuilder<T> onAction(Consumer<T> action) {
        this.action = action;
        return this;
    }

    public EventWaiterBuilder<T> onTimeout(Runnable timeoutAction) {
        this.timeoutAction = timeoutAction;
        return this;
    }

    public void submit() {
        EventWaiter.waitForEvent(classType, condition, action, timeout, timeoutUnit, timeoutAction);
    }
}
