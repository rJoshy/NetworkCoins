package me.josh.networkcoins.listeners;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import me.josh.networkcoins.NetworkCoins;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
	
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
	}

}
