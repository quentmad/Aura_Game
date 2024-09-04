package aura_game.app.LPCActions;

import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Point;

public abstract class ActionState {
    /**Dans l'ordre : SpellCast, Thrust, Walk,
     * Slash, Shoot, Hurt(x1), Jump, Run, Sit(not an animation), Idle(default)*/

    //Current...
    protected int currentSpriteX;//--> Mouvement des different mouvements d'une action
    protected int currentSpriteY;//--> Mouvement des differents mouvements d'une action

    /**Afin de permettre d'afficher des frames sur plusieurs temps*/
    protected int frameDurationCounter;
    protected Animation animation;


    public ActionState(Animation anim){
        this.animation = anim;
        this.currentSpriteX = 0;
        this.frameDurationCounter = -1;//Initialisation à -1 pour que la première frame soit affichée en entier
    }

    public void resetInfo(int lastSpeed, String dir) {
        this.frameDurationCounter = -1;
        this.currentSpriteX = 0;
        this.currentSpriteY = animation.getIndexYOf(dir);
    }

    // problem: the first frame is not displayed for the first tour of an animation because the currentSpriteX is incremented before the first display because it can depend on the act (colissions...)


    public abstract void act(ActorEntity entity);

    public abstract Point getMovementOf(String direction);



    public int getCurrentSpriteX() {
        return currentSpriteX;
    }
    public int getCurrentSpriteY() {
        //System.out.println("=======current Y:  "+ currentSpriteY + "\n");
        return currentSpriteY;
    }

    public Animation getAnimation() {
        return animation;
    }

    /**@return {@code true} si l'animation est en mode "singleExecution" (ne dépend pas du temps d'appuie sur la touche)*/
    public boolean isCurrentActionAutonome(){
        return animation.isAutonomeExecution();
    }




}