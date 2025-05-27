package game3d;

import java.util.ArrayList;
import java.awt.Color;

public class Screen {
    public ArrayList<int[][]> chunks; // Holds the chunks
    public int width, height;
    public ArrayList<Texture> textures;
    public int chunkSize; // Size of each chunk

    public Screen(ArrayList<int[][]> chunks, int w, int h, ArrayList<Texture> tex) {
        this.chunks = chunks;
        this.width = w;
        this.height = h;
        this.textures = tex;
        this.chunkSize = 15; // Assuming each chunk is 15x15
    }

    public int[] update(Camera camera, int[] pixels) {
        // Clear screen (fill half with dark gray and half with gray for effects)
        for (int n = 0; n < pixels.length / 2; n++) {
            if (pixels[n] != Color.DARK_GRAY.getRGB()) pixels[n] = Color.DARK_GRAY.getRGB();
        }
        for (int i = pixels.length / 2; i < pixels.length; i++) {
            if (pixels[i] != Color.gray.getRGB()) pixels[i] = Color.gray.getRGB();
        }

        // Loop over all columns (x-axis) of the screen
        for (int x = 0; x < width; x++) {
            double cameraX = 2 * x / (double)(width) - 1;
            double rayDirX = camera.xDir + camera.xPlane * cameraX;
            double rayDirY = camera.yDir + camera.yPlane * cameraX;

            // Camera position in terms of chunks
            int mapX = (int) camera.xPos;
            int mapY = (int) camera.yPos;

            // Calculate distances for raycasting
            double sideDistX, sideDistY;
            double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
            double perpWallDist;
            int stepX, stepY;
            boolean hit = false;
            int side = 0;

            // Direction to step in each axis
            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (camera.xPos - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
            }

            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (camera.yPos - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
            }

            // Raycasting loop
            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }

                // Check if the ray hits a wall (non-zero value)
                if (chunks.get(mapX / chunkSize)[mapX % chunkSize][mapY % chunkSize] > 0) {
                    hit = true;
                }
            }

            // Calculate perpendicular wall distance
            if (side == 0)
                perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX);
            else
                perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY);

            // Calculate the height of the line to draw
            int lineHeight = (int) Math.abs(height / perpWallDist);
            int drawStart = -lineHeight / 2 + height / 2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + height / 2;
            if (drawEnd >= height) drawEnd = height - 1;

            // Select the correct texture
            int texNum = chunks.get(mapX / chunkSize)[mapX % chunkSize][mapY % chunkSize] - 1;
            double wallX;
            if (side == 1) {
                wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX);
            } else {
                wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
            }
            wallX -= Math.floor(wallX);

            // Calculate texture coordinates
            int texX = (int) (wallX * (textures.get(texNum).SIZE));
            if (side == 0 && rayDirX > 0) texX = textures.get(texNum).SIZE - texX - 1;
            if (side == 1 && rayDirY < 0) texX = textures.get(texNum).SIZE - texX - 1;

            // Draw the vertical line for the wall
            for (int y = drawStart; y < drawEnd; y++) {
                int texY = (((y * 2 - height + lineHeight) << 6) / lineHeight) / 2;
                int color;
                if (side == 0)
                    color = textures.get(texNum).pixles[texX + (texY * textures.get(texNum).SIZE)];
                else
                    color = (textures.get(texNum).pixles[texX + (texY * textures.get(texNum).SIZE)] >> 1) & 8355711;
                pixels[x + y * (width)] = color;
            }
        }
        return pixels;
    }
}

