package aura_game.app.LPCActions;

import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Point;
import org.apache.commons.lang3.tuple.Pair;

public class HurtState extends ActionState{

    private boolean alive;

    public HurtState(Animation anim) {

        super(anim);
        alive = true;
    }

    /** Static action one loop*/
    @Override
    public void act(ActorEntity entity) {
        if(alive) {
            if(updateSpriteXWithDuration()){// Vérifie si l'animation est terminée
                alive = false;
            }
        }

    }

    @Override
    public Point getMovementOf(String direction) {
        return new Point(0,0);
    }


}
