
package aura_game.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;



public class App {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Aura";
        config.width = 1280;//Largeur _ before 1200 / 600
        config.height = 720;//hauteur |
        new LwjglApplication(new AuraGDX(), config);
        
    }
}
