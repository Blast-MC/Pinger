package tech.blastmc.pinger.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import tech.blastmc.pinger.client.PingManager;
import tech.blastmc.pinger.client.models.Ping;

public class ServerChannel {

	private static Identifier CHANNEL_IN;
	private static Identifier CHANNEL_OUT;

	public static void init() {
		ServerChannel.CHANNEL_IN = new Identifier("pinger", "in");
		ServerChannel.CHANNEL_OUT = new Identifier("pinger", "out");

		ClientPlayNetworking.registerGlobalReceiver(ServerChannel.CHANNEL_IN, new ServerChannelReceiver());
	}

	public static void send(Ping ping) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeBytes(ping.toBytes());

		ClientPlayNetworking.send(ServerChannel.CHANNEL_OUT, packetByteBuf);
	}

	public static class ServerChannelReceiver implements ClientPlayNetworking.PlayChannelHandler {

		@Override
		public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			try {
				PingManager.receivePing(Ping.fromByteArray(buf.getWrittenBytes()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
