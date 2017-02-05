package com.SirBlobman.combat_log;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.SirBlobman.combat_log.compat.TitleManagerScoreboard;
import com.SirBlobman.combat_log.nms.NMS;
import com.SirBlobman.combat_log.nms.action.Action;

public class CombatLog extends JavaPlugin implements CommandExecutor
{
	public static CombatLog instance;
	public static TitleManagerScoreboard TMS;
	private static Action action;
	private static final Server S = Bukkit.getServer();
	private static final PluginManager PM = S.getPluginManager();
	private static final BukkitScheduler BS = S.getScheduler();
	
	@Override
	public void onEnable()
	{
		instance = this;
		Config.load();
		boolean a = Config.ACTION_BAR;
		if(a) action = NMS.getAction();
		PM.registerEvents(new Events(), this);
		BS.runTaskTimer(this, new Combat(), 0L, 0L);
		S.broadcastMessage("§2CombatLog Enabled");
		boolean u = Config.UPDATE_CHECKER;
		if(u) Update.print();
		
		boolean t = PM.isPluginEnabled("TitleManager");
		if(t) {
			Plugin api = PM.getPlugin("TitleManager");
			Config.TITLE_MANAGER = true;
			TMS = new TitleManagerScoreboard(api);
		}
	}
	
	@Override
	public void onDisable()
	{
		for(Player p : Bukkit.getOnlinePlayers()) Combat.remove(p);
		S.broadcastMessage("§4CombatLog Disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args)
	{
		String cmd = c.getName().toLowerCase();
		if(cmd.equals("cl"))
		{
			if(cs instanceof Player)
			{
				Player p = (Player) cs;
				if(Combat.inCombat(p))
				{
					String msg = Config.option("messages.in combat", Combat.timeLeft(p));
					p.sendMessage(msg);
					return true;
				}
				p.sendMessage(Config.option("messages.not in combat"));
				return true;
			}
			cs.sendMessage("You are not a Player");
			return true;
		}
		if(cmd.equals("clreload"))
		{
			Config.load();
			cs.sendMessage(Config.option("messages.reload config"));
			return true;
		}
		return false;
	}
	
	public static void action(Player p, String msg)
	{
		boolean a = Config.ACTION_BAR;
		if(a)
		{
			String c = ChatColor.translateAlternateColorCodes('&', msg);
			if(action != null) action.action(p, c);
		}
	}
}