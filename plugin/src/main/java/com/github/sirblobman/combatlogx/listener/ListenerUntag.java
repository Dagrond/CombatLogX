package com.github.sirblobman.combatlogx.listener;

import java.util.List;
import java.util.Locale;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.configuration.CommandConfiguration;
import com.github.sirblobman.combatlogx.api.configuration.PunishConfiguration;
import com.github.sirblobman.combatlogx.api.event.PlayerUntagEvent;
import com.github.sirblobman.combatlogx.api.listener.CombatListener;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;
import com.github.sirblobman.combatlogx.api.manager.IPlaceholderManager;
import com.github.sirblobman.combatlogx.api.manager.IPunishManager;
import com.github.sirblobman.combatlogx.api.object.UntagReason;

import org.jetbrains.annotations.NotNull;

public final class ListenerUntag extends CombatListener {
    public ListenerUntag(@NotNull ICombatLogX plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        if (!isInCombat(player)) {
            return;
        }

        String kickReason = e.getReason();
        UntagReason untagReason = (isKickReasonIgnored(kickReason) ? UntagReason.EXPIRE : UntagReason.KICK);

        ICombatManager combatManager = getCombatManager();
        combatManager.untag(player, untagReason);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (!isInCombat(player)) {
            return;
        }

        ICombatManager combatManager = getCombatManager();
        combatManager.untag(player, UntagReason.QUIT);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onUntag(PlayerUntagEvent e) {
        Player player = e.getPlayer();
        UntagReason untagReason = e.getUntagReason();
        sendUntagMessage(player, untagReason);

        List<Entity> previousEnemies = e.getPreviousEnemies();
        runUntagCommands(player, previousEnemies);

        ICombatLogX plugin = getCombatLogX();
        IPunishManager punishManager = plugin.getPunishManager();
        punishManager.punish(player, untagReason, previousEnemies);
    }

    private boolean isKickReasonIgnored(String kickReason) {
        ICombatLogX plugin = getCombatLogX();
        PunishConfiguration punishConfiguration = plugin.getPunishConfiguration();

        List<String> kickIgnoreList = punishConfiguration.getKickIgnoreList();
        if (kickIgnoreList.isEmpty()) {
            return false;
        }

        if (kickIgnoreList.contains("*")) {
            return true;
        }

        for (String kickIgnoreMessage : kickIgnoreList) {
            if (kickReason.contains(kickIgnoreMessage)) {
                return true;
            }
        }

        return false;
    }

    private void sendUntagMessage(Player player, UntagReason untagReason) {
        if (!untagReason.isExpire()) {
            return;
        }

        String untagReasonName = untagReason.name();
        String untagReasonLower = untagReasonName.toLowerCase(Locale.US);
        String untagReasonReplaced = untagReasonLower.replace('_', '-');

        ICombatLogX plugin = getCombatLogX();
        LanguageManager languageManager = plugin.getLanguageManager();
        String languagePath = ("combat-timer." + untagReasonReplaced);
        languageManager.sendMessageWithPrefix(player, languagePath);
    }

    private void runUntagCommands(Player player, List<Entity> enemyList) {
        ICombatLogX plugin = getCombatLogX();
        CommandConfiguration commandConfiguration = plugin.getCommandConfiguration();
        List<String> untagCommandList = commandConfiguration.getUntagCommands();
        if (untagCommandList.isEmpty()) {
            return;
        }

        IPlaceholderManager placeholderManager = plugin.getPlaceholderManager();
        placeholderManager.runReplacedCommands(player, enemyList, untagCommandList);
    }
}
