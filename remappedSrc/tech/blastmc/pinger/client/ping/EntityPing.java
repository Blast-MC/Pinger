package tech.blastmc.pinger.client.ping;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import tech.blastmc.pinger.client.models.Ping;

import java.nio.ByteBuffer;
import java.util.Objects;

public class EntityPing extends Ping {

	Entity entity;

	public EntityPing(Entity entity) {
		this.entity = entity;
	}

	@Override
	public BlockPos getBlockPos() {
		return this.entity.getBlockPos().add(0, 1, 0);
	}

	@Override
	protected byte[] toByteArray() {
		return ByteBuffer.allocate(4).putInt(this.entity.getId()).array();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EntityPing ping2)
			return Objects.equals(this.entity, ping2.entity);
		return false;
	}

}
