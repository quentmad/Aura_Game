package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import org.apache.commons.lang3.tuple.Pair;

public class SitState extends ActionState{

    public SitState(Animation anim){
        super(anim);
        //this.animation = new Animation(30, 2, false);
    }

    /** ne fait rien car pas vraiment une action*/
    @Override
    public void act(Entity entity) {

    }

    @Override
    public Pair<Integer, Integer> getMovementOf(String direction) {
        return Pair.of(0,0);
    }


}
