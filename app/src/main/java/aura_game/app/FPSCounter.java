package aura_game.app;

import com.badlogic.gdx.utils.TimeUtils;

public class FPSCounter {

    private int frames;
    private long startTime;

    public FPSCounter() {
        frames = 0;
        startTime = TimeUtils.nanoTime();
    }

    public void update() {
        frames++;
        if (TimeUtils.nanoTime() - startTime >= 1000000000) {
            System.out.println("FPS: " + frames);
            frames = 0;
            startTime = TimeUtils.nanoTime();
        }
    }
}
