package tech.blastmc.pinger.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;
import tech.blastmc.pinger.client.network.ServerChannel;
import tech.blastmc.pinger.client.ping.BlockPosPing;
import tech.blastmc.pinger.client.ping.EntityPing;
import tech.blastmc.pinger.client.utils.RaycastHelper;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class PingerClient implements ClientModInitializer {

	public static final Identifier TEXTURE = new Identifier("pinger","textures/ping.png");
	private static KeyBinding pingKey;

	@Override
	public void onInitializeClient() {
		PingerClient.pingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.pinger.ping",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_Z,
				"category.pinger.main"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (PingerClient.pingKey.wasPressed()) {
				this.ping();
			}
		});
		ServerChannel.init();
	}

	public void ping() {
		HitResult hit = RaycastHelper.getCurrentHitResult();
		if (hit == null || hit.getType() == null)
			return;
		switch (hit.getType()) {
			case BLOCK -> {
				BlockHitResult blockHitResult = (BlockHitResult) hit;
				BlockPos pos = blockHitResult.getBlockPos();
				if (blockHitResult.getSide() != Direction.UP)
					pos = pos.add(blockHitResult.getSide().getVector());
				PingManager.newPing(new BlockPosPing(pos));
			}
			case ENTITY -> {
				EntityHitResult entityHitResult = (EntityHitResult) hit;
				PingManager.newPing(new EntityPing(entityHitResult.getEntity()));
			}
		}
	}

}
