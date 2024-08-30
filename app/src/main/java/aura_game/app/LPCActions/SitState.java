package aura_game.app.LPCActions;

import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Point;
import org.apache.commons.lang3.tuple.Pair;

public class SitState extends ActionState{

    public SitState(Animation anim){
        super(anim);
        //this.animation = new Animation(30, 2, false);
    }

    /** ne fait rien car pas vraiment une action*/
    @Override
    public void act(ActorEntity entity) {

    }

    @Override
    public Point getMovementOf(String direction) {
        return new Point(0,0);
    }


}
