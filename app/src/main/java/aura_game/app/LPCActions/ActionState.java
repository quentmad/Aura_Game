package aura_game.app.LPCActions;

import aura_game.app.Objects.Entity;
import org.apache.commons.lang3.tuple.Pair;

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


    public abstract void act(Entity entity);

    public abstract Pair<Integer, Integer> getMovementOf(String direction);

    /**Met à jour le spriteX en lui attribuant le spriteX suivant de l'action actuelle
     * @return true si l'animation a effectué un tour complet (est revenu à 0)
     */
    private boolean incrementCurrentSpriteX(){//TODO: on fait vraiment le 0 au debut ?
        int old = currentSpriteX;
        currentSpriteX = (currentSpriteX + 1);
        if(currentSpriteX > animation.getLastIndexX()){//On a fait un tour complet
            currentSpriteX = 0;
            //System.out.println("---- currentX :   "+ currentSpriteX);
            return true;
        }
        //System.out.println("----- currentX :   "+ currentSpriteX);
        return false;

    }

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

    /**
     * Ajoute 1 au temps de la frame actuelle et passe à la frame suivante si besoin.
     * @return true si l'animation a effectué un "tour" complet.
     */
    public boolean updateSpriteXWithDuration() {
        frameDurationCounter++;
        if(frameDurationCounter >= animation.getFrameDuration(currentSpriteX) ) {
            frameDurationCounter = 0;
            return incrementCurrentSpriteX();//Execution complete de l'animation ?
        }
        return false;
    }

}