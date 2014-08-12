package me.josh.networkcoins;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class API {
	
	static FileConfiguration config = NetworkCoins.getInstance().getConfig();
	
	public static Integer getCoins(Player p) {
		try {
			Connection c = NetworkCoins.getConnection();
			String uuid = p.getUniqueId().toString();
			Statement check = c.createStatement();
			
			ResultSet res = check.executeQuery("SELECT * FROM NetworkCoins WHERE player_uuid = '" + uuid + "';");
			res.next();
			
			if (res.getString("player_uuid") != null) {
				int coins = res.getInt("balance");
				
				return coins;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String getCoinsString(Player p) {
		try {
			Connection c = NetworkCoins.getConnection();
			String uuid = p.getUniqueId().toString();
			Statement check = c.createStatement();
			
			ResultSet res = check.executeQuery("SELECT * FROM NetworkCoins WHERE player_uuid = '" + uuid + "';");
			res.next();
			
			if (res.getString("player_uuid") != null) {
				Integer coins_int = res.getInt("balance");
				String coins = coins_int.toString();
				
				return coins;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void addCoins(Player p, Integer i) {
		try {
			Connection c = NetworkCoins.getConnection();
			String uuid = p.getUniqueId().toString();
			Statement check = c.createStatement();
			
			ResultSet res = check.executeQuery("SELECT * FROM NetworkCoins WHERE player_uuid ='" + uuid + "';");
			res.next();
			
			if (res.getString("player_uuid") != null) {
				int beforeCoins = res.getInt("balance");
				
				Statement update = c.createStatement();
				update.executeUpdate("UPDATE NetworkCoins SET balance = " + (beforeCoins + i) + " WHERE player_uuid = '" + uuid + "';");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void takeCoins(Player sender, Player p, Integer i) {
		try {
			Connection c = NetworkCoins.getConnection();
			String uuid = p.getUniqueId().toString();
			Statement check = c.createStatement();
			
			ResultSet res = check.executeQuery("SELECT * FROM NetworkCoins WHERE player_uuid = '" + uuid + "';");
			res.next();
			
			if (res.getString("player_uuid") != null) {
				int beforeCoins = res.getInt("balance");
				
				if (beforeCoins - i < 0) {
					if (!config.getBoolean("bypassLimit")) {
						return; //This will stop them if they try to set a players balance to a -(number)
					} else if (config.getBoolean("bypassLimit")) {
						Statement bypassUpdate = c.createStatement();
						bypassUpdate.executeUpdate("UPDATE NetworkCoins SET balance = " + (beforeCoins - i) + " WHERE player_uuid = '" + uuid + "';");
						return; //The operation is now finished so we can stop it
					}
				} else if (beforeCoins == i) {
					Statement update = c.createStatement();
					update.executeUpdate("UPDATE NetworkCoins SET balance = 0 WHERE player_uuid = '" + uuid + "';");
				} else if (beforeCoins > i) {
					Statement update = c.createStatement();
					update.executeUpdate("UPDATE NetworkCoins SET balance = " + (beforeCoins - i) + " WHERE player_uuid = '" + uuid + "';");
				}
 			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void resetCoins(Player p) {
		try {
			Connection c = NetworkCoins.getConnection();
			String uuid = p.getUniqueId().toString();
			Statement check = c.createStatement();
			
			ResultSet res = check.executeQuery("SELECT * FROM NetworkCoins WHERE player_uuid = '" + uuid + "';");
			res.next();
			
			if (res.getString("player_uuid") != null) {
				Statement update = c.createStatement();
				update.executeUpdate("UPDATE NetworkCoins SET balance = 0 WHERE player_uuid = '" + uuid + "';");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}