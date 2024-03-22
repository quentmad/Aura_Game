package aura_game.app.LPCActions;

import aura_game.app.Objects.Entity;
import aura_game.app.Objects.PlayableEntity;
import org.apache.commons.lang3.tuple.Pair;

public class WalkState extends ActionState{

    public WalkState(Animation anim) {
        super(anim);

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
            /*System.out.println("colission so idle");
            boolean tool = false;
            if(entity instanceof PlayableEntity){
                if(!((PlayableEntity) entity).getToolManager().getCurrentEquippedToolName().equals("")){
                    tool = true;
                }
            }*/

            entity.getEntityStateMachine().changeAction("Idle", entity.getEntityStateMachine().getCurrentOrientation());
        }
        //System.out.println("---- currentX :   "+ currentSpriteX);


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
