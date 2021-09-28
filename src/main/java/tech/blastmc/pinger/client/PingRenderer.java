package tech.blastmc.pinger.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import tech.blastmc.pinger.client.models.Ping;
import tech.blastmc.pinger.client.utils.ClientConfig;
import tech.blastmc.pinger.client.utils.TextToGraphicConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PingRenderer {

	public static final PingRenderer INSTANCE = new PingRenderer();
	private final Map<Integer, Identifier> distTextMap = new HashMap<>();

	public static void renderActivePings(MatrixStack matrixStack, Camera camera, float tickDelta) {
		if (MinecraftClient.getInstance() == null) return;
		// if (!ClientSettings.showWaypoints || !ClientSettings.waypointsWorldRender) return;

		BlockPos playerPos = MinecraftClient.getInstance().player.getBlockPos();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.enableTexture();
		RenderSystem.depthMask(false);
		MinecraftClient.getInstance().getTextureManager().bindTexture(PingerClient.TEXTURE);
		RenderSystem.setShaderTexture(0, PingerClient.TEXTURE);

		VertexConsumerProvider.Immediate consumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		Set<Ping> wayPoints = PingManager.getAllPingsInRange();
		for (Ping ping : new ArrayList<>(wayPoints)) {
			int dist = (int) PingRenderer.INSTANCE.getDistance(ping.getBlockPos(), playerPos, false);
			if (dist <= ClientConfig.getPingDistance())
				PingRenderer.INSTANCE.renderPing(matrixStack, consumerProvider, ping, camera, dist);
		}
		consumerProvider.draw();

		RenderSystem.depthMask(true);
	}

	private void renderPing(MatrixStack matrixStack, VertexConsumerProvider consumerProvider, Ping ping, Camera camera, int dist) {
		int wpX = ping.getBlockPos().getX();
		int wpY = ping.getBlockPos().getY();
		int wpZ = ping.getBlockPos().getZ();

		Vec3d vec3d = camera.getPos();

		double camX = vec3d.getX();
		double camY = vec3d.getY();
		double camZ = vec3d.getZ();

		matrixStack.push();
		matrixStack.translate((double) wpX - camX, (double) wpY - camY, (double) wpZ - camZ);
		matrixStack.translate(0.5, 0.5, 0.5);

		matrixStack.push();
		matrixStack.translate(0.0, 1.0, 0.0);

		matrixStack.multiply(camera.getRotation());
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
		matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));

		Identifier texture = PingerClient.TEXTURE;
		VertexConsumer vertexConsumer = consumerProvider.getBuffer(RenderLayer.getBeaconBeam(texture, true));
		this.renderIcon(matrixStack, vertexConsumer, dist);
		matrixStack.pop();

		matrixStack.push();
		matrixStack.translate((double) wpX - camX, (double) wpY - camY, (double) wpZ - camZ);
		matrixStack.translate(0.5, 0.5, 0.5);

		matrixStack.push();
		matrixStack.translate(0.0, ((dist / (double) ClientConfig.getPingDistance()) * 2) + 2.5, 0.0);

		matrixStack.multiply(camera.getRotation());
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
		matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));

		Identifier text;
		if (this.distTextMap.containsKey(dist))
			text = this.distTextMap.get(dist);
		else {
			NativeImage image = TextToGraphicConverter.convert(dist + "m");
			NativeImageBackedTexture backedTexture = new NativeImageBackedTexture(image);
			text = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("png", backedTexture);
			this.distTextMap.put(dist, text);
		}
		VertexConsumer vertexConsumer2 = consumerProvider.getBuffer(RenderLayer.getTextIntensitySeeThrough(text));
		this.renderIcon(matrixStack, vertexConsumer2, dist * 3);
		matrixStack.pop();

		matrixStack.pop();
		matrixStack.pop();
	}

	private void renderIcon(MatrixStack matrixStack, VertexConsumer vertexConsumer, int dist) {
		float sizeDelta = (float) (dist / (double) ClientConfig.getPingDistance()) * 1.5F + .1f;

		MatrixStack.Entry entry = matrixStack.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = entry.getNormal();

		this.addVertex(matrix4f, matrix3f, vertexConsumer, -sizeDelta, -sizeDelta, 0.0F, 0.0F);
		this.addVertex(matrix4f, matrix3f, vertexConsumer, -sizeDelta, sizeDelta, 0.0F, 1f);
		this.addVertex(matrix4f, matrix3f, vertexConsumer, sizeDelta, sizeDelta, 1f, 1f);
		this.addVertex(matrix4f, matrix3f, vertexConsumer, sizeDelta, -sizeDelta, 1f, 0.0F);
	}

	private void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float y, float x, float m, float n) {
		vertexConsumer.vertex(matrix4f, x, y, (float) 0.0).color(255, 255, 255, 255).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(0xFFF000F0).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
	}

	public double getDistance(BlockPos a, BlockPos b, boolean horizontalOnly) {
		int dist;
		int distX = (a.getX() - b.getX());
		int distZ = (a.getZ() - b.getZ());

		dist = distX * distX + distZ * distZ;
		if (!horizontalOnly) {
			int distY = (a.getY() - b.getY());
			dist += distY * distY;
		}

		return Math.sqrt(dist);
	}

}
