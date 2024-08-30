package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.MyInputProc;
import aura_game.app.rework.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;


/**
 * Classe représentant l'état de l'entité lorsqu'elle pousse un objet.
 * Ce state n'est activable que si l'entité est en train de marcher, et sera désactivé si l'entité s'arrête de marcher ou si elle n'a pas d'objet à pousser.
 */
public class PushState extends ActionState {
    BlockEntity pushedObject;
    int distanceWithObjectPushed;
    private final int distancePushed = 25;


    public PushState(Animation anim) {
        super(anim);
        this.pushedObject = null;
        this.distanceWithObjectPushed = -1;
    }


    /** Méthode lancé lorsqu'on appuie sur la touche pour pousser un objet, on cherche l'objet à pousser (de type item)
     * @param entity l'entité qui pousse l'objet
     * @return true si un objet a été trouvé et qu'il est poussable, false sinon
     */

    public void findObjectToPush(ActorEntity entity){

        //On cherche l'objet à pousser depuis le player
        int x = entity.stateComponant().getCurrentOrientation().getX();
        int y = entity.stateComponant().getCurrentOrientation().getY();
        //Nouveau rectangle basé sur la zone de colission de l'entité aggrandit de "distancePushed" dans la direction de l'entité
        Rectangle zonePush = new Rectangle(entity.hitbox().approximativeHitbox().x,entity.hitbox().approximativeHitbox().y, entity.hitbox().approximativeHitbox().width + x*distancePushed, entity.hitbox().approximativeHitbox().height+ y*distancePushed);
        List<AbstractEntity> objsCol = entity.physics().getCollidingObjectsInBothGrids(zonePush);
        this.pushedObject = getFirstBlock(objsCol);
        if(pushedObject != null){
            this.distanceWithObjectPushed = entity.calculateDistanceFromCenter(pushedObject);
            System.out.println("Object found: "+pushedObject.name() +" distance: "+distanceWithObjectPushed);
        }
    }


    /**
     *
     * Lors du premier appel de la méthode, on cherche l'objet à pousser, puis on le pousse si possible
     * @param entity
     */
    @Override
    public void act(ActorEntity entity) {
        boolean succeed = false;
        if (pushedObject != null && pushedObject.movable() && Math.abs(entity.calculateDistanceFromCenter(pushedObject)- distanceWithObjectPushed) < 20) {
            System.out.println("Pushing object");
            //On bouge l'objet ainsi que l'entité
            Point movement = getMovementOf(entity.stateComponant().getCurrentOrientation().getDirection());
            movement.mult((int)(entity.speed()/3));//On multiplie par la vitesse pour avoir le bon déplacement (selon la vitesse de l'entité)
            Point posWishEntity = new Point(entity.posC().x() + movement.x(),entity.posC().y() + movement.y());
            Point posWishObject = new Point(pushedObject.posC().x() + movement.x(),pushedObject.posC().y() + movement.y());
            //On vérifie si il n'y a pas de colission
            int objectColission = pushedObject.physics().isColliding(pushedObject,posWishObject);
            boolean objectCanMove = objectColission == 0 || objectColission == 1; //Pas de colission ou colission avec lui meme car vérifie la colission avec lui meme
            if(objectCanMove) {
                System.out.println("Object can move");
                pushedObject.move(movement.x(), movement.y());
                pushedObject.toStringInfo();
                Game.getInstance().getRegion().interactionComponent().abstractObjectsOnGround().setNeedSort(true);
                if(entity.physics().isColliding(entity,posWishEntity) == 0) {
                    System.out.println("Entity can move");
                    //On bouge ensuite l'entité
                    entity.move(movement.x(),movement.y());
                    updateSpriteXWithDuration();
                    succeed = true;
                    if(entity instanceof Player){//On met a jour la caméra de la map si besoin
                        Game.getInstance().getRegion().camera().calculAndUpdateCameraPosition(entity);//Update pour que le plan/map bouge en fonction du joueur TODO: les autres entites doivent pas faire sur la cam (sauf si cinematique...)
                    }
                    //TEST VERIF
                    System.out.println("Pos"+ pushedObject.posC().x() +" " + pushedObject.posC().y() + "player:"+ entity.posC().x() +" "+ entity.posC().y());



                }
            }
        }

        //Si le bouton n'est plus appuyé, on arrête de pousser l'objet
        boolean finish = ! MyInputProc.getInstance().getKeysPressed().contains(Input.Keys.Y);//si on appuie plus sur la touche Y
        // Vérifie si l'animation est terminée
        if(finish) {
            Game.getInstance().getMyInputProc().finishAction();
        }

        if(!succeed) {
            pushedObject = null;
            distanceWithObjectPushed = -1;
            entity.stateComponant().changeAction("Idle", entity.stateComponant().getCurrentOrientation());
        System.out.println("Pushing failed");
        }


    }

    @Override
    public Point getMovementOf(String direction){
        return switch (direction){
            case "U" -> new Point(0,1);
            case "D" -> new Point(0,-1);
            case "L" -> new Point(-1,0);
            case "R" -> new Point(1,0);
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }


    /**
     * Méthode permettant de récupérer le premier objet de type BlockEntity dans une liste d'objets AbstractEntity
     * @param objsCol
     * @return
     */
    private BlockEntity getFirstBlock(List<AbstractEntity> objsCol){
        for (AbstractEntity obj : objsCol){
            if (obj instanceof BlockEntity){
                return (BlockEntity) obj;
            }
        }
        return null;
    }

}
