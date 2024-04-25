package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import aura_game.app.Objects.Item;
import aura_game.app.Objects.PlayableEntity;
import org.apache.commons.lang3.tuple.Pair;


/**
 * Classe représentant l'état de l'entité lorsqu'elle porte un objet.
 */
public class CarryState extends ActionState {
    Item pushedObject;


    public CarryState(Animation anim) {
        super(anim);
    }


    /**TODO:  comment faire ? On appuie pour activer le porter puis lache apres ou on laisse appuyer tant que ?
     * Attention il est possible de rester dans la position push mais ne pas avancer si l'objet est bloqué ou si on appuie pas sur avancer
     * @param entity
     */
    @Override
    public void act(Entity entity) {

        Pair<Integer, Integer> movement = getMovementOf(entity.getEntityStateMachine().getCurrentOrientation().getDirection());
        if (!entity.isColliding(movement.getLeft(), movement.getRight())) {//Pas de colission
            //Verifier si il est possible de pousser l'objet selon la "nouvelle position
            pushedObject.updateHitboxsAndPosition(movement.getLeft(), movement.getRight());
            if (!pushedObject.willCollideIn(Game.getInstance().getRegion())) {
                //Alors on peut bouger le joueur et l'objet
                entity.move(movement.getLeft(), movement.getRight());
                pushedObject.updateHitboxsAndPosition(movement.getLeft(), movement.getRight());
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

