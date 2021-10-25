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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <h1><b>Commands in DiscordUtilities</b></h1>
 *
 * <p>The internal interface for SlashCommands used in DiscordUtilities.</p>
 *
 * <p></p>
 *
 * @author Harsh Patil (LamerGamerYT)
 */
@SuppressWarnings("unused")
public abstract class SlashCommand {
    /**
     * The name of the slash command.
     */
    protected String name = "null";

    /**
     * The help of the command displayed while the command is being executed.
     */
    protected String help = "no help available";

    /**
     * The options the command accepts.
     */
    protected ArrayList<OptionData> options = new ArrayList<>();

    /**
     * The {@link net.dv8tion.jda.api.Permission Permission}s a Member must have to use this command.
     * <br>These are only checked in a {@link net.dv8tion.jda.api.entities.Guild Guild} environment.
     */
    protected Permission[] userPermissions = new Permission[0];

    /**
     * The {@link net.dv8tion.jda.api.Permission Permission}s the bot must have to use a command.
     * <br>These are only checked in a {@link net.dv8tion.jda.api.entities.Guild Guild} environment.
     */
    protected Permission[] botPermissions = new Permission[0];

    /**
     * An {@code int} number of seconds users must wait before using this command again.
     */
    protected int cooldown = 0;

    /**
     * The {@link com.lamergameryt.discordutils.commands.CooldownScope CooldownScope}
     * of the command. This defines the scope of a cooldown.
     * <br>Default {@link com.lamergameryt.discordutils.commands.CooldownScope#USER CooldownScope.USER}.
     */
    protected CooldownScope cooldownScope = CooldownScope.USER;

    /**
     * The list of {@link net.dv8tion.jda.api.entities.Guild Guilds} the command can be used in.
     */
    protected String[] guilds = new String[0];

    /**
     * The list of users ids who can use the command.
     * <br/>This is applied only if {@link com.lamergameryt.discordutils.commands.SlashCommand#guilds} is not empty.
     */
    protected String[] restrictedUsers = new String[0];

    /**
     * The list of role ids who can use the command.
     * <br/>This is applied only if {@link com.lamergameryt.discordutils.commands.SlashCommand#guilds} is not empty.
     */
    protected String[] restrictedRoles = new String[0];

    /**
     * {@code true} if the command should not be registered.
     * <br/>Default {@code false}
     */
    protected boolean skip = false;

    /**
     * {@code true} if the command should be synced with every restart.
     * <br/>Default {@code true}
     */
    protected boolean sync = true;

    /**
     * {@code true} if the command can only be used in a NSFW Channel.
     * <br/>Default {@code false}
     */
    protected boolean nsfwOnly = false;

    /**
     * The main body of {@link com.lamergameryt.discordutils.commands.SlashCommand SlashCommand}.
     * <br/>This is what will be executed when a command is executed.
     *
     * @param event The {@link com.lamergameryt.discordutils.commands.CommandEvent CommandEvent} that
     *              triggered this command.
     */
    protected abstract void execute(CommandEvent event);

    /**
     * Performs all checks required before executing the command.
     *
     * @param event The {@link com.lamergameryt.discordutils.commands.CommandEvent CommandEvent} that
     *              triggered this command.
     */
    public final void run(CommandEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        if (guild == null || member == null) return;


        if (!guild.getSelfMember().hasPermission(botPermissions)) {
            event.send("The bot requires the permissions: " + Arrays.stream(botPermissions).map(Permission::getName)
                            .collect(Collectors.joining(", ")))
                    .setEphemeral(true).queue();
            return;
        }

        if (!event.getMember().hasPermission(userPermissions)) {
            event.send("You require the permissions: " + Arrays.stream(userPermissions).map(Permission::getName)
                            .collect(Collectors.joining(", ")))
                    .setEphemeral(true).queue();
            return;
        }

        if (guilds.length != 0) {
            if (restrictedUsers.length != 0 || restrictedRoles.length != 0) {
                boolean flag = false;
                for (String id : restrictedUsers) {
                    if (event.getUser().getId().equals(id)) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    List<String> roleList = member.getRoles().stream().map(Role::getId).collect(Collectors.toList());
                    for (String id : restrictedRoles) {
                        if (roleList.contains(id)) {
                            flag = true;
                            break;
                        }
                    }
                }

                if (!flag) {
                    event.send("You cannot use this command!").setEphemeral(true).queue();
                    return;
                }
            }
        }

        if (cooldown > 0) {
            String key = getCooldownKey(event);
            int remaining = event.getClient().getRemainingCooldown(key);
            if (remaining > 0) {
                event.send("The command is on cooldown for " + remaining + " seconds.").setEphemeral(true).queue();
                return;
            } else {
                event.getClient().applyCooldown(key, cooldown);
            }
        }

        if (nsfwOnly && !event.getTextChannel().isNSFW()) {
            event.send("This command can only be used in a NSFW channel.").setEphemeral(true).queue();
            return;
        }

        execute(event);
    }

    /**
     * Generate a cooldown key based on the
     * {@link com.lamergameryt.discordutils.commands.CooldownScope CooldownScope} specified.
     *
     * @param event The {@link com.lamergameryt.discordutils.commands.CommandEvent} to generate the cooldown key for.
     * @return The generated cooldown key.
     */
    private String getCooldownKey(CommandEvent event) {
        switch (cooldownScope) {
            case USER:
                return cooldownScope.getKey(name, event.getUser().getId());
            case USER_GUILD:
                return cooldownScope.getKey(name, event.getUser().getId(), Objects.requireNonNull(event.getGuild()).getId());
            case USER_CHANNEL:
                return cooldownScope.getKey(name, event.getUser().getId(), event.getChannel().getId());
            case CHANNEL:
                return cooldownScope.getKey(name, event.getChannel().getId());
            case GUILD:
                return cooldownScope.getKey(name, Objects.requireNonNull(event.getGuild()).getId());
            case GLOBAL:
                return cooldownScope.getKey(name);
            default:
                return "";
        }
    }

    public final CommandData getData() {
        return new CommandData(name, help).addOptions(options);
    }

    public final CompletableFuture<Command> upsertGuild(Guild guild) {
        CommandData data = new CommandData(name, help);
        data.addOptions(options);

        return guild.upsertCommand(data).submit().whenComplete((command, t) -> {
            if (t != null) return;
            if (restrictedUsers.length == 0 && restrictedRoles.length == 0)
                return;

            ArrayList<CommandPrivilege> privileges = new ArrayList<>();
            privileges.add(CommandPrivilege.disable(guild.getPublicRole()));

            for (String restrictedUser : restrictedUsers)
                privileges.add(CommandPrivilege.enableUser(restrictedUser));
            for (String restrictedRole : restrictedRoles)
                privileges.add(CommandPrivilege.enableRole(restrictedRole));

            command.updatePrivileges(guild, privileges).queue();
        });
    }

    public boolean isSkip() {
        return skip;
    }

    public boolean isSync() {
        return sync;
    }

    public String[] getGuilds() {
        return guilds;
    }

    public String getName() {
        return name;
    }
}
