package tech.blastmc.serverspigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class PingListener implements PluginMessageListener {
	@Override
	public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
		if (!channel.equalsIgnoreCase("pinger:out")) return;
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			if (!player.equals(player1))
				if (player.getWorld().equals(player1.getWorld()))
					player1.sendPluginMessage(ServerSpigot.getInstance(), "pinger:in", message);
		}
	}
}
