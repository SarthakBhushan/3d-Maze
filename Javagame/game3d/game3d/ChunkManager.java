package game3d;

import java.util.ArrayList;

public class ChunkManager {
    private int chunkSize;
    private MazeGenerator mazeGenerator;
    private int chunkRadius;

    public ChunkManager(int chunkSize, int chunkRadius) {
        this.chunkSize = chunkSize;
        this.chunkRadius = chunkRadius;
        mazeGenerator = new MazeGenerator(chunkSize, chunkSize);
    }

    public ArrayList<int[][]> generateChunks(int playerX, int playerY) {
        ArrayList<int[][]> chunks = new ArrayList<>();

        for (int dx = -chunkRadius; dx <= chunkRadius; dx++) {
            for (int dy = -chunkRadius; dy <= chunkRadius; dy++) {
                int chunkX = playerX + dx * chunkSize;
                int chunkY = playerY + dy * chunkSize;
                mazeGenerator.generateMaze(chunkX, chunkY);
                chunks.add(mazeGenerator.getMaze());
            }
        }

        return chunks;
    }
}
