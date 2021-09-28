package tech.blastmc.pinger.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import tech.blastmc.pinger.client.models.Ping;
import tech.blastmc.pinger.client.network.ServerChannel;
import tech.blastmc.pinger.client.utils.ClientConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class PingManager {

	private static Set<Ping> pings = new HashSet<>();

	public static void newPing(Ping ping) {
		if (PingManager.receivePing(ping))
			ServerChannel.send(ping);
	}

	public static boolean receivePing(Ping ping) {
		if (ping == null) return false;
		if (PingManager.pings.contains(ping)) return false;
		PingManager.pings.add(ping);
		MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.MASTER, 1f, 1f);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				PingManager.pings.remove(ping);
			}
		}, ClientConfig.getPingSeconds() * 1000L);
		return true;
	}

	public static Set<Ping> getAllPings() {
		return PingManager.pings;
	}

	public static Set<Ping> getAllPingsInRange() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		return PingManager.pings
				       .stream()
				       .filter(ping -> PingRenderer.INSTANCE.getDistance(ping.getBlockPos(), player.getBlockPos(), false) < ClientConfig.getPingDistance())
				       .collect(Collectors.toSet());
	}

}
