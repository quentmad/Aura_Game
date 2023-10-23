package aura_game.app.Util;

/**Méthodes utiles un peu partout */
public class Util {
    private static Util instance;

    private Util(){

    }

    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

    /**
     * Calcule la distance euclidienne entre deux points (x1, y1) et (x2, y2).
     *
     * @param x1 Coordonnée x du premier point.
     * @param y1 Coordonnée y du premier point.
     * @param x2 Coordonnée x du deuxième point.
     * @param y2 Coordonnée y du deuxième point.
     * @return La distance entre les deux points.
     */
    public static float distanceBetween(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
