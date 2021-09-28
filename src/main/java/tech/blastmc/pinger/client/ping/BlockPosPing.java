package tech.blastmc.pinger.client.ping;

import net.minecraft.util.math.BlockPos;
import tech.blastmc.pinger.client.models.Ping;

import java.util.Objects;

public class BlockPosPing extends Ping {

	BlockPos blockPos;

	public BlockPosPing(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	@Override
	public BlockPos getBlockPos() {
		return this.blockPos;
	}

	@Override
	public byte[] toByteArray() {
		int x = this.blockPos.getX();
		int y = this.blockPos.getY();
		int z = this.blockPos.getZ();
		return (x + "," + y + "," + z).getBytes();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockPosPing ping2)
			return Objects.equals(this.blockPos, ping2.blockPos);
		return false;
	}
}
