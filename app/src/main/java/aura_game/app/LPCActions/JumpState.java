package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import aura_game.app.Objects.PlayableEntity;
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

    public void act(Entity entity) {
        boolean end = false;
        Pair<Integer,Integer> movement = getMovementOf(entity.getEntityStateMachine().getCurrentOrientation().getDirection());

        if(!entity.isColliding(movement.getLeft(), movement.getRight())){//Pas de colission
            entity.move(movement.getLeft(),movement.getRight());

            if(entity instanceof PlayableEntity){//On met a jour la caméra de la map si besoin
                entity.getActualRegion().calculAndUpdatePosition(entity);//Update pour que le plan/map bouge en fonction du joueur TODO: les autres entites doivent pas faire sur la cam (sauf si cinematique...)
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
            entity.getEntityStateMachine().changeAction("Idle", entity.getEntityStateMachine().getCurrentOrientation());

        }

    }

    /**
     * Cette version permet de bouger autant que l'action précedente : 0, +- 1 ou 2
     */
    @Override
    public Pair<Integer,Integer> getMovementOf(String direction){
        return switch (direction){
            case "U" -> Pair.of(0,previousSpeed);
            case "D" -> Pair.of(0,-previousSpeed);
            case "L" -> Pair.of(-previousSpeed,0);
            case "R" -> Pair.of(previousSpeed,0);
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }



}
