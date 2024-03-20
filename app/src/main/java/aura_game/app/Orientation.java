package aura_game.app;

public enum Orientation{
    NORTH(0,1,"U"),
    SOUTH(0,-1,"D"),
    EAST(1,0,"R"),
    WEST(-1,0,"L");
    private final int x;//-1, 0, 1
    private final int y;//-1, 0, 1
    private final String direction;
    Orientation(int x, int y, String direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getDirection() {
        return direction;
    }
}
