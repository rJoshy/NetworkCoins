package me.josh.networkcoins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import me.josh.networkcoins.commands.CoinsCommand;
import me.josh.networkcoins.listeners.PlayerJoin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NetworkCoins extends JavaPlugin {
	
	public int checkInterval = (getConfig().getInt("connectionCheckInterval") * 1200);
	private static Connection c;
	
	public static Connection getConnection() {
		return c;
	}
	
	private static NetworkCoins instance;
	
	public static NetworkCoins getInstance() {
		return instance;
	}
	
	public void onEnable() {
		instance = this;
		
		saveDefaultConfig();
		
        getCommand("coins").setExecutor(new CoinsCommand());
		
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		
		try {
			String host = getConfig().getString("MySQL.Host");
			int port = getConfig().getInt("MySQL.Port");
			String dbname = getConfig().getString("MySQL.DbName");
			String user = getConfig().getString("MySQL.Username");
			String pass = getConfig().getString("MySQL.Password");
			
			openConnection(host, user, pass, dbname, port);
			
			if (!(c.isClosed())) {
				System.out.println("[NetworkCoins] Successfully connected to the database");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[NetworkCoins] Couldn't connect to the database, disabling NetworkCoins");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		
		System.out.println("[NetworkCoins] Successfully Enabled NetworkCoins v0.3 by MinecraftJoshjr. Please report all bugs so I can fix them. Thanks for using my plugin :)");
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (c == null) {
					try {
						System.out.println("[NetworkCoins] Connection is closed, attempting to reconnect to the database!");
						
						String host = getConfig().getString("MySQL.Host");
						int port = getConfig().getInt("MySQL.Port");
						String dbname = getConfig().getString("MySQL.DbName");
						String user = getConfig().getString("MySQL.Username");
						String pass = getConfig().getString("MySQL.Password");
						
						openConnection(host, user, pass, dbname, port);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("[NetworkCoins] Connection is still open and active! Yay :D");
				}
			}
		}, 0L, checkInterval);
	}
	
	public void onDisable() {
		closeConnection();
	}
	
	public void openConnection(String hostname, String username, String password, String database, Integer port) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		c = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
		
		String update_string = "CREATE TABLE IF NOT EXISTS NetworkCoins"
	            + "  (player_uuid           VARCHAR(50),"
	            + "   balance               INTEGER)";

	    Statement update = c.createStatement();
	    update.execute(update_string);
	}

	public void closeConnection() {
		try {
			if(c != null) {
				c.close();
				c = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
