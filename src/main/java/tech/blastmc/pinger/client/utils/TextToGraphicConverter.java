package tech.blastmc.pinger.client.utils;

import net.minecraft.client.texture.NativeImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TextToGraphicConverter {

	private int width;
	private int height;

	public static NativeImage convert(String text) {
		TextToGraphicConverter converter = new TextToGraphicConverter();
		BufferedImage image = converter.convertTextToGraphic(text, new Font("Arial", Font.PLAIN, 40));
		return loadImage(converter.toFile(image), converter.width, converter.height);
	}

	public static NativeImage loadImage(InputStream image, int w, int h) {
		try  {
			return NativeImage.read(image);
		} catch (IOException ignore) { }
		return new NativeImage(w, h, false);
	}

	private InputStream toFile(BufferedImage bufferedImage) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (Exception ignore) {}
		return null;
	}

	public BufferedImage convertTextToGraphic(String text, Font font) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();

		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		this.width = fm.stringWidth(text);
		this.height = fm.getHeight();
		g2d.dispose();

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2d.setFont(font);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fill3DRect(0, 0, width + 1, height + 1, false);
		fm = g2d.getFontMetrics();
		g2d.setColor(Color.WHITE);
		g2d.drawString(text, 0, fm.getAscent());
		g2d.dispose();
		return img;
	}
}
