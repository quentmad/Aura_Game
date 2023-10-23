package aura_game.app;

import java.util.List;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import aura_game.app.Objects.CollidableObject;
import aura_game.app.Util.Triplet;

public class Chest extends CollidableObject {

    private boolean isOpen = false;

 public Chest(String name,Rectangle hitboxFlat, int posC_X, int posC_Y,
     int tall, Polygon hitboxPolygon, int hitboxHeight,int offY, List<Triplet<String, Integer, Integer>> deathLoots) {

        
        super(name,hitboxFlat,posC_X,posC_Y,tall,hitboxPolygon,hitboxHeight,offY,deathLoots);
    }

    /**
     * Ouvre le coffre et génère les butins.
     */
    public void openChest(){

        spawnDeathLoots();
    }
    

    






}
