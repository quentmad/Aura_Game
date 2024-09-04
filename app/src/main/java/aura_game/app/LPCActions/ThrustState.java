package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Point;
import org.apache.commons.lang3.tuple.Pair;

public class ThrustState extends ActionState{

    public ThrustState(Animation anim){
        super(anim);
        //this.animation = new Animation(4, 7, true);
    }

    /** Static action one loop*/
    @Override
    public void act(ActorEntity entity) {
        Pair<Boolean,Integer> p = animation.returnUpdatedSpriteXWithDuration(currentSpriteX);
        boolean finish = p.getLeft();
        this.currentSpriteX = p.getRight();
        // Vérifie si l'animation est terminée
        if(finish) {
            Game.getInstance().getMyInputProc().finishAction();
        }
    }

    @Override
    public Point getMovementOf(String direction) {
        return new Point(0,0);
    }

}
