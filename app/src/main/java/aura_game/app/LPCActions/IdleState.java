package aura_game.app.LPCActions;

import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Point;

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
    public void act(ActorEntity entity) {

        //System.out.println(" !!! debug: player shouldn't have colission with pixmap when he doesn't walk : colission : \n" + entity.physics().isColliding(entity,entity.posC()));

        this.currentSpriteX = animation.returnUpdatedSpriteXWithDuration(currentSpriteX).getRight();
    }

    @Override
    public Point getMovementOf(String direction) {
        return new Point(0,0);
    }

}
