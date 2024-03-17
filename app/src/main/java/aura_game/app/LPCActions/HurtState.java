package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import org.apache.commons.lang3.tuple.Pair;

public class HurtState extends ActionState{

    private boolean alive;

    public HurtState(Animation anim) {

        super(anim);
        alive = true;
    }

    /** Static action one loop*/
    @Override
    public void act(Entity entity) {
        if(alive) {
            if(updateSpriteXWithDuration()){// Vérifie si l'animation est terminée
                alive = false;
            }
        }

    }

    @Override
    public Pair<Integer, Integer> getMovementOf(String direction) {
        return Pair.of(0,0);
    }


}
