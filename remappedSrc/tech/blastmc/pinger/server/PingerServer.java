package tech.blastmc.pinger.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import tech.blastmc.pinger.server.network.ServerChannel;

@Environment(EnvType.SERVER)
public class PingerServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ServerChannel.init();
    }
}
