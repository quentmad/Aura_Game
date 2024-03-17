package aura_game.app.LPCActions;

import aura_game.app.Objects.CollidableObject;
import aura_game.app.Objects.Entity;
import aura_game.app.Objects.IAEntity;
import aura_game.app.Objects.PlayableEntity;
import com.badlogic.gdx.math.Rectangle;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class WalkState extends ActionState{

    public WalkState(Animation anim) {
        super(anim);

    }

    @Override
    public void act(Entity entity) {
        Pair<Integer,Integer> movement = getMovementOf(entity.getEntityStateMachine().getCurrentDirectionLetter());
        if(!entity.isColliding(movement.getLeft(), movement.getRight())){//Pas de colission
            entity.move(movement.getLeft(),movement.getRight());
            updateSpriteXWithDuration();

            if(entity instanceof PlayableEntity){//On met a jour la caméra de la map si besoin
                entity.getActualRegion().calculAndUpdatePosition(entity);//Update pour que le plan/map bouge en fonction du joueur TODO: les autres entites doivent pas faire sur la cam (sauf si cinematique...)
            }

        }else{
            boolean tool = false;
            if(entity instanceof PlayableEntity){
                if(!((PlayableEntity) entity).getCurrentToolName().equals("")){
                    tool = true;
                }
            }

            entity.getEntityStateMachine().changeStateAction("Idle", entity.getEntityStateMachine().getCurrentDirectionLetter(),tool);
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
