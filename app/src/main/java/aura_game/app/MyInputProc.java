package aura_game.app;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.PlayableEntity;

/**
 * Singleton garantissant qu'il n'y ait qu'une seule instance de MyInputProc dans l'application
 */
public class MyInputProc implements InputProcessor {

    private static MyInputProc instance;
    /**Liste des keycode actuellement pressée*/
    private List<Integer> keysPressed;
    
    /**Liste des actions continues actuellement pressés : Walk_L, Walk_R, Walk_U, Walk_D
     * Permet de pouvoir passer d'une direction/action à l'autre sans être arrêté par changeAction(null) si on lâche une ancienne touche
     */
    private List<String> activeWalkingDirections;//Stockant seulement les "walk" (L,R,U,D) on se permet de ne stocker que la direction

    /**Si une action instantanée/autonome est en cours*/
    private boolean onGoingStaticAction = false;

    /**Instance unique du player présent dans GameManager*/
    private PlayableEntity player;

    private MyInputProc() {
        // Constructeur privé pour empêcher l'instanciation directe
        Gdx.input.setInputProcessor(this);//gestionnaire d'entrée actif
        this.keysPressed = new ArrayList<>();
        this.activeWalkingDirections = new ArrayList<>();
        player = Game.getInstance().getPlayer();
    }
    
    public static MyInputProc getInstance() {
        if (instance == null) {
            instance = new MyInputProc();
        }
        return instance;
    }


    public void setOnGoingStaticAction(boolean b){
        onGoingStaticAction = b;
    }

    public String getDirectionWalkingFromKeycode(int keycode){//TODO enum Direction
        return switch (keycode) {
            case Input.Keys.LEFT -> "L";
            case Input.Keys.RIGHT -> "R";
            case Input.Keys.UP -> "U";
            case Input.Keys.DOWN -> "D";
            default -> null;
        };
    }

    /**
     * 
     * @param keycode
     * @return True si c'est une action qui s'excutera entièrement en cliquant juste une fois (Slash, Thrust...)
     */
    private boolean IsStaticAction(int keycode) {//TODO: old ?(existe un attribut)
        return switch (keycode) {
            case 0 ->//Slash
                    true;
            case Input.Keys.L -> //Spellcast
                    true;
            case Input.Keys.P ->//Thrust
                    true;
            case 1 ->//Shoot //O
                    true;
            case Input.Keys.SPACE ->//Jump
                    true;
            default -> false;
        };
    }
    
    /**
     * Méthode appelée lorsqu'une touche du clavier est enfoncée.
     * Si la touche n'était pas déjà enfoncée, cette méthode effectue les actions suivantes :
     * - Si la touche E (inventaire) est enfoncée et qu'il y a des actions continues (activeContinuousActions) en cours, 
     *   elle arrête ces actions continues, vide la liste des touches enfoncées (keysPressed) 
     *   et met l'action du joueur à null pour éviter de continuer à bouger lorsque l'inventaire est ouvert.
     * - Si la touche correspond à une action continue (Walk_L, Walk_R, etc.), et que l'inventaire n'est pas ouvert, 
     *   ajoute cette action à la liste activeContinuousActions.
     * - Si la touche correspond à une action statique (Slash, Thrust, etc.), défini onGoingStaticAction sur true
     *   pour empêcher le démarrage d'autres actions continues.
     * Appelle la méthode performAction() de l'InputHandler pour effectuer l'action associée.
     * @param keycode le code de la touche enfoncée
     * @return true si la touche n'était pas déjà enfoncée, sinon false
     */
    @Override
    public boolean keyDown(int keycode) {
    
        if (!keysPressed.contains(keycode)) {
            //Si l'inventaire s'ouvre/se ferme on arrete les actions continues (si jamais elle sont tjr enfoncés)...
            if(keycode == Input.Keys.E && !activeWalkingDirections.isEmpty() ){
                onGoingStaticAction = false;
                keysPressed.clear();
                activeWalkingDirections.clear();
                //============
                boolean tool = false;
                if(player.getCurrentToolName().equals("")){
                        tool = true;
                }
                player.getEntityStateMachine().changeAction("Idle", player.getEntityStateMachine().getCurrentDirectionLetter(),tool);
                //============
            }
            if(!onGoingStaticAction || keycode != Input.Keys.E){
                keysPressed.add(keycode);
                //Si c'est une action continues(Walk_L...) on le met dans activeActions
                String actionWalkingDirection = getDirectionWalkingFromKeycode(keycode);
                if(Game.getInstance().getUpdateManager().activeMenu().equals("game")){
                    if(actionWalkingDirection!=null){
                        activeWalkingDirections.add(actionWalkingDirection);
                    }
                    if(IsStaticAction(keycode)){
                        onGoingStaticAction = true;   
                    }
                }
                System.out.println("keycode : " + keycode);
                Game.getInstance().getInputHandler().performAction(keycode);
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode appelée lorsqu'une touche du clavier est relâchée.
     * Supprime la touche de la liste des touches enfoncées (keysPressed).
     * Si l'action associée à la touche relâchée était une action continue/de mouvement (Walk_L, Walk_R, etc.), 
     * elle est retirée de la liste activeContinuousActions.
     * S'il n'y a pas d'action statique en cours, on lance finishAction().
     * @param keycode le code de la touche relâchée
     * @return true 
     */
    @Override
    public boolean keyUp(int keycode) {
        keysPressed.remove(Integer.valueOf(keycode));
        //Si l'action qu'on vient de désactiver est une action de mouvement/slash
        String actionWalking = getDirectionWalkingFromKeycode(keycode);
        if(actionWalking!=null) {
            activeWalkingDirections.remove(actionWalking);
        }
        //Si il n'y a pas d'action statique en cours
        if(!onGoingStaticAction){
            finishAction();
        }
        return true;
    }

    /**
     * Appelé lors de keyUp s'il n'y a pas d'action statique "en cours" ou par executeStaticAction()
     * dans entity a la fin de l'action.
     * Si la liste activeContinuousActions est vide cela signifie qu'aucune touche de mouvement n'est enfoncée,
     * l'action du joueur est donc mise à null pour arrêter l'action de mouvement.
     * Sinon, l'action du joueur est mise à jour en fonction de la dernière action de mouvement enregistrée dans activeContinuousActions.
     */
    public void finishAction(){
        //=================
        boolean tool = false;
        if(!player.getCurrentToolName().equals("")){
            tool = true;
        }
        //Si y'a plus rien comme action de mouvement dont la touche est enfoncé on le met a null
        if(activeWalkingDirections.isEmpty()){
            player.getEntityStateMachine().changeAction("Idle", player.getEntityStateMachine().getCurrentDirectionLetter(), tool);
        }else{//Sinon on met la derniere action (forcement walk) ajouté dans activeActions comme action actuelle
            player.getEntityStateMachine().changeAction("Walk", activeWalkingDirections.get(activeWalkingDirections.size()-1), tool);
        }
        //=================
        onGoingStaticAction = false;
    }

    public boolean keyTyped(char keycode) {

        return false;
    }

    /**
     * Méthode qui est appelée lorsqu'un bouton de la souris est enfoncé.
     *
     * @param screenX La position horizontale du curseur de la souris.
     * @param screenY La position verticale du curseur de la souris.
     * @param pointer L'index du pointeur associé à l'événement (pour les écrans tactiles).
     * @param button Le bouton de la souris enfoncé.
     * @return true si le bouton est enfoncé, false sinon.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Si le bouton enfoncé n'est pas déjà dans la liste des boutons enfoncés, on l'ajoute.
        if (!keysPressed.contains(button)) {
            // Si le bouton enfoncé est la touche E et qu'il y a des actions continues en cours, on arrête ces actions.
            if (button == Input.Keys.E && !activeWalkingDirections.isEmpty()) {
                onGoingStaticAction = false;
                keysPressed.clear();
                activeWalkingDirections.clear();
                //============
                boolean tool = false;
                if(player.getCurrentToolName().equals("")){
                    tool = true;
                }
                player.getEntityStateMachine().changeAction("Idle", player.getEntityStateMachine().getCurrentDirectionLetter(),tool);
                //============
            }
            // Si aucune action continue n'est en cours ou si le bouton enfoncé est la touche E, on ajoute le bouton à la liste des boutons enfoncés.
            if (!onGoingStaticAction || button != Input.Keys.E) {
                keysPressed.add(button);
                // Si le bouton enfoncé correspond à une action continue (comme Walk_L), on ajoute cette action à la liste des actions continues.
                String actionWalking = getDirectionWalkingFromKeycode(button);
                if (Game.getInstance().getUpdateManager().activeMenu().equals("game")) {
                    if (actionWalking != null) {
                        activeWalkingDirections.add(actionWalking);
                    }
                    // Si le bouton enfoncé correspond à une action statique (comme Jump), on met à jour l'état `onGoingStaticAction` pour indiquer qu'une action statique est en cours.
                    if (IsStaticAction(button)) {
                        onGoingStaticAction = true;
                    }
                }
                // On appelle la méthode `performAction` du gestionnaire d'entrée pour effectuer l'action associée au bouton enfoncé.
                Game.getInstance().getInputHandler().performAction(button);
                // On retourne true pour indiquer que le bouton a été enfoncé.
                return true;
            }
        }
        // Si le bouton enfoncé est déjà dans la liste des boutons enfoncés, on retourne false pour indiquer qu'il n'a pas été enfoncé.
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        keysPressed.remove(Integer.valueOf(button));
        //Si l'action qu'on vient de désactiver est une action de mouvement/slash
        String actionWalking = getDirectionWalkingFromKeycode(button);
        if(actionWalking!=null) {
            activeWalkingDirections.remove(actionWalking);
        }
        //Si il n'y a pas d'action statique en cours on fini l'action
        if(!onGoingStaticAction){
            finishAction();
        }

        return true;
    }











    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'touchDragged'");
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'scrolled'");
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        // Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'touchCancelled'");
    }

    
}

