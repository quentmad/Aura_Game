package aura_game.app.SpriteSheet;

/**
 * Classe permettant de stocker les pair d'action/taille pour remplir ensuite le tableau 2D qui servira à remplir le hashmap
 */
public class Act_Sz {
    private final String action;
    private final int taille;


    public Act_Sz(String a, int t){
        this.action = a;
        this.taille = t;

    }
    /**
     * @return L'action en question (Walk...) SANS LA DIRECTION, car déduit après
     */
    public String action(){
        return action;
    }

    /**
     * @return La taille de la sprite (64,128,192)
     */
    public int taille(){
        return taille;
    }
}
