
package aura_game.app.Weather;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

/**
 * La classe Sky représente le ciel du jeu. Elle gère l'affichage du ciel qui change de couleur au fil du temps.
 */
public class Sky {
    //private SpriteBatch spriteBatch;
    //private Region actualRegion;
    private Texture fullTexture;
    private Color startColor;
    private Color endColor;
    Pixmap pixmap;

    /**
     * Constructeur de la classe Sky.
     */
    public Sky() {
        //this.spriteBatch = new SpriteBatch();
        pixmap = new Pixmap(1200, 600, Pixmap.Format.RGBA8888);

        this.fullTexture = new Texture(1200, 600, Pixmap.Format.RGBA8888);
        this.startColor = new Color(1, 1, 1, 0.1f); // Couleur de départ (blanc)
        this.endColor = new Color(38 / 255f, 101 / 255f, 189 / 255f, 0.5f); // Couleur de fin (bleu foncé)
        //this.endColor = new Color(0, 0, 0, 0.5f); // Couleur de fin (noir)

    }

    /**
     * Affiche le ciel en mettant à jour les couleurs en fonction du temps écoulé.
     *
     * @param batch 
     * @param deltaTime Temps écoulé depuis la dernière frame.
     */
    public void render(SpriteBatch batch,float deltaTime) {
        //for (int i = 0; i < 3; i++) {
            if (startColor.r > endColor.r) {
                startColor.r -= deltaTime;
            }
            if (startColor.g > endColor.g) {
                startColor.g -= deltaTime;
            }
            if (startColor.b > endColor.b) {
                startColor.b -= deltaTime;
            }
            if (startColor.a < endColor.a) {
                startColor.a += deltaTime;
            }
        //}
/* 
        pixmap.setColor(startColor);
        pixmap.fill();
        fullTexture.draw(pixmap, 0, 0);
        batch.draw(fullTexture, 0, 0);*/
    }
}
