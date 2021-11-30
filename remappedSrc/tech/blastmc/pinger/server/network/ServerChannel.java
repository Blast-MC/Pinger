package tech.blastmc.pinger.server.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import tech.blastmc.pinger.client.PingManager;
import tech.blastmc.pinger.client.models.Ping;

public class ServerChannel {

    private static Identifier CHANNEL_IN;
    private static Identifier CHANNEL_OUT;

    public static void init() {
        ServerChannel.CHANNEL_IN = new Identifier("pinger", "in");
        ServerChannel.CHANNEL_OUT = new Identifier("pinger", "out");

        ServerPlayNetworking.registerGlobalReceiver(ServerChannel.CHANNEL_OUT, new ServerChannelReceiver());
    }

    public static class ServerChannelReceiver implements ServerPlayNetworking.PlayChannelHandler {

        @Override
        public void receive(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
            for (ServerPlayerEntity player : minecraftServer.getPlayerManager().getPlayerList())
                if (!serverPlayerEntity.getUuidAsString().equals(player.getUuidAsString()))
                    ServerPlayNetworking.send(player, ServerChannel.CHANNEL_IN, packetByteBuf);
        }
    }

}
