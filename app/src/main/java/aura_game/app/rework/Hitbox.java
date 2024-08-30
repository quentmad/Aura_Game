package aura_game.app.rework;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe qui gère les hitbox des entités
 * Il y a une hitbox approximative (rectangle) pour les premieres vérifications de collision
 * et une hitbox précise (polygon) pour les vérifications de collision plus précises
 */
public class Hitbox {

    public final int width;
    public final int height;

    /**Dernière version des index dans lequel l'obj est présent dans la grille / grille de superposition s'il y en a plusieurs*/
    private List<Pair<Integer, Integer>> listIndexGrid;
    public Rectangle approximativeHitbox;
    public Polygon preciseHitbox;
    /** Tableau de sommets du polygone avec des points intermédiaires pour une détection de collision plus précise
     * Correspondant à la position x:0, y:0, avec une intervale de 10*/
    private final float[] verticesWithIntermediate;





    public Hitbox(Rectangle approximativeHitbox, Polygon preciseHitbox) {
        this.width = (int) approximativeHitbox.width;
        this.height = (int) approximativeHitbox.height;
        this.approximativeHitbox = approximativeHitbox;
        this.preciseHitbox = preciseHitbox;
        this.verticesWithIntermediate = generatePointsWithIntermediate(preciseHitbox.getTransformedVertices(), 8);

    }

    /**
     * Met à jour la position de la hitbox (approximative et précise)
     * @param depX déplacement en x.
     * @param depY déplacement en y.
     */
    public void update(int depX, int depY){
        approximativeHitbox.setPosition(approximativeHitbox.getX() + depX, approximativeHitbox.getY() + depY);
        preciseHitbox.translate(depX, depY);

    }

    /**
     * Génère des points intermédiaires entre les sommets du polygone (par rapport à 0).
     * @param verticesTransformed les sommets du polygone.
     * @param interval interval (environ) souhaité entre les points intermédiaires.
     * @return un tableau de points intermédiaires (contenant les originaux).
     */
    private float[] generatePointsWithIntermediate(float[] verticesTransformed, int interval) {
        if (interval >= Math.max(width, height)) {
            return verticesTransformed;
        }

        if (verticesTransformed.length % 2 != 0) {
            throw new IllegalArgumentException("Le tableau de sommets doit avoir un nombre pair de coordonnées (x, y).");
        }

        List<Float> intermediatePointsList = new ArrayList<>();
        int numVertices = verticesTransformed.length / 2;

        for (int i = 0; i < numVertices - 1; i++) {
            float x1 = verticesTransformed[i * 2];
            float y1 = verticesTransformed[i * 2 + 1];
            float x2 = verticesTransformed[(i + 1) * 2];
            float y2 = verticesTransformed[(i + 1) * 2 + 1];

            intermediatePointsList.add(x1);
            intermediatePointsList.add(y1);

            float dx = x2 - x1;
            float dy = y2 - y1;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            int numPointsInter = (int) (distance / interval);

            for (int j = 1; j <= numPointsInter; j++) {
                float t = j / (float) (numPointsInter + 1);
                float x = x1 + t * dx;
                float y = y1 + t * dy;
                intermediatePointsList.add(x);
                intermediatePointsList.add(y);
            }
        }

        float xLast = verticesTransformed[(numVertices - 1) * 2];
        float yLast = verticesTransformed[(numVertices - 1) * 2 + 1];
        intermediatePointsList.add(xLast);
        intermediatePointsList.add(yLast);

        float[] intermediatePoints = new float[intermediatePointsList.size()];
        for (int i = 0; i < intermediatePointsList.size(); i++) {
            intermediatePoints[i] = intermediatePointsList.get(i);
        }

        return intermediatePoints;
    }


    /**
     * Donne le tableau de points intermédiaires translate par rapport à x et y
     * @param x déplacement en x.
     * @param y déplacement en y.
     */
    public float[] translateIntermediatePoints(float x, float y) {

        float[] verticesWithIntermediateUpdated = Arrays.copyOf(verticesWithIntermediate, verticesWithIntermediate.length);
        for (int i = 0; i < verticesWithIntermediateUpdated.length; i += 2) {
            verticesWithIntermediateUpdated[i] += x;
            verticesWithIntermediateUpdated[i + 1] += y;
        }
        return verticesWithIntermediateUpdated;
    }


    //getters and setters
    public Rectangle approximativeHitbox() {
        return approximativeHitbox;
    }

    public Polygon preciseHitbox() {
        return preciseHitbox;
    }

    /**
     * @return La liste des index dans laquelle l'objet est présent dans la grille au dernier update
     */
    public List<Pair<Integer, Integer>> listIndexGrid() {
        return listIndexGrid;
    }

    public void setListIndexGrid(List<Pair<Integer, Integer>> listIndexGrid) {
        this.listIndexGrid = listIndexGrid;
    }




}
