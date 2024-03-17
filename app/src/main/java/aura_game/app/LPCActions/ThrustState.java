package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import aura_game.app.Objects.PlayableEntity;
import org.apache.commons.lang3.tuple.Pair;

public class ThrustState extends ActionState{

    public ThrustState(Animation anim){
        super(anim);
        //this.animation = new Animation(4, 7, true);
    }

    /** Static action one loop*/
    @Override
    public void act(Entity entity) {
        boolean finish = updateSpriteXWithDuration();
        // Vérifie si l'animation est terminée
        if(finish) {
            Game.getInstance().getMyInputProc().finishAction();
        }
    }

    @Override
    public Pair<Integer, Integer> getMovementOf(String direction) {
        return Pair.of(0,0);
    }

}
