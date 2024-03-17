package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Default State
 */
public class IdleState extends ActionState {

    public IdleState(Animation anim){
        super(anim);
        //this.animation = new Animation(34, 1,new int[]{20,6}, false);
    }

    /** Static action unlimited loop*/
    @Override
    public void act(Entity entity) {
        updateSpriteXWithDuration();
    }

    @Override
    public Pair<Integer, Integer> getMovementOf(String direction) {
        return Pair.of(0,0);
    }

}
