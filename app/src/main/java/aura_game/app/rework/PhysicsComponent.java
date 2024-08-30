package aura_game.app.rework;

import aura_game.app.GameManager.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static aura_game.app.GameManager.Game.actualRegionHeight;

/**Utilisé pour les abstractEntity, ayant besoin de gestion de collision static avec le sol / objets*/
public class PhysicsComponent {

    /** map pixmap de collision */
    protected Pixmap groundPixCollision;
    /** gridBlockEntity display sur la map */
    protected Grid gridBlockEntity;


    public PhysicsComponent(String namepixmap) {
        this.groundPixCollision = new Pixmap(Gdx.files.internal("src/main/resources/" + namepixmap + ".png"));

        this.gridBlockEntity = new Grid(500);
    }

    /**
     * Vérifie si le polygone est en collision avec la map de collision
     * @param verticesWithInt tableau de sommets du polygone avec des points intermédiaires, déjà mis à jour avec la position de l'entité (via translateIntermediatePoints)
     * @return
     */

    public boolean isPolygonCollidingWithPixmap(float[] verticesWithInt){
        //Vérifie si un des points du polygone est sur un pixel autre que blanc de la map de collision
        for (int i = 0; i <verticesWithInt.length; i+=2) {
            //Debug.getInstance().drawPoint(Math.round(verticesWithInt[i]), Math.round(verticesWithInt[i+1]));
            //System.out.println("Collision detection with pixmap at " + Math.round(verticesWithInt[i]) + " " + Math.round(verticesWithInt[i+1]));
            //0 0 est en haut a gauche
            if (groundPixCollision.getPixel(Math.round(verticesWithInt[i]), actualRegionHeight -  Math.round(verticesWithInt[i+1])) != 0) {

                System.out.println("Collision detected with pixmap");
                return true;
            }
        }
        //System.out.println("No collision detected with pixmap");
        return false;
    }



    /**
     * Détermine avec quels AbstractEntity le polygone est en collision
     * @param ActorVerticesWithInt tableau de sommets du polygone avec des points intermédiaires, déjà mis à jour avec la position de l'entité (via translateIntermediatePoints)
     */
    public List<AbstractEntity> getCollidingPolygons(float[] ActorVerticesWithInt, List<AbstractEntity> potentialCollidingEntities){
        List<AbstractEntity> collidingObj = new ArrayList<>();
        for (AbstractEntity object : potentialCollidingEntities) {
            for (int i = 0; i < ActorVerticesWithInt.length; i += 2) {
                if (object.hitbox().preciseHitbox.contains(Math.round(ActorVerticesWithInt[i]), Math.round(ActorVerticesWithInt[i + 1]))) {
                    collidingObj.add(object);
                    break;//L'entité est en collision avec l'objet
                }

            }
        }
        return collidingObj;
    }


    /**
     * Détermine si l'entité est en collision avec un {@code BlockEntity} ou le {@code sol}
     * @param actor l'entité
     * @param posWish position que souhaite avoir l'entité
     * @return -1 si collision avec le sol, sinon le nombre d'entités avec lesquelles il y a collision
     */
    public int isColliding(AbstractEntity actor, Point posWish){

        float[] polyActor = actor.hitbox().translateIntermediatePoints(posWish.x(), posWish.y());
        if(isPolygonCollidingWithPixmap(polyActor)){
           return -1;
        }
        //On ne teste que avec gridBlockEntity car la colission avec une entité n'empeche pas de bouger
        List<AbstractEntity> objColList = gridBlockEntity.getCollidingObjects(new Rectangle(posWish.x(), posWish.y(), actor.hitbox().width, actor.hitbox().height)).getList();
        //System.out.println(actor.name() + " is colliding with " + objColList.size() + " Abstractobjects");
        return getCollidingPolygons(polyActor, objColList).size();

    }


    public Grid gridActorEntity() {
        throw new UnsupportedOperationException("Not implemented in parent");
    }

    public List<AbstractEntity> getCollidingObjectsInBothGrids(Rectangle zone){
        throw new UnsupportedOperationException("Not implemented in parent");
    }

    public Grid gridBlockEntity() {
        return gridBlockEntity;
    }

    public PhysicsComponent get(){
        return this;
    }




}
