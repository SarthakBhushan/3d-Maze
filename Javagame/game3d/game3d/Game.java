package game3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable {
    
    private static final long serialVersionUID = 1L;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    public int[] pixels;
    public ArrayList<Texture> textures;
    public Camera camera;
    public Screen screen;
    private ChunkManager chunkManager;
    private ArrayList<int[][]> chunks;  // List to hold the generated chunks

    public Game() {
        thread = new Thread(this);
        image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        textures = new ArrayList<Texture>();
        textures.add(Texture.wood);
        textures.add(Texture.brick);
        textures.add(Texture.bluestone);
        textures.add(Texture.stone);
        
        camera = new Camera(4.5, 4.5, 1, 0, 0, -0.66);
        chunkManager = new ChunkManager(15, 3);  // Set the chunk size and radius for chunk generation
        chunks = new ArrayList<>();
        
        screen = new Screen(chunks, 640, 480, textures);  // Pass the chunks to the screen for rendering
        addKeyListener(camera);
        
        setSize(640, 480);
        setResizable(false);
        setTitle("3D Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
        setLocationRelativeTo(null);
        setVisible(true);
        
        start();
    }

    private synchronized void start() {
        running = true;
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        bs.show();
    }

    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        requestFocus();
        while (running) {
            long now = System.nanoTime();
            delta = delta + ((now - lastTime) / ns);
            lastTime = now;
            while (delta >= 1) {
                // Update the chunks based on the camera's position
                updateChunks();

                // Update the camera and screen
                screen.update(camera, pixels);
                camera.update();
                delta--;
            }
            render();
        }
    }

    private void updateChunks() {
        // Get the player position and generate chunks around it
        chunks = chunkManager.generateChunks((int) camera.xPos, (int) camera.yPos);
    }

    public static void main(String[] args) {
        Game game = new Game();
    }
}

