package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Player;
import aura_game.app.rework.Point;
import aura_game.app.rework.Tool;
import aura_game.app.rework.Tools.RangedTool;
import org.apache.commons.lang3.tuple.Pair;

public class ShootState extends ActionState{

    public ShootState(Animation anim){
        super(anim);
        //this.animation = new Animation(16,12,new int[]{2,2,2,2,2,2,2,3,4,2,2,2,2}, true);
    }

    /** Static action one loop*/
    public void act(ActorEntity entity) {
        boolean finish = updateSpriteXWithDuration();
        // Vérifiez si l'animation est terminée
        if(finish) {
            if (entity instanceof Player) {
                Tool equippedTool = ((Player) entity).toolManager().getCurrentEquippedTool();
                if (equippedTool instanceof RangedTool) {
                    ((RangedTool) equippedTool).shoot((Player) entity, null);
                }
            }
            Game.getInstance().getMyInputProc().finishAction();
        }
    }

    @Override
    public Point getMovementOf(String direction) {
        return new Point(0,0);
    }

}
