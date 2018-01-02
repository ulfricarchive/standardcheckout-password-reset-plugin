package com.standardcheckout.plugin.passwords;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.server.ServerListPingEvent;

public class JoinListener implements Listener {

	private StandardCheckoutClient client = new StandardCheckoutClient();

	@EventHandler
	public void on(AsyncPlayerPreLoginEvent event) {
		String code = client.reset(event.getUniqueId());
		if (code != null) {
			event.disallow(Result.KICK_OTHER,
					ChatColor.GREEN + "" + ChatColor.BOLD + "StandardCheckout\n\n" + ChatColor.GOLD
							+ "Your one-time passcode is " + ChatColor.RED + code + "\n\n" + ChatColor.GOLD
							+ ChatColor.ITALIC + "Do not share your passcode with others!");
		} else {
			event.disallow(Result.KICK_OTHER, ChatColor.GREEN + "" + ChatColor.BOLD + "StandardCheckout\n\n"
					+ ChatColor.RED + "We couldn't reset your password. Try again in a few minutes.");
		}
	}

	@EventHandler
	public void on(ServerListPingEvent event) {
		event.setMaxPlayers(1);
		event.setMotd(ChatColor.GREEN + "StandardCheckout\n" + ChatColor.GRAY + "Password resetting & verification server");
	}

}
