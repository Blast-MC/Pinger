package tech.blastmc.pinger.client.models;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import tech.blastmc.pinger.client.ping.BlockPosPing;
import tech.blastmc.pinger.client.ping.EntityPing;

import java.nio.ByteBuffer;

public abstract class Ping {

	public abstract BlockPos getBlockPos();

	public static Ping fromByteArray(byte[] bytes) {
		byte[] bytesWithoutType = new byte[bytes.length - 1];
		System.arraycopy(bytes, 1, bytesWithoutType, 0, bytes.length - 1);
		switch (bytes[0]) {
			case 1 -> {
				String string = new String(bytesWithoutType);
				String[] split = string.split(",");
				int x = Integer.parseInt(split[0]);
				int y = Integer.parseInt(split[1]);
				int z = Integer.parseInt(split[2]);
				return new BlockPosPing(new BlockPos(x, y, z));
			}
			case 2 -> {
				int id = ByteBuffer.wrap(bytesWithoutType).getInt();
				Entity entity = MinecraftClient.getInstance().player.world.getEntityById(id);
				if (entity != null)
					return new EntityPing(entity);
			}
		}
		return null;
	}

	public byte[] toBytes() {
		byte type = 0;
		if (this instanceof BlockPosPing) type = 1;
		if (this instanceof EntityPing) type = 2;
		byte[] bytes = this.toByteArray();
		byte[] bytesWithType = new byte[bytes.length + 1];
		bytesWithType[0] = type;
		System.arraycopy(bytes, 0, bytesWithType, 1, bytes.length);
		return bytesWithType;
	}

	protected abstract byte[] toByteArray();



}
