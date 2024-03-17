package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import org.apache.commons.lang3.tuple.Pair;

public class SlashState extends ActionState{

    public SlashState(Animation anim){
        super(anim);
        //this.animation = new Animation(12,5, new int[]{2,2,2,3,4,5},true);
    }

    /** Static action one loop (hit at the end)*/
    @Override
    public void act(Entity entity) {
        boolean finish = updateSpriteXWithDuration();
        // Vérifie si l'animation est terminée
        if(finish) {
            entity.hit();
            Game.getInstance().getMyInputProc().finishAction();
        }
    }

    @Override
    public Pair<Integer, Integer> getMovementOf(String direction) {
        return Pair.of(0,0);
    }


}
