package aura_game.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import aura_game.app.rework.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import aura_game.app.GameManager.Game;
/**
 * Singleton garantissant qu'il n'y ait qu'une seule instance de MyInputProc dans l'application
 */
public class MyInputProc implements InputProcessor {

    private static MyInputProc instance;

    /**Liste des touches valides*/
    private final List<Integer> validKeys = Arrays.asList(
            Input.Keys.LEFT,
            Input.Keys.RIGHT,
            Input.Keys.UP,
            Input.Keys.DOWN,
            Input.Keys.I,//Inventory
            Input.Keys.W,//Left menu
            Input.Keys.X,//Right menu
            0, // Slash
            Input.Keys.L, // Spellcast
            Input.Keys.P, // Thrust
            1, // Shoot
            Input.Keys.SPACE, // Jump
            Input.Keys.D,//run (if walking)
            Input.Keys.S,//cheat give
            Input.Keys.R,//wheel ranged
            Input.Keys.M,//wheel melee
            Input.Keys.ENTER,//validate in menu
            Input.Keys.A,//select melee tool
            Input.Keys.Z,//select ranged tool
            Input.Keys.BACKSPACE,//drop item from inventory
            Input.Keys.U,//carry (if walking)
            Input.Keys.Y, //push (if walking)
            Input.Keys.H//help buttons

    );
    /**Liste des keycode actuellement pressée*/
    private List<Integer> keysPressed;
    
    /**Liste des orientations des walks en cours (North, South...)
     * Permet de pouvoir passer d'une direction/action à l'autre sans être arrêté par changeAction(null) si on lâche une ancienne touche
     */
    private List<Orientation> activeWalkingOrientations;

    /**Si une action instantanée/autonome est en cours*/
    private boolean onGoingAutonomeAction;

    /**Instance unique du player présent dans GameManager*/
    private Player player;

    private MyInputProc() {
        // Constructeur privé pour empêcher l'instanciation directe
        Gdx.input.setInputProcessor(this);//gestionnaire d'entrée actif
        this.keysPressed = new ArrayList<>();
        this.activeWalkingOrientations = new ArrayList<>();
        player = Game.getInstance().getPlayer();
        onGoingAutonomeAction = false;
    }
    
    public static MyInputProc getInstance() {
        if (instance == null) {
            instance = new MyInputProc();
        }
        return instance;
    }


    public void setOnGoingAutonomeAction(boolean b){
        onGoingAutonomeAction = b;
    }

    public Orientation getOrientationIfWalkingFromKeycode(int keycode){//TODO enum Direction
        return switch (keycode) {
            case Input.Keys.LEFT -> Orientation.WEST;
            case Input.Keys.RIGHT -> Orientation.EAST;
            case Input.Keys.UP -> Orientation.NORTH;
            case Input.Keys.DOWN -> Orientation.SOUTH;
            default -> null;
        };
    }

    private boolean handleKeyDown(int keycode) {
        // Vérifie si la touche enfoncée correspond à une action valide
        if (!isValidActionKey(keycode)) {
            return false;
        }
        //Si la touche n'est pas déjà enfoncée
        if (!keysPressed.contains(keycode)) {
            //Si la touche E est enfoncée et qu'il y a des actions en cours, on arrête ces actions, afin d'ouvrir l'inventaire
            if (keycode == Input.Keys.E && !activeWalkingOrientations.isEmpty()) {
                stopActions();
            }

            //Si aucune action n'est en cours
            if (!onGoingAutonomeAction ) {
                //On ajoute la touche à la liste des touches enfoncées
                keysPressed.add(keycode);
                handleContinuousAction(keycode);
                //On effectue l'action associée à la touche enfoncée
                Game.getInstance().getInputHandler().performAction(keycode);
                onGoingAutonomeAction = player.stateComponant().getCurrentState().isCurrentActionAutonome();
                //On retourne true pour indiquer que la touche a été enfoncée et l'action en question effectué;
                return true;
            }
        }
        //Si la touche est déjà enfoncée, on retourne false
        return false;
    }

    private boolean handleKeyUp(int keycode) {
        if (keysPressed.contains(keycode)){
            keysPressed.remove(Integer.valueOf(keycode));
            verifHandleKeyUpManualy();
            //On verifie que en cas de relachement de plusieurs touches en simultané, il n'y ait pas de touches enfoncées qui ne le sont plus
            //Si l'action qu'on vient de désactiver est une action de mouvement/slash...
            if (getOrientationIfWalkingFromKeycode(keycode) != null) {
                //On retire l'orientation de marche de la liste des orientations de marche actives
                activeWalkingOrientations.remove(getOrientationIfWalkingFromKeycode(keycode));
                if (!onGoingAutonomeAction) {
                    finishAction();
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Vérifie si parmis les touches dites enfoncés, il y en a qui ne le sont plus (problème qui survient lors du relachement de plusieurs touches en simultané)
     */
    private void verifHandleKeyUpManualy(){ //Jamais appelé ?
        if(!keysPressed.isEmpty()){
            Iterator<Integer> iterator = keysPressed.iterator();
            while (iterator.hasNext()) {
                int key = iterator.next();
                //System.out.println("key : " + key + " is in keysPressed ?");
                if(!Gdx.input.isKeyPressed(key)){
                    //System.out.println(key + " was suppose to be released");
                    iterator.remove();
                    handleKeyUp(key);
                }
            }
        }
    }

    private void printActiveWalkingOrientations(){
        System.out.println("Active walking orientations : ");
        for (Orientation o : activeWalkingOrientations) {
            System.out.print(o +" ");
        }
        System.out.println("        ----\n----");
    }
    private void printKeyPressed(){
        System.out.println("keys pressed : ");
        for (int o : keysPressed) {
            System.out.print(o +" ");
        }
        System.out.println("        ----\n----");
    }

    private boolean isValidActionKey(int keycode) {

        return validKeys.contains(keycode);
    }

    private void stopActions() {
        onGoingAutonomeAction = false;
        keysPressed.clear();
        activeWalkingOrientations.clear();
        //Si le joueur a un outil en main
        //boolean tool = !player.getToolManager().getCurrentEquippedToolName().equals("");
        //On change l'action du joueur à "Idle"
        player.stateComponant().changeAction("Idle", player.stateComponant().getCurrentOrientation());
    }

    private void handleContinuousAction(int keycode) {
        //Si c'est une action continue (comme marcher), on l'ajoute à la liste des actions continues
        Orientation actionWalkingDirection = getOrientationIfWalkingFromKeycode(keycode);
        //Si le menu actif est le jeu
        if (Game.getInstance().getUpdateManager().getActiveMenu().equals("game")) {
            //Si l'action de marche n'est pas nulle, on l'ajoute à la liste des orientations de marche actives
            if (actionWalkingDirection != null) {
                activeWalkingOrientations.add(actionWalkingDirection);
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return handleKeyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return handleKeyUp(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return handleKeyDown(button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return handleKeyUp(button);
    }

    /**
     * Appelé lors de keyUp s'il n'y a pas d'action statique "en cours" ou par executeStaticAction()
     * dans entity a la fin de l'action.
     * Si la liste activeContinuousActions est vide cela signifie qu'aucune touche de mouvement n'est enfoncée,
     * l'action du joueur est donc mise à null pour arrêter l'action de mouvement.
     * Sinon, l'action du joueur est mise à jour en fonction de la dernière action de mouvement enregistrée dans activeContinuousActions.
     */
    public void finishAction(){
        //boolean tool = !player.getToolManager().getCurrentEquippedToolName().equals("");
        //Si y'a plus rien comme action de mouvement dont la touche est enfoncé on le met a null
        if(activeWalkingOrientations.isEmpty()){
            player.stateComponant().changeAction("Idle", player.stateComponant().getCurrentOrientation());
        }else{//Sinon on met la derniere action (forcement walk) ajouté dans activeActions comme action actuelle
            player.stateComponant().changeAction("Walk", activeWalkingOrientations.get(activeWalkingOrientations.size()-1));
        }
        onGoingAutonomeAction = false;
    }


    public List<Integer> getKeysPressed() {
        return keysPressed;
    }


    public boolean keyTyped(char keycode) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        // Auto-generated method stub
        return false;
    }

    
}

