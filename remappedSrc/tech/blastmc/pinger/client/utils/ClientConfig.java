package tech.blastmc.pinger.client.utils;

public class ClientConfig {

	private static int pingDistance = 100;
	private static int pingSeconds = 10;

	public static int getPingDistance() {
		return ClientConfig.pingDistance;
	}

	public static int getPingSeconds() {
		return ClientConfig.pingSeconds;
	}
}
