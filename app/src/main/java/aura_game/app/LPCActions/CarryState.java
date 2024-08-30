package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;

import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.BlockEntity;
import aura_game.app.rework.Player;
import aura_game.app.rework.Point;
import org.apache.commons.lang3.tuple.Pair;


/**
 * Classe représentant l'état de l'entité lorsqu'elle porte un objet.
 */
public class CarryState extends ActionState {
     BlockEntity pushedObject;


    public CarryState(Animation anim) {
        super(anim);
    }


    /**TODO:  comment faire ? On appuie pour activer le porter puis lache apres ou on laisse appuyer tant que ?
     * Attention il est possible de rester dans la position push mais ne pas avancer si l'objet est bloqué ou si on appuie pas sur avancer
     * @param entity
     */
    @Override
    public void act(ActorEntity entity) {

        Point movement = getMovementOf(entity.stateComponant().getCurrentOrientation().getDirection());
        movement.mult(entity.speed());//On multiplie par la vitesse pour avoir le bon déplacement (selon la vitesse de l'entité)
        Point posWish = new Point(entity.posC().x() + movement.x(),entity.posC().y() + movement.y());

        if (entity.physics().isColliding(entity,posWish)==0) {//Pas de colission
            //Verifier si il est possible de pousser l'objet selon la "nouvelle position
            pushedObject.hitbox().update(movement.x(), movement.y());
            if (pushedObject.physics().isColliding(entity,posWish)==0) {//TODO pb ??
                //Alors on peut bouger le joueur et l'objet
                entity.move(movement.x(), movement.y());
                pushedObject.hitbox().update(movement.x(), movement.y());
            }else{
                entity.move(-movement.x(), -movement.y());
                entity.stateComponant().changeAction("Idle", entity.stateComponant().getCurrentOrientation());
                return;
            }
            updateSpriteXWithDuration();

            if (entity instanceof Player) {//On met a jour la caméra de la map si besoin
                Game.getInstance().getRegion().camera().calculAndUpdateCameraPosition(entity);//Update pour que le plan/map bouge en fonction du joueur TODO: les autres entites doivent pas faire sur la cam (sauf si cinematique...)
            }

        } else {
            entity.stateComponant().changeAction("Idle", entity.stateComponant().getCurrentOrientation());
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



}

