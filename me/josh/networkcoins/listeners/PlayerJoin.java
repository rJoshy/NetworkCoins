package me.josh.networkcoins.listeners;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import me.josh.networkcoins.API;
import me.josh.networkcoins.NetworkCoins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerJoin implements Listener {
	
	static FileConfiguration config = NetworkCoins.getInstance().getConfig();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		try {
			Player p = e.getPlayer();
			Connection c = NetworkCoins.getConnection();
			String uuid = p.getUniqueId().toString();
			Statement check = c.createStatement();
			
			ResultSet res = check.executeQuery("SELECT player_uuid FROM NetworkCoins WHERE player_uuid = '" + uuid + "';");
			if (!res.next()) {
				Statement update = c.createStatement();
				update.executeUpdate("INSERT INTO NetworkCoins VALUES ('" + uuid + "',0);");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (config.getBoolean("scoreboardEnabled")) {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();
			Objective o = board.registerNewObjective("test", "dummy");
			o.setDisplaySlot(DisplaySlot.SIDEBAR);
			o.setDisplayName(config.getString("scoreboardPrefix").replace("%currency", config.getString("currency")).replace("&", "¤"));
			
			@SuppressWarnings("deprecation")
			Score s1 = o.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "" + ChatColor.BOLD + "Balance"));
			s1.setScore(2);
			
			@SuppressWarnings("deprecation")
			Score s2 = o.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "  " + API.getCoinsString(e.getPlayer())));
			s2.setScore(1);
			
			e.getPlayer().setScoreboard(board);
		}
	}

}
