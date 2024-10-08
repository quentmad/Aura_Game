package aura_game.app;

import java.util.ArrayList;

import aura_game.app.GameManager.Game;
import aura_game.app.rework.LootableObject;
import aura_game.app.rework.Tool;
import aura_game.app.rework.TypeEnum.LootableObjectType;
import aura_game.app.rework.TypeEnum.ToolType;
import com.badlogic.gdx.math.Vector2;

/**
 * Classe qui gère tous les objets de butin dans le jeu. 
 */
public class LootManager {
    private static LootManager instance;
    private ArrayList<LootableObject> loots;

    private LootManager() {
        loots = new ArrayList<>();//TODO mettre dans region
    }

    //TODO faire comme autres MANAGER
    /**
     * Méthode pour obtenir l'instance unique du gestionnaire de butin.
     * @return L'instance du gestionnaire de butin.
     */
    public static LootManager getInstance() {
        if (instance == null) {
            instance = new LootManager();
        }
        return instance;
    }

    /**
     * Supprime un objet de butin de la liste.
     * @param loot L'objet de butin à supprimer.
     * @return Vrai si l'objet de butin a été supprimé avec succès, sinon faux.
     */
    public boolean remove(LootableObject loot){
        return loots.remove(loot);
    }

    /**
     * Met à jour tous les objets de butin.
     * @param dt Delta-temps.
     */
    public void update(float dt) {
        //TODO: changer methode ? (pas boucle a chaque tour...)
        for (int i =0; i< loots.size();i++) {
            if (!loots.get(i).isOnGround()){
                loots.remove(i);
            }
        }
        for (LootableObject l : loots) {
            l.update(dt);
        }
    }

    /**
     * Méthode pour faire apparaître un objet de butin.
     * @param lt Le type de butin.
     * @param x La position en x.
     * @param y La position en y.
     * @param bounce Indique si l'objet de butin doit rebondir.
     * @param dir La direction de l'objet de butin.
     */
    public void spawnLoot(LootableObjectType lt, int x, int y, boolean bounce, Vector2 dir) {

        LootableObject loot = new LootableObject(lt,x, y, bounce, dir,true);
        Game.getInstance().getRegion().interactionComponent().abstractObjectsOnGround().add(loot);
        loots.add(loot);
    }


    /**TODO : pk ne pas direct mettre le tool en parametre ?
     * Méthode pour faire apparaître une arme.
     * @param tl Le type de butin.
     * @param x La position en x.
     * @param y La position en y.
     * @param bounce Indique si l'objet de butin doit rebondir.
     * @param dir La direction de l'objet de butin.
     * @param durability La durabilité restante de l'arme.
     */
    public void spawnTool(ToolType tl, int x, int y, boolean bounce, Vector2 dir, float durability){
        //System.out.println("new "+ lt.getName());
        Tool tool = new Tool(tl,x, y, bounce, dir,true);
        tool.setSolidity(durability);
        loots.add(tool);
    }

    /**
     * Méthode pour obtenir le vecteur de saut.
     * @param dirX La direction en x.
     * @param dirY La direction en y.
     * @param speed La vitesse du saut.
     * @return Le vecteur de saut.
     */
    public Vector2 getJumpVec(float dirX, float dirY, float speed) {
        float vecSpeed = 30;
        if (speed != 0) {
            vecSpeed = speed;
        }
        return new Vector2(dirX, dirY).nor().scl(vecSpeed);
    }
}