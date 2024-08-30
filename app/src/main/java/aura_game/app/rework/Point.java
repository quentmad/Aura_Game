package aura_game.app.rework;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addToX(int dx) {
        this.x += dx;
    }

    public void addToY(int dy) {
        this.y += dy;
    }

    public boolean equals(Point pos) {
        return this.x == pos.x() && this.y == pos.y();
    }

    public void add (Point pos) {
        this.x += pos.x();
        this.y += pos.y();
    }

    public void mult (int factor) {
        this.x *= factor;
        this.y *= factor;
    }
}

