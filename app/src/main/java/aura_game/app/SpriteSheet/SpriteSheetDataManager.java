package aura_game.app.SpriteSheet;
import java.util.HashMap;
import java.util.Map;

import aura_game.app.Type.EntityType;

public class SpriteSheetDataManager {
    /**Permet à chaque instance d'entité du même type d'accéder à la même instance de SpriteSheetData associée à ce type d'entité*/
    private static HashMap<String, SpriteSheetData> spriteSheetDataMap = new HashMap<>();

    private SpriteSheetDataManager() {
        //Constructeur privé pour empêcher l'instanciation directe
    }


    /** Cette méthode vérifie si une instance de SpriteSheetData existe déjà pour ce type d'entité dans la HashMap, 
     * et si c'est le cas, elle la retournera. Sinon, elle créera une nouvelle instance de SpriteSheetData, 
     * l'ajoutera à la HashMap et la renverra.
     * @param entityType type d'entité pour lequel on veut avoir une instance
     */
    public static SpriteSheetData getSpriteSheetDataInstance(EntityType entityType) {
        if (spriteSheetDataMap.containsKey(entityType.getName())) {
            // Si les données de sprite existent déjà pour le type d'entité, les retourner
            return spriteSheetDataMap.get(entityType.getName());
        } else {
            // Sinon, créer de nouvelles données de sprite pour le type d'entité et les ajouter à la HashMap, initialisé aux valeurs du type
            SpriteSheetData spriteSheetData = new SpriteSheetData(createActionDataMap(entityType));
            //On rajoute donc ce nouveau type a la spriteSheetDataMap
            spriteSheetDataMap.put(entityType.getName(), spriteSheetData);
            return spriteSheetData;
        }
    }

    /**
     * Permet de créer la hashMap avec les données d'actions pour l'entité Type. Appelé par createSpriteSheetDataForEntityType
     * @param entType le type entite possédant actions tableau int[] de EntitySpriteSheet permettant de remplir la hashMap
     * et tableau String[] de EntitySpriteSheet permettant de remplir le nom dans la hashMap
     * @return la hashMap
     */
    private static Map<String, int[]> createActionDataMap(EntityType entType ) {
        Map<String, int[]> map = new HashMap<>();
        String[] actionsName = entType.spriteSheetActionsName();
        int[] actions = entType.spriteSheetXEnd();
        int numActions = actionsName.length; // Nombre d'actions

        for (int i = 0; i < numActions; i++) {

            String actionName = actionsName[i];
            int endSpriteX = actions[i];
            int spriteY = i;

            map.put(actionName, new int[]{endSpriteX, spriteY});
        }
        return map;
    }


    // Autres méthodes et fonctionnalités de gestion des données de sprite
}
