package aura_game.app.LPCActions;

import aura_game.app.Objects.Entity;
import aura_game.app.Objects.PlayableEntity;
import org.apache.commons.lang3.tuple.Pair;

public class RunState extends ActionState{

    public RunState(Animation anim){
        super(anim);
        //this.animation = new Animation(25,7,false);
    }

    @Override
    public void act(Entity entity) {
        Pair<Integer,Integer> movement = getMovementOf(entity.getEntityStateMachine().getCurrentOrientation().getDirection());
        if(!entity.isColliding(movement.getLeft(), movement.getRight())){//Pas de colission
            entity.move(movement.getLeft(),movement.getRight());
            updateSpriteXWithDuration();

            if(entity instanceof PlayableEntity){//On met a jour la caméra de la map si besoin
                entity.getActualRegion().calculAndUpdatePosition(entity);//Update pour que le plan/map bouge en fonction du joueur TODO: les autres entites doivent pas faire sur la cam (sauf si cinematique...)
            }

        }else{
            /*boolean tool = false;
            if(entity instanceof PlayableEntity){
                if(!((PlayableEntity) entity).getToolManager().getCurrentEquippedToolName().equals("")){
                    tool = true;
                }
            }*/
            entity.getEntityStateMachine().changeAction("Idle", entity.getEntityStateMachine().getCurrentOrientation());
        }

    }

    @Override
    public Pair<Integer,Integer> getMovementOf(String direction){
        return switch (direction){
            case "U" -> Pair.of(0,2);
            case "D" -> Pair.of(0,-2);
            case "L" -> Pair.of(-2,0);
            case "R" -> Pair.of(2,0);
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }



}
