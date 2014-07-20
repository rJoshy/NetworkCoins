package me.josh.networkcoins.commands;

import me.josh.networkcoins.API;
import me.josh.networkcoins.NetworkCoins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {
	
	static FileConfiguration config = NetworkCoins.getInstance().getConfig();
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		Player p = (Player)sender;
		    if (cmd.getName().equalsIgnoreCase("coins")) {
		    	if (args.length < 1) {
		    		p.sendMessage(config.getString("getCoinsMessage").replace("%prefix", config.getString("Prefix")).replace("%count", API.getCoinsString(p)).replace("%currency", config.getString("currency")).replace("&", "¤"));
		    	}
		    }
		    if (args.length == 1) {
		    	if (args[0].equalsIgnoreCase("help")) {
		    		if (p.isOp() || p.hasPermission("networkcoins.admin")) {
		    			p.sendMessage(ChatColor.GRAY + "- = - = - = - = - = - = - " + ChatColor.RED + "NetworkCoins Help" + ChatColor.GRAY + " - = - = - = - = - = - = -");
		    			p.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins" + ChatColor.GRAY + " - " + ChatColor.GREEN + "View your current %c count".replace("%c", config.getString("currency")));
		    			p.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins help" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Show the NetworkCoins help menu");
		    			p.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins give (player) (amount)" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Give a player an amount of " + config.getString("currency"));
		    			p.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins take (player) (amount)" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Take a amount of " + config.getString("currency") + " from a player");
		    			p.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins reset (player)" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Reset a players " + config.getString("currency"));
		    			p.sendMessage(ChatColor.GRAY + " - " + ChatColor.RED + "/coins user (player)" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Get a players amount of " + config.getString("currency"));
		    		} else {
		    			p.sendMessage(config.getString("cannotUseHelp").replace("%prefix", config.getString("Prefix")).replace("&", "¤"));
		    		}
		    	}
		    }
		    if (args.length == 2) {
		    	if (!p.hasPermission("networkcoins.admin")) {
		    		p.sendMessage(config.getString("noPermission").replace("%prefix", config.getString("Prefix")).replace("&", "¤"));
		    	} else {
		    		if (args[0].equalsIgnoreCase("reset")) {
		    			@SuppressWarnings("deprecation")
						Player target = Bukkit.getServer().getPlayer(args[1]);
		    			
		    			API.resetCoins(target);
		    			p.sendMessage(config.getString("resetCoinsGiver").replace("%prefix", config.getString("Prefix")).replace("%target", target.getName()).replace("%currency", config.getString("currency")).replace("&", "¤"));
		    			target.sendMessage(config.getString("resetCoinsReceiver").replace("%prefix", config.getString("Prefix")).replace("%currency", config.getString("currency")).replace("&", "¤"));
		    		}
		    	}
		    }
		    if (args.length == 2) {
		    	if (!p.hasPermission("networkcoins.admin")) {
		    		p.sendMessage(config.getString("noPermission").replace("%prefix", config.getString("Prefix")).replace("&", "¤"));
		    	} else {
		    		if (args[0].equalsIgnoreCase("user")) {
		    			@SuppressWarnings("deprecation")
						Player target = Bukkit.getServer().getPlayer(args[1]);
		    		
		    		    p.sendMessage(config.getString("getSomeonesCoinsMessage").replace("%prefix", config.getString("Prefix")).replace("%target", target.getName()).replace("%count", API.getCoinsString(p)).replace("%currency", config.getString("currency")).replace("&", "¤"));
		    		}
		    	}
		    }
		    if (args.length == 3) {
		    	if (!p.hasPermission("networkcoins.admin")) {
		    		p.sendMessage(config.getString("noPermission").replace("%prefix", config.getString("Prefix")).replace("&", "¤"));
		    	} else {
		    		if (args[0].equalsIgnoreCase("give")) {
			    		@SuppressWarnings("deprecation")
						Player target = Bukkit.getServer().getPlayer(args[1]);
			    		int coins = Integer.parseInt(args[2]);
			    		Integer coins_int = coins;
			    		String coins_string = coins_int.toString();
			    		
			    		API.addCoins(target, coins);
			    		p.sendMessage(config.getString("addCoinsGiver").replace("%prefix", config.getString("Prefix")).replace("%coins", coins_string).replace("%currency", config.getString("currency")).replace("%target", target.getName()).replace("&", "¤"));
			    		target.sendMessage(config.getString("addCoinsReceiver").replace("%prefix", config.getString("Prefix")).replace("%coins", coins_string).replace("%currency", config.getString("currency")).replace("&", "¤"));
			    	}
		    	}
		    }
		    if (args.length == 3) {
		    	if (!p.hasPermission("networkcoins.admin")) {
		    		p.sendMessage(config.getString("noPermission").replace("%prefix", config.getString("Prefix")).replace("&", "¤"));
		    	} else {
		    		if (args[0].equalsIgnoreCase("take")) {
			    		@SuppressWarnings("deprecation")
						Player target = Bukkit.getServer().getPlayer(args[1]);
			    		int coins = Integer.parseInt(args[2]);
			    		Integer coins_int = coins;
			    		String coins_string = coins_int.toString();
			    		
			    		API.takeCoins(p, target, coins);
			    		p.sendMessage(config.getString("removeCoinsGiver").replace("%prefix", config.getString("Prefix")).replace("%coins", coins_string).replace("%currency", config.getString("currency")).replace("%target", target.getName()).replace("&", "¤"));
			    		target.sendMessage(config.getString("removeCoinsReceiver").replace("%prefix", config.getString("Prefix")).replace("%coins", coins_string).replace("%currency", config.getString("currency")).replace("&", "¤"));
			    	}
		    	}
		    }
		    if (args.length > 3) {
		    	if (!p.hasPermission("networkcoins.admin")) {
		    		p.sendMessage(config.getString("noPermission").replace("%prefix", config.getString("Prefix")).replace("&", "¤"));
		    	} else {
		    		p.sendMessage(config.getString("incorrectUsage").replace("%prefix", config.getString("Prefix")).replace("&", "¤"));
		    	}
		    }
		return false;
	}

}
