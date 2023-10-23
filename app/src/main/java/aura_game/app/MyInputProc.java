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
    /**Liste des keycode actuellement pressé*/
    private List<Integer> keysPressed;
    /**Liste des actions continues(Walk_L...) actuellements pressés 
     * Permet de pouvoir passer d'une direction/action à l'autre sans etre arreté par changeAction(null) si on lache une ancienne touche
     */
    private List<String> activeContinuousActions;

    /**Si une action instantané/autonome est en cours*/
    private boolean onGoingStaticAction = false;

    /**Instance unique du player présent dans GameManager*/
    private PlayableEntity player;


    //TODO: CHANGER SLASH,Thrust... (tjr changeAction...) mais qui sont activé et ce font 1 fois dès le clique, pu "tant que c'est cliquer"


    private MyInputProc() {
        // Constructeur privé pour empêcher l'instanciation directe
        Gdx.input.setInputProcessor(this);//gestionnaire d'entrée actif
        this.keysPressed = new ArrayList<>();
        this.activeContinuousActions = new ArrayList<>();
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

    public String getNameActionWalkingFromKeycode(int keycode){
        switch(keycode){
            case Input.Keys.LEFT :
                return "Walk_L";
            case Input.Keys.RIGHT:
                return "Walk_R";
            case Input.Keys.UP:
                return "Walk_U";
            case Input.Keys.DOWN:
                return "Walk_D";
            default:
                return null;
        }
    }
//TODO: action déclenché (thrust,spellcast) et action instantanée(slash, shoot)
    /**
     * 
     * @param keycode
     * @return True si c'est une action qui s'excutera entièrement en cliquant juste une fois (SLash,Thrust...)
     */
    private boolean IsStaticAction(int keycode) {
        switch(keycode){   
            case Input.Keys.K://Slash
                return true;
            case Input.Keys.L: //Spellcast
                return true;
            case Input.Keys.P://Thrust
                return true;
            case Input.Keys.O://Shoot
                return true;
            default:
                return false;

        }
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
            if(keycode == Input.Keys.E && !activeContinuousActions.isEmpty() ){
                onGoingStaticAction = false;
                keysPressed.clear();
                activeContinuousActions.clear();
               player.changeAction(null);
            }
            if(!onGoingStaticAction || keycode == Input.Keys.E){
                keysPressed.add(keycode);
                //Si c'est une action continues(Walk_L...) on le met dans activeActions
                String actionWalking = getNameActionWalkingFromKeycode(keycode);
                if(!Game.getInstance().getUpdateManager().activeMenu().equals("game")){
                    if(actionWalking!=null){
                        activeContinuousActions.add(actionWalking);
                    }
                    if(IsStaticAction(keycode)){
                        onGoingStaticAction = true;   
                    }
                }
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
     * Si il n'y a pas d'action statique en cours, on lance finishAction().
     * @param keycode le code de la touche relâchée
     * @return true 
     */
    @Override
    public boolean keyUp(int keycode) {
        keysPressed.remove(Integer.valueOf(keycode));
        //Si l'action qu'on vient de désactiver est une action de mouvement/slash
        String actionWalking = getNameActionWalkingFromKeycode(keycode);
        if(actionWalking!=null){
            activeContinuousActions.remove(actionWalking);
            //Si il n'y a pas d'action statique en cours
            if(!onGoingStaticAction){
                finishAction();
            }
        }
        return true;
    }

    /**
     * Appelé lors de keyUp si il ni a pas d'action statique "en cours" ou par executeStaticAction() 
     * dans entity a la fin de l'action.
     * Si la liste activeContinuousActions est vide, 
     * cela signifie qu'aucune touche de mouvement n'est enfoncée, 
     * l'action du joueur est donc mise à null pour arrêter l'action de mouvement.
     * Sinon, l'action du joueur est mise à jour en fonction de la dernière action de mouvement enregistrée dans activeContinuousActions.
     */
    public void finishAction(){
        //Si y'a plus rien comme action de mouvement dont la touche est enfoncé on le met a null
        if(activeContinuousActions.isEmpty()){
            player.changeAction(null);
        }else{//Sinon on met la derniere action ajouté dans activeActions comme action actuelle
            player.changeAction(activeContinuousActions.get(activeContinuousActions.size()-1));
        }
    }

    public boolean keyTyped(char keycode) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'touchDown'");
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'touchUp'");
        return false;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'touchCancelled'");
    }

    
}

