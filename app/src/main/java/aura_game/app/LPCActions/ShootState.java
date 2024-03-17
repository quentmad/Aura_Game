package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import org.apache.commons.lang3.tuple.Pair;

public class ShootState extends ActionState{

    public ShootState(Animation anim){
        super(anim);
        //this.animation = new Animation(16,12,new int[]{2,2,2,2,2,2,2,3,4,2,2,2,2}, true);
    }

    /** Static action one loop*/
    public void act(Entity entity) {
        boolean finish = updateSpriteXWithDuration();
        // Vérifie si l'animation est terminée
        if(finish) {
            Game.getInstance().getMyInputProc().finishAction();
            //TODO: hit...

        }
    }

    @Override
    public Pair<Integer, Integer> getMovementOf(String direction) {
        return Pair.of(0,0);
    }

}
