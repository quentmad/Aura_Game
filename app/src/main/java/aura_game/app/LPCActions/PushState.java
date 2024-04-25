package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.CollidableObject;
import aura_game.app.Objects.Entity;
import aura_game.app.Objects.Item;
import aura_game.app.Objects.PlayableEntity;
import org.apache.commons.lang3.tuple.Pair;


/**
 * Classe représentant l'état de l'entité lorsqu'elle pousse un objet.
 */
public class PushState extends ActionState {
    Item pushedObject;
    private final int distancePushed = 8;


    public PushState(Animation anim) {
        super(anim);
        this.pushedObject = null;
    }


    /** Méthode lancé lorsqu'on appuie sur la touche pour pousser un objet, on cherche l'objet à pousser (de type item)
     * @param entity l'entité qui pousse l'objet
     */
    public void findObjectToPush(Entity entity){
        //On cherche l'objet à pousser depuis le player
        int x = entity.getEntityStateMachine().getCurrentOrientation().getX();
        int y = entity.getEntityStateMachine().getCurrentOrientation().getY();
        CollidableObject objCol = (entity.isCollidingWithObjects(x*distancePushed,y * distancePushed));
        this.pushedObject = (objCol instanceof Item) ? (Item) objCol : null;

        if(pushedObject != null)System.out.println("Pushed object : " + pushedObject.getName());
    }


    /**TODO:  comment faire ? On appuie pour activer le porter puis lache apres ou on laisse appuyer tant que ?
     * Attention il est possible de rester dans la position push mais ne pas avancer si l'objet est bloqué ou si on appuie pas sur avancer
     * @param entity
     */
    @Override
    public void act(Entity entity) {

        Pair<Integer,Integer> movement = getMovementOf(entity.getEntityStateMachine().getCurrentOrientation().getDirection());
        //Si on tiens toujours l'objet et que l'entité n'est pas en collision avec le sol
        if (pushedObject != null && !entity.isCollidingWithGround(0,0) && entity.isCollidingWithObjects(entity.getEntityStateMachine().getCurrentOrientation().getX()+distancePushed,entity.getEntityStateMachine().getCurrentOrientation().getY()+distancePushed) == pushedObject){


            //Si l'objet ne va pas rentrer en collision avec la map ou autres collidableObject, sauf nous (l'entité)
            System.out.println("if one passed");
            if (!pushedObject.willCollideInIgnoring(Game.getInstance().getRegion(), entity)) {
                System.out.println("if two passed");
                //Alors on peut bouger le joueur et l'objet
                //TODO: Que si le move bien enfoncé...
                entity.move(movement.getLeft(), movement.getRight());
                pushedObject.move(movement.getLeft(), movement.getRight(), entity.getSpeed());
                //update la liste des objets dans la map
                Game.getInstance().getRegion().updateItemBasicObjectsOnRegion(pushedObject);

            }else{
                entity.move(-movement.getLeft(), -movement.getRight());
                entity.getEntityStateMachine().changeAction("Idle", entity.getEntityStateMachine().getCurrentOrientation());
                return;
            }
            updateSpriteXWithDuration();

            if (entity instanceof PlayableEntity) {//On met a jour la caméra de la map si besoin
                entity.getActualRegion().calculAndUpdatePosition(entity);//Update pour que le plan/map bouge en fonction du joueur TODO: les autres entites doivent pas faire sur la cam (sauf si cinematique...)
            }

        } else {
            entity.getEntityStateMachine().changeAction("Idle", entity.getEntityStateMachine().getCurrentOrientation());
        }
    }

    @Override
    public Pair<Integer,Integer> getMovementOf(String direction){
        return switch (direction){
            case "U" -> Pair.of(0,1);
            case "D" -> Pair.of(0,-1);
            case "L" -> Pair.of(-1,0);
            case "R" -> Pair.of(1,0);
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }



}
