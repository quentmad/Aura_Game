package aura_game.app.LPCActions;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.Entity;
import aura_game.app.Orientation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**Classe représentant la machine à états du personnage*/
public class EntityStateMachine {
    private ActionState currentState;
    private String currentStateName;
    private final Map<String, ActionState> states;

    /**(U, D, L, R) utile pour attaquer... changeAction (action+currentDirectionLetter) */
    //private String currentDirectionLetter;
    /**TODO: A init et modif quand il faut puis supprimer currentDirectionLetter)*/
    private Orientation currentOrientation; //Contient le currentDirectionLETTER !!!! (on pourra l'enlever)

    public EntityStateMachine() {
        states = new HashMap<>();
        loadStates();
        setCurrentState("Idle");
        //changeAction("Idle", Orientation.SOUTH);

    }

    /**Ajoute un état à la machine à états*/
    public void addState(String stateName, ActionState state) {
        states.put(stateName, state);
    }

    /**Initialise la hashmap des actions possibles*/
    private void loadStates(){
        /*Dans l'ordre : SpellCast, Thrust, Walk,
         * Slash, Shoot, Hurt(x1), Jump, Run, Sit(not an animation), Idle(default)
         */
        addState("SpellCast", new SpellCastState(new Animation(0,6,new int[]{2,2,2,2,3,5,3,2},true)));
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

    public Orientation getCurrentOrientation() {
        return currentOrientation;
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
    public void changeAction(String action, Orientation orientation){
        if(StringUtils.isNotEmpty(action) && orientation != null){
            String currentStateName = getCurrentStateName();
            if(action.equals(currentStateName)){//On change juste de direction
                this.currentOrientation = orientation;
                this.currentState.currentSpriteX = 0;
                this.currentState.currentSpriteY = currentState.animation.getIndexYOf(currentOrientation.getDirection());
            }else{//On change d'action
                changeStateAction(action, orientation);
            }
            Game.getInstance().getPlayer().getToolManager().updateEquippedToolInfo();//Met à jour les informations de l'outil actuellement équipé car l'action ou la direction de l'entité change

        }
    }


    /**Méthode appelée lorsqu'on souhaite changer d'action
     * Attention : changer simplement de direction ne change pas de stateAction mais simplement {@code currentOrientation}
     * @param action l'action a mettre (ex: Walk, Slash, Sit)
     * @param orientation de l'action qu'on veut mettre (North, south...)
     * @return {@code true} si le currentTool du player doit être update sinon {@code false}
     */
    private boolean changeStateAction(String action, Orientation orientation){
        if(StringUtils.isNotEmpty(action) && orientation != null){
            this.currentOrientation = orientation;

            Pair<Integer,Integer> prev = currentState.getMovementOf(currentOrientation.getDirection());
            int val = Math.abs(prev.getLeft() + prev.getRight());

            this.setCurrentState(action);
            currentState.resetInfo(val, currentOrientation.getDirection());
            return true;
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