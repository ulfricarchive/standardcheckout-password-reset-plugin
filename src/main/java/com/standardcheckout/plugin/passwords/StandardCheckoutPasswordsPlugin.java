package com.standardcheckout.plugin.passwords;

import org.bukkit.plugin.java.JavaPlugin;

public class StandardCheckoutPasswordsPlugin extends JavaPlugin {

	public static StandardCheckoutPasswordsPlugin getInstance() {
		return JavaPlugin.getPlugin(StandardCheckoutPasswordsPlugin.class);
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
	}

}
