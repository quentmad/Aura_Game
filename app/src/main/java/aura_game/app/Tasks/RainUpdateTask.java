package aura_game.app.Tasks;

import aura_game.app.Weather.RainManager;
import aura_game.app.rework.Point;

public class RainUpdateTask implements Runnable {
    private final RainManager rainManager;
    private final Point playerPos;
    private volatile boolean running = true;

    public RainUpdateTask(RainManager rainManager, Point playerPos) {
        this.rainManager = rainManager;
        this.playerPos = playerPos;
    }

    @Override
    public void run() {
        while (running) {
            try {
                rainManager.waitForUpdate();
                rainManager.update(0.1f, playerPos);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
