package aura_game.app.rework;

import aura_game.app.Orientation;
import com.badlogic.gdx.math.Rectangle;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static aura_game.app.GameManager.Game.actualRegionHeight;

/** Les movables entités utilisent ce composant pour gérer les collisions (accès à l'autre via l'extend)*/
public class PhysicsMovableComponent extends PhysicsComponent{

    private Grid gridActorEntity;

    public PhysicsMovableComponent(String namePixMap) {
        super(namePixMap);
        this.gridActorEntity = new Grid(500);
    }

    public PhysicsComponent physicsComponent() {
        return get();
    }

    public Grid gridActorEntity() {
        return gridActorEntity;
    }

    /**
     * Retourne les objets (du grid BlockEntityEntity & ActorEntity)  en collision avec la zone donnée.
     * @param zone la zone de collision
     * @return la liste des objets en collision
     */
    public List<AbstractEntity> getCollidingObjectsInBothGrids(Rectangle zone){
        List<AbstractEntity> collidingObj =gridBlockEntity().getCollidingObjects(zone).getList();
        collidingObj.addAll(gridActorEntity().getCollidingObjects(zone).getList());
        return collidingObj;
    }

}
