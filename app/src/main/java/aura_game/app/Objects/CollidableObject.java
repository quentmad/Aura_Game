package aura_game.app.Objects;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import aura_game.app.LootManager;
import aura_game.app.UIBar;
import aura_game.app.Type.LootType;
import aura_game.app.Util.Triplet;


/**BasicObject avec un hitbox de collision, et des loots lors de sa destruction */
public class CollidableObject extends BasicObject {

    private final String name;

    private Rectangle hitboxFlat;//Correspond à une hitbox rectangle simple plat pour le quadtree/grid
    private final int tall;//La taille 0 correspond à un item "plat"
    /*La différence entre le bas de l'image et le 1e pixel de l'item(en y) non ombre. Stock la valeur du itemType*/
    private final int offY;
    /**Hauteur du rectangle hitbox plat (sans vu isometrique...)*/
    private int hitboxHeight; 
    /*Apres verif sur Rectangle, afin d'avoir une vérification plus précise (les entités ont seulement des Rectangle)*/
    private Polygon hitboxPolygon; //Hitbox (flat). Utilisé lorsqu'une colission est détecté 
    private UIBar ui;//Gère les coeurs, barre de vies, -3 / -6 ...
    protected float maxLives;
    protected float life;
    private boolean dead;
    /**Loots qui peuvent etre donné par l'objet lorsqu'il est détruit :
     * Name, Min, Max*/
    private final List<Triplet<String,Integer,Integer>> deathLoots;
    private LootManager lootManager;
    /**Dernière version des index dans lequel l'obj est présent dans la grille / grille de superposition s'il y en a plusieurs*/
    private List<Pair<Integer, Integer>> listIndexGrid;



    public CollidableObject(String name,Rectangle hitboxFlat, int posC_X, int posC_Y,
     int tall, Polygon hitboxPolygon, int hitboxHeight,int offY, List<Triplet<String, Integer, Integer>> deathLoots){
        super(posC_X, posC_Y);
        this.name = name;
        this.ui = new UIBar();
        this.maxLives = 15; //todo: Prendre depuis le type
        this.life = maxLives;
        this.dead = false;
        this.hitboxFlat = hitboxFlat;
        //TODO: depasse pas de region ?? pas de taille hitbox ici ?
        this.tall = tall;
        this.hitboxPolygon = hitboxPolygon;
        this.hitboxHeight = hitboxHeight;
        this.offY = offY;
        this.deathLoots = deathLoots;
        this.lootManager = LootManager.getInstance();


    }


    public String getName(){
        return name;
    }

    public List<Pair<Integer, Integer>> getListIndexGrid() {
        return listIndexGrid;
    }

    public void setListIndexGrid(List<Pair<Integer, Integer>> newGridList) {
        this.listIndexGrid = newGridList;
    }

    /**Correspond à une hitbox rectangle simple plat pour le quadtree/grid, pour les colissions... */
    public Rectangle getHitboxFlat() {
        return hitboxFlat;
    }

    public int getTall(){
        return tall;
    }

    /**Bar de vies, cœurs... */
    public UIBar getUI(){
        return ui;
    }

    /**
     * Retourne la différence entre le bas de l'image et le 1e pixel de l'item(en y). Cette valeur est extraite d'itemType et est constante
     * @return la valeur de différence 
     */
    public int getOffY(){
        return this.offY;
    }

    /**Hauteur du rectangle hitbox plat (sans vu isometrique...)*/
    public int getHitboxHeight(){
        return hitboxHeight;
    }

    public Polygon getHitboxPolygon(){
        return hitboxPolygon;
    }
    /**Ne doit pas être appelé, existe ici pour éviter pb */
    public int getLootSpawnCenterX(int lootWidth){
        System.out.println("getLootSpawnCenterX should not be called directetly in CollidableObject");
        return -1;
    }
     /**Ne doit pas être appelé, existe ici pour éviter pb */
    public int getCenterX(){
        System.out.println("getCenterX should not be called directly in CollidableObject");
        return -1;
    }

    public void setHitboxPolygon(Polygon poly){
        this.hitboxPolygon = poly;
    }
    /**Nombre de points de vie actuel*/
    public float getLife(){
        return life;
    }
    public float getMaxLives(){
        return maxLives;
    }

    public void hurt(float pv){
        life = life - pv;
        this.getUI().setBarWidthAlive(Math.round((life / maxLives) * getUI().getBarWidth())); // Calcul de la largeur de la barre
    }

    /**
     * Génère des loots à partir du deathLoots et lance spawnLoot pour chacun, selon le this
     * Pour chaque loots possible, on en génère un nombre aléatoire entre min et max.
     */
    public void spawnDeathLoots() {

        for(int i =0; i<deathLoots.size();i++){
            //Pour chaque loots possible, on en génère un nombre aléatoire entre min et max
            LootType lootType= LootType.valueOf(deathLoots.get(i).first());
            int nbLoots =(int)Math.floor(Math.random() * (deathLoots.get(i).third() - deathLoots.get(i).second() + 1)) + deathLoots.get(i).second();
            //System.out.println(lootType.getName() + " : " +nbLoots+ "loots");
            for(int j = 0; j < nbLoots; j++){
                // On appel spawnLoot en définisant une direction
                int dx = Math.round((float) (Math.random() * 2 - 1));
                int dy = Math.round((float) (Math.random() * 2 - 1));
                //System.out.println("en " + dx + " "+ dy);
                lootManager.spawnLoot(lootType, getLootSpawnCenterX(lootType.width()), getPosC_Y()+lootType.offY(),true , lootManager.getJumpVec(dx, dy, 1));
            }
        }
    }

    /**Vérifie si l'objet this se trouve la zone de collision, suivant sa hitbox {@code polygon}
     *(pour rappel les entités ont seulement des rectangles et leur polygon est égal à leur rectangle) */
    public boolean isPresentInZoneNoMove(Rectangle zone) {
        float[] vertices = getHitboxPolygon().getTransformedVertices();
    
        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i];
            float y = vertices[i + 1];
    
            if (zone.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

}
