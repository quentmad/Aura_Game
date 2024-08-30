package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Player;
import aura_game.app.rework.Point;

public class WalkState extends ActionState{

    public WalkState(Animation anim) {
        super(anim);

    }


    /** Permet de faire marcher l'entité dans la direction actuelle s'il n'y a pas de colission, sinon on change l'action de l'entité en Idle
     * @param entity l'entité qui doit marcher
     */
    public void act(ActorEntity entity){
        Point movement = getMovementOf(entity.stateComponant().getCurrentOrientation().getDirection());
        movement.mult(entity.speed());//On multiplie par la vitesse pour avoir le bon déplacement (selon la vitesse de l'entité)
        Point posWish = new Point(entity.posC().x() + movement.x(),entity.posC().y() + movement.y());

        if(entity.physics().isColliding(entity,posWish) == 0){//Pas de colission
            entity.move(movement.x(),movement.y());
            updateSpriteXWithDuration();

            if(entity instanceof Player){//On met a jour la caméra de la map si besoin
                Game.getInstance().getRegion().camera().calculAndUpdateCameraPosition(entity);//Update pour que le plan/map bouge en fonction du joueur TODO: les autres entites doivent pas faire sur la cam (sauf si cinematique...)
            }

        }else{
            //System.out.println("COLLISION, cant walk ");
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
