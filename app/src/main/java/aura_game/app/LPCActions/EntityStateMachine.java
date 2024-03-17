package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**Classe représentant la machine à états du personnage*/
public class EntityStateMachine {
    private ActionState currentState;
    private String currentStateName;
    private final Map<String, ActionState> states;

    /**(U, D, L, R) utile pour attaquer... changeAction (action+currentDirectionLetter) */
    private String currentDirectionLetter;

    public EntityStateMachine() {
        states = new HashMap<>();
        init();
        setCurrentState("Idle");

    }

    /**Ajoute un état à la machine à états*/
    public void addState(String stateName, ActionState state) {
        states.put(stateName, state);
    }

    /**Initialise la hashmap des actions possibles*/
    private void init(){//A terme: mettre le new animation en param de constructeur pour pouvoir personnaliser
        /*Dans l'ordre : SpellCast, Thrust, Walk,
         * Slash, Shoot, Hurt(x1), Jump, Run, Sit(not an animation), Idle(default)
         */
        addState("SpellCast", new SpellCastState(new Animation(0,6,true)));
        addState("Thrust", new ThrustState(new Animation(4, 7, true)));
        addState("Walk",new WalkState(new Animation(8,8,new int[]{2,3,2,3,3,2,2,3,3},false)));
        addState("Slash",new SlashState(new Animation(12,5, new int[]{2,1,1,2,4,3},true)));
        addState("Shoot", new ShootState(new Animation(16,12,new int[]{2,2,2,2,2,2,2,2,3,4,2,2,2}, true)));
        addState("Hurt", new HurtState(null/*----------------TODO---------------*/));//20
        addState("Jump", new JumpState(new Animation(21,5, new int[]{2,2,2,3,4,5},true)));
        addState("Run", new RunState(new Animation(25,7,new int[]{3,2,2,3,3,2,2,3},false)));
        addState("Sit", new SitState(new Animation(29, 2, false)));
        addState("Idle", new IdleState(new Animation(33, 1,new int[]{20,6}, false)));



    }


    public String getCurrentDirectionLetter() {
        return currentDirectionLetter;
    }

    public void setCurrentDirectionLetter(String dir) {
        currentDirectionLetter = dir;
    }
    public ActionState getCurrentState() {
        return currentState;
    }

    public String getCurrentStateName() {
        return currentStateName;
    }

    /**Définit l'état actuel*/
    public void setCurrentState(String stateName) {
        currentState = states.get(stateName);
        currentStateName = stateName;

    }

    /**
     * Appel changeStateAction si changement d'action ou juste change la direction si l'action en elle meme reste la meme
     * @return
     */
    public void changeAction(String action, String direction, boolean haveATool){
        if(action !="" && direction != null && direction.length()==1) {
            if(action.equals(getCurrentStateName())){//Simple changement de direction
                this.currentDirectionLetter = direction;
                this.currentStateName = action;
                this.currentState.currentSpriteX =0;
                this.currentState.currentSpriteY = currentState.animation.getIndexYOf(direction);
            }else{
                changeStateAction(action, direction, haveATool);
            }
        }
        System.out.println("current action " + currentStateName);
    }

    /**Méthode appelée lorsqu'on souhaite changer d'action
     * Attention : changer simplement de direction ne change pas de stateAction mais simplement {@code currentDirectionLetter}
     * @param action l'action a mettre (ex: Walk, Slash, Sit)
     * @param direction de l'action qu'on veut mettre (U,L,R,D)
     * @param haveATool vrai si c'est le joueur et qu'il a un tool en main
     * @return {@code true} si le currentTool du player doit être update sinon {@code false}
     */
    public boolean changeStateAction(String action, String direction, boolean haveATool){
        if(!action.isEmpty() && direction.length()==1){
            Pair<Integer,Integer> prev = currentState.getMovementOf(direction);
            int val = Math.abs(prev.getLeft() + prev.getRight());
            System.out.println("previous speed : " + val);
            this.setCurrentState(action);
            currentState.resetInfo(val, direction);//remet a 0 les currents et defini le previousSpeed de jump si c'est un jump
            this.currentStateName = action;
            this.currentDirectionLetter = direction;

            //Si c'est un joueur (LPC) et qu'il a un Tool en main, penser à mettre à jour spriteY et size: retourne true
            if (haveATool && Game.getInstance().isGameStarted()){
                return true;//TODO: a revoir
                //if(!((PlayableEntity) this).getCurrentToolName().equals("")){
                    //((PlayableEntity)this).updateSpriteToolInfo();//TODO comparer pour pas lancer ca a chaque change action meme h24 sans outils
                    //((PlayableEntity)this).updateSpriteDurationFromActionName();
                //}

            }
        }
        return false;
    }


    /**Exécute l'action associée à l'état actuel*/
    public void executeCurrentAction(Entity entity) {
        if (currentState != null && currentState != null) {
            currentState.act(entity);
        } else {
            System.out.println("No current state set or action not defined.");
        }
    }


}