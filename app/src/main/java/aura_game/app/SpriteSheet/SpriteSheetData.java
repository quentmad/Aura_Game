package aura_game.app.SpriteSheet;


import java.util.Map;
/**
 * Cette classe permet d'avoir accès aux élements de spriteSheet d'une action
 */
public class SpriteSheetData {

    private final Map<String, int[]> spriteDataMap;
    
    public SpriteSheetData(Map<String, int[]> spriteDataMap) {
        this.spriteDataMap = spriteDataMap;
    }

    /**
     * Permet de récupérer les élements de spriteSheet de l'action et ainsi définir l'action actuelle de l'entité
     * @param action l'action auquel on souhaite avoir les informations
     * @return Tableau d'entier unique à chaque type d'entité de la forme {startSpriteX, endSpriteX, spriteY} de l'action souhaité*/
    public int[] getTabofAction(String action){
        return spriteDataMap.getOrDefault(action, null);
    }


}
