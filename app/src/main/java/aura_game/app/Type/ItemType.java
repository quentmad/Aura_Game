package aura_game.app.Type;
/*Add:
 *Description
 * loots (si détruit) (/!\ détruit c'est avant recup)
 * action liés (poser,manger... : comment faire ? ou alors on donne le type: arme/outil/nourriture/objet  autre)
 * le type -> Actions possible / propriété:solidité/dégats/max stackable
 * audio (a quel moment?)
 */

import java.util.Arrays;
import java.util.List;

import aura_game.app.Util.Triplet;

//TODO reflechir comment faire pour snow... car color n'est pas stocker dans ITEM (juste cherché pendant le construct)
public enum ItemType {//TODO: loots (arbre: branche,bois, feuilles) liste d'id ?
    ash_tree("ash_tree",128,128,9,82,58, //1
    new float[]{43,26, 53,26, 60,32, 69,32, 76,26, 86,26,
                86,15, 77,15, 68,9, 61,9, 52,15, 43,15}, new String[]{"verydark_green","dark_green","light_green","verylight_green"},
    Arrays.asList(
                new Triplet<>("stick", 0, 4),new Triplet<>("branch", 0, 1),new Triplet<>("leaf", 1, 4),new Triplet<>("apple", 0, 2)
                )
    ),

    banyan_1_tree("banyan_1_tree", 128,96, 7,70,61, //2
    new float[]{63,7, 63,9, 59,9, 59,11, 53,11, 53,13, 43,13, 43,24, 53,24, 53,26, 58,26, 61,30,
                68,30, 71,26, 76,26, 76,24, 86,24, 86,13, 76,13, 76,11, 70,11, 70,9, 66,9, 66,7},new String[]{"verydark_green","dark_green","light_green","verylight_green"},
    Arrays.asList(
                new Triplet<>("stick", 0, 4), new Triplet<>("branch", 1, 2),new Triplet<>("leaf", 1, 3),new Triplet<>("apple", 0, 1)
                )
    ),

    banyan_2_tree("banyan_2_tree", 128,96, 9,70,68, //3
    new float[]{60,11, 60,9, 55,9, 51,13, 45,13, 45,26, 51,26, 55,30, 60,30, 60,28,
                69,28, 69,30, 74,30, 78,26, 84,26, 84,13, 78,13, 74,9, 69,9, 69,11},new String[]{"verydark_green","dark_green","light_green","verylight_green"},
    Arrays.asList(
                new Triplet<>("stick", 0, 4), new Triplet<>("branch", 1, 2),new Triplet<>("leaf", 1, 3)
                )
    ),

    banyan_ball_tree("banyan_ball_tree",128,160, 13,85,58, //4
    new float[]{56,15, 56,13, 52,13, 39,21, 39,29, 33,29, 29,33, 29,36, 31,36, 31,40, 46,48, 56,48, 56,46,
                69,46, 69,48, 79,48, 94,40, 94,36, 96,36, 96,33, 92,29, 86,29, 86,21, 73,13, 69,13, 69,15},
    new String[]{"verydark_green","dark_green","light_green","verylight_green"},
    Arrays.asList(
                new Triplet<>("stick", 1, 4), new Triplet<>("branch", 1, 2),new Triplet<>("leaf", 1, 4)
                )
    ),

    clear_trunk_little_tree("clear_trunk_little_tree",64,32,1,9,28, //6
    new float[]{21,16, 25,18, 28,18, 28,16, 37,16, 37,18, 40,18, 44,16,
                46,8, 44,3, 40,3, 40,1, 37,1, 37,3,
                28,3, 28,1, 25,1, 25,3, 21,3, 19,8},new String[]{},
    Arrays.asList(
                new Triplet<>("stick", 2, 4)
                )
    ),

    clear_trunk_tree("clear_trunk_tree",64,64, 7,11,54,//5
    new float[]{13,15, 5,20, 5,22, 7,22, 7,26, 3,30, 3,32,      16,44, 22,44, 27,40, 38,40, 43,44, 49,44,
                62,32, 62,30, 58,26, 58,22, 60,22, 60,20, 55,15, 52,15, 52,9, 50,9, 50,7, 44,7, 38,11, 27,11, 21,7, 15,7, 15,9, 13,9},
                new String[]{},
    Arrays.asList(
                new Triplet<>("stick", 1, 3)
                )
    ),

    fir_1_tree("fir_1_tree",128,160,15,110,124,  //7
    new float[]{61,15, 55,23, 43,27, 43,32, 55,32, 61,36,
                66,36, 72,32, 84,32, 84,27, 72,23, 66,15},
    new String[]{"verydark_green","dark_green","light_green","verylight_green"},
    Arrays.asList(
                new Triplet<>("stick", 1, 4), new Triplet<>("branch", 0, 1),new Triplet<>("leaf", 0, 3),new Triplet<>("apple", 0, 2)
                )
    ),

    fir_2_tree("fir_2_tree",128,96,9,72,88, //8
    new float[]{61,9, 53,15, 47,15, 47,17, 41,17, 41,19, 39,19, 39,24, 47,30, 53,30, 59,36,
                70,36, 76,30, 82,30, 90,24, 90,19, 88,19, 88,17, 82,17, 82,15, 76,15, 68,9},
    new String[]{"verydark_green","dark_green","light_green","verylight_green"},
    Arrays.asList(
                new Triplet<>("stick", 1, 4), new Triplet<>("branch", 1, 2),new Triplet<>("leaf", 1, 4),new Triplet<>("apple", 0, 2)
                )
    ),

    fir_dead_tree("fir_dead_tree",128,128,3,110,124, //9
    new float[]{61,3, 55,11, 43,15, 43,20, 55,20, 61,24,
                66,24, 72,20, 84,20, 84,15, 72,11, 66,3},new String[]{},//TODO enlever rock ...
    Arrays.asList(
                new Triplet<>("stick", 6, 8),new Triplet<>("branch", 0, 2)
                )
    ),

    japanese_maple_tree("japanese_maple_tree",128,128,17,65,96, //10
    new float[]{61,17, 55,25, 49,25, 49,27, 45,27, 45,29, 50,34, 57,34, 62,38, 67,38,
                72,34, 79,34, 84,29,  84,27, 80,27, 80,25, 74,25, 68,17 },
    new String[]{"verydark_green","dark_green","light_green","verylight_green","orange_green","orange","red"},
    Arrays.asList(
                new Triplet<>("stick", 0, 3), new Triplet<>("branch", 0, 2),new Triplet<>("leaf", 0, 3),new Triplet<>("apple", 0, 1)
                )
    ),
                
    liana_tree("liana_tree",192,256, 39,200,214, //11
    new float[]{73,55, 81,58, 87,59, 93,60, 100,60, 112,56,
                112,50, 100,39, 93,39, 90,43, 81,43, 73,49},new String[]{"verydark_green","dark_green","light_green"},
    Arrays.asList(
                new Triplet<>("stick", 2, 8), new Triplet<>("branch", 2, 5),new Triplet<>("leaf", 0, 4)
                )
    ),

    oak_tree("oak_tree", 128,160 ,27,100,116, //12
    new float[]{45,39, 49,44, 59,44, 59,46, 64,46, 64,44, 74,44,
                80,37, 80,34, 76,29, 64,29, 64,27, 59,27, 59,29, 49,29, 45,36},
    new String[]{"verydark_green","dark_green","light_green","verylight_green"},
    Arrays.asList(
                new Triplet<>("stick", 0, 3), new Triplet<>("branch", 1, 2),new Triplet<>("leaf", 1, 4),new Triplet<>("apple", 0, 2)
                )
    ),

    spruce_tree("spruce_tree",128,160, 27,104,114, //13
    new float[]{62,29, 62,27, 57,27, 54,33, 49,33, 47,35, 47,38, 49,40, 55,40, 55,44, 57,46, 62,46,62,44,
                67,44, 67,46, 72,46, 74,44, 74,40, 80,40, 82,38, 82,35, 80,33, 75,33, 72,27, 67,27, 67,29},
    new String[]{"verydark_green","dark_green","light_green","verylight_green"}, 
    Arrays.asList(
                new Triplet<>("stick", 0, 3), new Triplet<>("branch", 1, 1),new Triplet<>("leaf", 1, 3)
                )
    ),
    boulder("boulder", 64,64 ,1,25,60, //14
    new float[]{11,13, 11,24, 15,24, 15,30, 19,34, 30,34, 30,32, 46,32, 46,30, 54,30, 62,24, 62,15,
                58,13, 57,7, 54,3, 43,3, 43,5, 27,5, 27,7, 19,7},
    new String[]{},
    Arrays.asList(
                new Triplet<>("rock", 5, 10)
                )
    ),

    boulder_little("boulder_little", 32,32 ,1,10,17, //15
    new float[]{3,9, 3,12, 9,18, 15,20, 26,20, 32,16, 32,13, 20,3, 13,3},
    new String[]{},
    Arrays.asList(
                new Triplet<>("rock", 1, 5)
                )
    );




    private String name;
    /**Le chemin d'accès à la texture (sans ombre) de l'item.*/
    private final String texturePath;
    /**Premier pixel en y par rapport au bas de l'image */
    /**Largeur de la texture complète (image complete)*/
    private final int textureWidth;
    /**Hauteur de la texture complète (image complete)*/
    private final int textureHeight;
    /**La différence entre le bas de l'image et le 1e pixel de l'item(en y)(non ombre)*/
    private final int offY;
    private final int tall;
    /**Hauteur du rectangle hitbox (sans vu isometrique (nombre de pixels en Y non transp/ombre bas))*/
    private final int hitboxHeight;
    private final float[] listPolygon;
    /**Liste des couleurs possible pour l'item(green...) */
    private String[] listColor;
    private List<Triplet<String, Integer, Integer>> deathLoots;

    /**
     * @param itemName le nom de l'item
     * @param textureW largeur de l'image (equivaut a spriteWidth pour entity)
     * @param textureH hauteur de l'image (equivaut a spriteHeight pour entity)
     * @param offY La différence entre le bas de l'image et le 1e pixel de l'item(en y)(non ombre)
     * @param tall La taille de l'item
     * @param hitboxHeight
     * @param listPolygon le tableau des points du polygon hitbox de l'item
     * @param listColor le tableau des couleurs possible pour l'item(green...)
     * @param deathLoots la liste des loots possible avec leur intervale min-max
     */
    ItemType(String itemName,int textureW, int textureH, int offY, int tall,int hitboxHeight, float[] listPolygon, String[] listColor, List<Triplet<String, Integer, Integer>> deathLoots) {
        this.texturePath = "src/main/resources/items/" + itemName;
        //this.shadowTexturePath = "src/main/resources/items/" + itemName + "_S.png";
        //this.idItem = idItem;
        this.textureWidth = textureW;
        this.textureHeight = textureH;
        this.offY = offY;
        this.tall = tall;
        this.listPolygon = listPolygon;
        this.name = itemName;
        this.hitboxHeight = hitboxHeight;
        this.listColor = listColor;
        this.deathLoots = deathLoots;
        
    }


    public String getName(){
        return name;
    }

    public String texturePath() {
        return texturePath;
    }
    
    public int offY() {
        return offY;
    }

    public int textureWidth() {
        return textureWidth;
    }

    public int textureHeight() {
        return textureHeight;
    }

    public int tall(){
        return tall;
    }

    /**Tous les pixels en Y après offY/ombre */
    public int hitboxHeight(){
        return tall;
    }


    public float[] listPolygon(){
        return listPolygon;
    }

    public String[] listColor(){
        return listColor;
    }

    public List<Triplet<String, Integer, Integer>> deathLoots(){
        return deathLoots;
    }

    /**Permet d'avoir le nom du fichier de l'image de l'item à partir de la couleur souhaité.
     * Verifie si la couleur souhaité existe pour l'itemType en question
     * @param color
     * @return Si Color existe retourne nomType+color sinon met un message d'erreur et retourne null
     */
    public String getFullNameFileFromColor(String color){
        if(!color.equals("default")){
            for(String col : listColor){
                //System.out.println("colo"+col);
                if(col.equals(color)){
                    //System.out.println(texturePath+"-"+col+".png");
                    return texturePath+"-"+col+".png";
                }
            }
            System.out.println("Error during getFullNameFilFromColor, the color "+ color+ " doesn't exist for " + name );
            return null;
        }else{
            return texturePath+".png";
        }
    }

}
