package tech.blastmc.pinger.client.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.blastmc.pinger.client.PingRenderer;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

	@Inject(method = "render", at = @At(value = "RETURN", ordinal = 0))
	public void renderBeam(MatrixStack matrixStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		if (!MinecraftClient.isFabulousGraphicsOrBetter()) PingRenderer.renderActivePings(matrixStack, camera, f);
	}

}
