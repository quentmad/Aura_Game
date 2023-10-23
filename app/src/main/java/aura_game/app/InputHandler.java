package aura_game.app;

import java.util.HashMap;

/**
 * Cette classe contient une HashMap<Integer, Runnable> qui permet de relier une touche (key) à une méthode/fonction 
 * à executer. Elle permet de gérer les actions à effectuer en cas de pression sur une touche.
 * Singleton garantissant qu'il n'y ait qu'une seule instance de InputHandler dans l'application 
 */
public class InputHandler {

    private static InputHandler instance;

    private HashMap<Integer, Runnable> keyToActionMap;
    /**
     * Hashmap reliant le keycode avec l'action/méthode a lancé
     */
    private InputHandler() {
        keyToActionMap = new HashMap<>();
    }

    public static InputHandler getInstance() {
        if (instance == null) {
            instance = new InputHandler();
        }
        return instance;
    }

    /**Méthode pour associer un code de touche à une fonction*/
    public void mapKeyToAction(int keyCode, Runnable action) {
        keyToActionMap.put(keyCode, action);
    }

    /**Méthode pour exécuter la fonction associée à un code de touche*/
    public void performAction(int keyCode) {
        Runnable action = keyToActionMap.get(keyCode);
        if (action != null) {
            action.run();
        }
    }
}

