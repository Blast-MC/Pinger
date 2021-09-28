package tech.blastmc.serverspigot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerSpigot extends JavaPlugin {

	private static ServerSpigot instance;

	public static ServerSpigot getInstance() {
		return ServerSpigot.instance;
	}

	@Override
	public void onEnable() {
		ServerSpigot.instance = this;
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "pinger:out", new PingListener());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "pinger:in");
	}

	@Override
	public void onDisable() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(this);
	}
}
