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
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"rawtypes", "unused"})
public class EventWaiter implements EventListener {
    private static final Logger logger = LoggerFactory.getLogger(EventWaiter.class);
    private static final HashMap<Class<?>, Set<WaitingEvent>> waitingEvents = new HashMap<>();
    private static final ScheduledExecutorService threadpool = Executors.newSingleThreadScheduledExecutor();
    private final boolean shutdownAutomatically = true;
    private static boolean instantiated;

    public EventWaiter() {
        if (instantiated)
            throw new IllegalStateException("The EventWaiter is already registered.");

        instantiated = true;
    }

    public boolean isShutdown() {
        return threadpool.isShutdown();
    }

    public static <T extends GenericEvent> EventWaiterBuilder<T> of(Class<T> eventType) {
        return new EventWaiterBuilder<>(eventType);
    }

    public static <T extends GenericEvent> void waitForEvent(Class<T> classType, Predicate<T> condition,
                                                             Consumer<T> action, long timeout, TimeUnit unit,
                                                             Runnable timeoutAction) {
        WaitingEvent<T> we = new WaitingEvent<>(condition, action);
        Set<WaitingEvent> set = waitingEvents.computeIfAbsent(classType, c -> new HashSet<>());
        set.add(we);

        if (timeout > 0 && unit != null) {
            threadpool.schedule(() ->
            {
                try {
                    if (set.remove(we) && timeoutAction != null)
                        timeoutAction.run();
                } catch (Exception ex) {
                    logger.error("Failed to run timeoutAction", ex);
                }
            }, timeout, unit);
        }
    }

    @Override
    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public final void onEvent(GenericEvent event) {
        Class c = event.getClass();
        while (c != null) {
            if (waitingEvents.containsKey(c)) {
                Set<WaitingEvent> set = waitingEvents.get(c);
                WaitingEvent[] toRemove = set.toArray(new WaitingEvent[0]);
                set.removeAll(Stream.of(toRemove).filter(i -> i.attempt(event)).collect(Collectors.toSet()));
            }
            if (event instanceof ShutdownEvent && shutdownAutomatically) {
                threadpool.shutdown();
            }
            c = c.getSuperclass();
        }
    }

    public void shutdown() {
        if (shutdownAutomatically)
            throw new UnsupportedOperationException("Shutting down EventWaiters that are set to automatically close is unsupported!");

        threadpool.shutdown();
    }

    public static class WaitingEvent<T extends GenericEvent> {
        final Predicate<T> condition;
        final Consumer<T> action;

        WaitingEvent(Predicate<T> condition, Consumer<T> action) {
            this.condition = condition;
            this.action = action;
        }

        boolean attempt(T event) {
            if (condition.test(event)) {
                action.accept(event);
                return true;
            }
            return false;
        }
    }
}
