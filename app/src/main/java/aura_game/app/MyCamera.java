package aura_game.app;

public class MyCamera {
    private int x;
    private int y;
    
    public MyCamera() {
        this.x = 0;
        this.y = 0;
    }  
    
    public void updatePosition(int dx, int dy, int mapWidth, int mapHeight, int screenWidth, int screenHeight) {
        int cameraMaxX = mapWidth - screenWidth;
        int cameraMaxY = mapHeight - screenHeight;
        
        if (getX() + dx < 0) {
            dx = - getX();
        } else if (getX() + dx > cameraMaxX) {
            dx = cameraMaxX - getX();
        }

        if (getY() + dy < 0) {
            dy = - getY();
        } else if (getY() + dy > cameraMaxY) {
            dy = cameraMaxY - getY();
        }
        
        addX(dx);
        addY(dy);
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addX(int dx) {
        x += dx;
    }

    public void addY(int dy) {
        y += dy;
    }
}
