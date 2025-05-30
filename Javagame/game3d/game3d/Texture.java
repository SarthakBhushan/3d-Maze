package game3d;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	public int[] pixles;
	private String loc;
	public final int SIZE;
	
	public Texture(String location, int size) {
		loc = location;
		SIZE = size;
		pixles = new int[SIZE *size];
		load();
	}
	

	private void load() {
		try {
			BufferedImage image = ImageIO.read(new File(loc));
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixles, 0, w);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public static Texture wood = new Texture("resources/pictures/wood.png", 64);
	public static Texture brick = new Texture("resources/pictures/brick.png", 64);
	public static Texture bluestone = new Texture("resources/pictures/bluestone.png", 64);
	public static Texture stone = new Texture("resources/pictures/stone.png", 64);


}
