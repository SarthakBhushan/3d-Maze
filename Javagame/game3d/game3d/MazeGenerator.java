package game3d;

public class MazeGenerator {
    private int[][] maze;
    private int width, height;

    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        maze = new int[width][height];
    }

    public void generateMaze(int startX, int startY) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                maze[x][y] = 1;  // Initially set all cells as walls
            }
        }
        carvePath(startX, startY);  // Start carving from a random point
    }

    private void carvePath(int x, int y) {
        int[] dirs = new int[]{0, 1, 2, 3}; // Directions: 0=up, 1=right, 2=down, 3=left
        shuffleArray(dirs);

        for (int dir : dirs) {
            int nx = x;
            int ny = y;
            
            switch (dir) {
                case 0: ny -= 2; break; // up
                case 1: nx += 2; break; // right
                case 2: ny += 2; break; // down
                case 3: nx -= 2; break; // left
            }
            
            if (isValid(nx, ny)) {
                maze[nx][ny] = 0;  // Carve the path
                maze[(x + nx) / 2][(y + ny) / 2] = 0;  // Carve the wall between cells
                carvePath(nx, ny);  // Recurse to the next cell
            }
        }
    }

    private boolean isValid(int x, int y) {
        return x > 0 && y > 0 && x < width && y < height && maze[x][y] == 1;
    }

    private void shuffleArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int j = (int) (Math.random() * array.length);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public int[][] getMaze() {
        return maze;
    }
}

