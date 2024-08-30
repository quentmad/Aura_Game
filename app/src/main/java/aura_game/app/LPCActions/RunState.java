package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Player;
import aura_game.app.rework.Point;
import org.apache.commons.lang3.tuple.Pair;

public class RunState extends ActionState{

    public RunState(Animation anim){
        super(anim);
        //this.animation = new Animation(25,7,false);
    }

    @Override
    public void act(ActorEntity entity) {
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
            /*boolean tool = false;
            if(entity instanceof PlayableEntity){
                if(!((PlayableEntity) entity).getToolManager().getCurrentEquippedToolName().equals("")){
                    tool = true;
                }
            }*/
            entity.stateComponant().changeAction("Idle", entity.stateComponant().getCurrentOrientation());
        }

    }

    @Override
    public Point getMovementOf(String direction){
        return switch (direction){
            case "U" -> new Point(0,2);
            case "D" -> new Point(0,-2);
            case "L" -> new Point(-2,0);
            case "R" -> new Point(2,0);
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }



}
