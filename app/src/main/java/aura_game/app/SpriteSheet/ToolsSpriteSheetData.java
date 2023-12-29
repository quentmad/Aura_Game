package aura_game.app.SpriteSheet;
import java.util.HashMap;


/**
 * Singleton, permet de gérer les spritesheets des armes et outils d'un LPC, qu'on peut souhaiter afficher.
 */
public class ToolsSpriteSheetData {

    /**
     * La classe est un singleton, ce qui oblige à n'avoir qu'un seul exemplaire de ToolsSpriteSheetData.
     * Ainsi, tout le monde accédera à la meme instance
     */
    private static ToolsSpriteSheetData instance;

    private String[] tools;
    private Act_Sz[][] actions;
    
    /**
     * Name (ex: Bow -> HashmapAction de Bow -> spriteY, sizeSprite (64 / 128 / 192)
     */
    private HashMap<String, HashMap<String, int[]>> spriteDataMap;

    /**
     * Privé pour respecter l'unicité d'instance. Rempli directement le hashmap
     */
    private ToolsSpriteSheetData() {
        this.spriteDataMap = new HashMap<>();//TODO maj anglais minuscule
        this.tools = new String[]{"hache", "marteau","pioche","pelle","flail","hache_de_guerre","mace", 
            "simple_staff","diamond_staff","gnarled_staff","loop_staff",
            "longue_lance","longue_lance_alt","magic","magic_alt","trident",  "spear_metal","spear_wooden",
            "arc_normal","arc_great","fleche","arbalette",  "bouclier_crusader_blue","bouclier_crusader_red","bouclier_kite_gray_blue","bouclier_spartiate",
            "longue_epee","katana","sabre","rappier","poignard"};

        this.actions = new Act_Sz[][]{
            { new Act_Sz("Slash",128) }/*Hache*/, { new Act_Sz("Slash",128) }/*Marteau*/, { new Act_Sz("Slash",128) }/*Pioche*/,
            { new Act_Sz("Thrust",64),new Act_Sz("Walk",64) }/*Pelle*/, { new Act_Sz("Walk",64),new Act_Sz("Hurt",64),new Act_Sz("Slash",128) }/*Flail*/,
            { new Act_Sz("Walk",64),new Act_Sz("Hurt",64),new Act_Sz("Slash",128) }/*Hache_de_guerre*/, { new Act_Sz("Walk",64),new Act_Sz("Hurt",64),new Act_Sz("Slash",128) }/*Mace*/,
            { new Act_Sz("Spellcast",64),new Act_Sz("Thrust",64),new Act_Sz("Walk",64),new Act_Sz("Slash",64), new Act_Sz("Hurt",64)  }/*Simple_staff*/,
            { new Act_Sz("Walk",64),new Act_Sz("Hurt",64),new Act_Sz("Thrust",128) }/*Diamond_staff*/, { new Act_Sz("Walk",64),new Act_Sz("Hurt",64),new Act_Sz("Thrust",128) }/*Gnarled_staff*/,
            { new Act_Sz("Walk",64),new Act_Sz("Hurt",64),new Act_Sz("Thrust",128) }/*Loop_staff*/, { new Act_Sz("Thrust",192)}/*Longue_lance*/, { new Act_Sz("Thrust",192)}/*Longue_lance_alt*/,
            { new Act_Sz("Thrust",192)}/*Magic*/, { new Act_Sz("Thrust",192)}/*Magic_alt*/, { new Act_Sz("Thrust",192)}/*Trident*/, { new Act_Sz("Thrust",64),new Act_Sz("Walk",64)}/*Spear_metal*/, 
            { new Act_Sz("Thrust",64),new Act_Sz("Walk",64)}/*Spear_wooden*/, { new Act_Sz("Shoot",64),new Act_Sz("Walk",128)}/*Arc_normal*/,  { new Act_Sz("Shoot",64),new Act_Sz("Walk",128)}/*Arc_great*/,
            { new Act_Sz("Shoot",64)}/*Fleche*/, { new Act_Sz("Thrust",64),new Act_Sz("Walk",64)}/*Arbalette*/, { new Act_Sz("all",64)}/*Bouclier_crusader_blue*/, { new Act_Sz("all",64)}/*Bouclier_crusader_red*/,
            { new Act_Sz("Thrust",64),new Act_Sz("Walk",64),new Act_Sz("Slash",64)}/*Bouclier_kite_gray_blue*/, { new Act_Sz("all",64)}/*Bouclier_spartiate*/, { new Act_Sz("Walk",128),new Act_Sz("Slash",128)}/*Longue_epee*/,
            { new Act_Sz("Walk",128),new Act_Sz("Slash",128)}/*Katana*/, { new Act_Sz("Walk",64),new Act_Sz("Hurt",64),new Act_Sz("Slash",128)}/*Sabre*/, { new Act_Sz("Walk",64),new Act_Sz("Hurt",64),new Act_Sz("Slash",192)}/*Rappier*/,
            { new Act_Sz("Thrust",64),new Act_Sz("Walk",64),new Act_Sz("Slash",64),new Act_Sz("Hurt",64),}/*Poignard*/
        };

        insertIntoToolsDataMap();
    }

    //Accesseur statique pour obtenir l'instance unique
    public static ToolsSpriteSheetData getInstance() {
        if (instance == null) {
            instance = new ToolsSpriteSheetData();
        }
        return instance;
    }


    
    public String[] getTools() {
        return tools;
    }

    /**
     * Permet de trouver la position de l'outil dans le tableau (pour definir ensuite currentTool...)
     * @param tool l'outil dont on cherche la position
     * @return la position de tool ou -1 si non trouvé
     */
    public int getToolIndex(String tool){
        for(int i = 0; i<tools.length; i++){
            if(tools[i].equals(tool)){
                return i;
            }
        }
        return -1;
    }

    public Act_Sz[][] getActions() {
        return actions;
    }

    /**
     * À partir du tableau de Tools et tableau 2d d'actions (sans direction), insert dans la hashmap les actions
     * (avec direction) ainsi que la spriteY qui est déduit de la position dans actions[][], ainsi que la size de Sprite, 
     * Pour connaitre SpriteY: On ajoute un à chaque fois. Il y en a 3 différents cars 3 spritesSheet différents (64,128,192)
     * permettant par la suite de savoir où placer l'image, et quelle sprite entre 64 128 et 192 afficher
     */
    private void insertIntoToolsDataMap() {        
        //Remplir la HashMap à partir des tableaux
        //Pour tous les outils/armes
        int y64 = -1; int y128 = -1; int y192 = -1;
        for (int i = 0; i < tools.length; i++) {
            HashMap<String, int[]> actionDataMap = new HashMap<>();
            for (int j = 0; j < actions[i].length; j++) {
                int size = actions[i][j].taille();
                String[]actionsWithDir = getCompleteActions(actions[i][j].action());
                //Pour cette action dans les différentes direction
                for(int k=0; k < actionsWithDir.length; k++ ){
                    int spriteY = -1;
                    switch(size){
                        case 64:
                            y64++;
                            spriteY = y64;
                            break;
                        case 128:
                            y128++;
                            spriteY = y128;
                            break;
                        case 192:
                            y192++;
                            spriteY = y192;
                            break;
                    }
                    int beginX =0;
                    //System.out.println(actionsWithDir[k] +",Y:"+spriteY + ", size:"+size);
                    //Si on a un Tool et qu'on est en Slash alors le début est le 3e sprite.
                    //Ainsi, le sprite de debut n'est pas 0 pour tous...
                    if((tools[i].equals("Hache") || tools[i].equals("Marteau") || tools[i].equals("Pioche")) && (actions[i][j].action().equals("Slash"))){
                        //System.out.println(actionsWithDir[k] +",Y:"+spriteY + ", size:"+size + " 2 "+tools[i]);
                        beginX = 2;
                    }           
                        actionDataMap.put(actionsWithDir[k], new int[]{spriteY, size, beginX});
                }
            }
            spriteDataMap.put(tools[i], actionDataMap);
        }
    }

    /**
     * Obtient les informations de l'action spécifiée pour l'outil donné.
     * @param tool L'outil pour lequel on veut récupérer les informations.
     * @param action L'action pour laquelle on veut récupérer les informations.
     * @return Les informations associées à l'action pour l'outil donné, ou -1 si l'action n'existe pas pour l'outil.
     * Dans le cas où l'outil n'existe carrément pas
     */
    
    public int[] getActionInfo(String tool, String action) {
        HashMap<String, int[]> actionDataMap = spriteDataMap.get(tool);
        if (actionDataMap != null) {
            return actionDataMap.getOrDefault(action, null);
        }
        throw new IllegalArgumentException("L'outil specifie n'existe pas dans la spriteDataMap.");   
    }
     
    /**
     * Cette méthode prend une action (par exemple Walk) 
     * @return toutes les actions avec la direction (Walk_U, Walk_D...)
     */
    private String[] getCompleteActions(String action){
        return switch (action) {
            case "Spellcast", "Thrust", "Walk", "Slash", "Shoot" ->
                    new String[]{action + "_U", action + "_L", action + "_D", action + "_R"};
            case "Hurt" -> new String[]{action + "_D"};
            case "all" -> new String[]{
                    "SpellCast_U", "SpellCast_L", "SpellCast_D", "SpellCast_R", "Thrust_U", "Thrust_L", "Thrust_D", "Thrust_R", "Walk_U", "Walk_L", "Walk_D", "Walk_R",
                    "Slash_U", "Slash_L", "Slash_D", "Slash_R", "Shoot_U", "Shoot_L", "Shoot_D", "Shoot_R", "Hurt_D"};
            default -> null;
        };
    }
      
}

