package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Player;
import aura_game.app.rework.Point;
import org.apache.commons.lang3.tuple.Pair;

public class JumpState extends ActionState{

    /**Valeur abs de la vitesse de la derniere action (pour sauter plus ou moins loin en fonction de l'action d'avant)*/
    private int previousSpeed;

    public JumpState(Animation anim/*ActionState previousState*/) {
        super(anim);
        //this.animation = new Animation(21,5, true);
        /*Pair<Integer,Integer> s = previousState.getMovementOf("U");
        this.previousSpeed = Math.abs(s.getLeft() + s.getRight());
        TODO a voir comment recuperer l'ancienne valeur */
    }

    @Override
    public void resetInfo(int lastSpeed, String dir) {
        this.previousSpeed = lastSpeed;
        this.frameDurationCounter = -1;
        this.currentSpriteX = 0;
        this.currentSpriteY = animation.getIndexYOf(dir);
    }

    public void act(ActorEntity entity) {
        //boolean end = false;
        Point movement = getMovementOf(entity.stateComponant().getCurrentOrientation().getDirection());
        movement.mult((int)(entity.speed()/1.5));//On multiplie par la vitesse pour avoir le bon déplacement (selon la vitesse de l'entité)
        Point posWish = new Point(entity.posC().x() + movement.x(),entity.posC().y() + movement.y());

        if(entity.physics().isColliding(entity,posWish) == 0){//Pas de colission
            entity.move(movement.x(),movement.y());

            if(entity instanceof Player){//On met a jour la caméra de la map si besoin
                Game.getInstance().getRegion().camera().calculAndUpdateCameraPosition(entity);//Update pour que le plan/map bouge en fonction du joueur TODO: les autres entites doivent pas faire sur la cam (sauf si cinematique...)
            }

            boolean finish = updateSpriteXWithDuration();
            // Vérifie si l'animation est terminée
            if(finish) {
                Game.getInstance().getMyInputProc().finishAction();
            }

        }else{
            /*boolean tool = false;
            if(entity instanceof PlayableEntity){
                if(!((PlayableEntity) entity).getToolManager().getCurrentEquippedToolName().equals("")){
                    tool = true;
                }
            }*/
            entity.stateComponant().changeAction("Idle", entity.stateComponant().getCurrentOrientation());

        }

    }

    /**
     * Cette version permet de bouger autant que l'action précedente : 0, +- 1 ou 2
     */
    @Override
    public Point getMovementOf(String direction){
        return switch (direction){
            case "U" -> new Point(0,previousSpeed);
            case "D" -> new Point(0,-previousSpeed);
            case "L" -> new Point(-previousSpeed,0);
            case "R" -> new Point(previousSpeed,0);
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }



}
