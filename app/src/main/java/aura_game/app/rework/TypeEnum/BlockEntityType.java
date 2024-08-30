package aura_game.app.rework.TypeEnum;

import aura_game.app.Util.Triplet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.List;
//Anciennement "Item"
public enum BlockEntityType {
    ash_tree(128,128,-1,-1, 9, 6, 82,
            Arrays.asList(
                    new Triplet<>("stick", 0, 4),new Triplet<>("branch", 0, 1),new Triplet<>("leaf", 1, 4),new Triplet<>("apple", 0, 2)
            ), 58,
    new float[]{43,26, 53,26, 60,32, 69,32, 76,26, 86,26, 86,15, 77,15, 68,9, 61,9, 52,15, 43,15}, true, "tree",false,false,  new String[]{"verydark_green","dark_green","light_green","verylight_green"}, "Un arbre de cendre"),

    banyan_1_tree(128,96,-1,-1, 7,8, 70,
            Arrays.asList(
                    new Triplet<>("stick", 0, 4),new Triplet<>("branch", 1, 2),new Triplet<>("leaf", 1, 3),new Triplet<>("apple", 0, 1)
            ), 61,
            new float[]{63,7, 63,9, 59,9, 59,11, 53,11, 53,13, 43,13, 43,24, 53,24, 53,26, 58,26, 61,30,
            68,30, 71,26, 76,26, 76,24, 86,24, 86,13, 76,13, 76,11, 70,11, 70,9, 66,9, 66,7}, true, "tree",false, false, new String[]{"verydark_green","dark_green","light_green","verylight_green"}, "Un arbre de banyan"),

    banyan_2_tree(128,96,-1,-1,9,8,70,
            Arrays.asList(
                    new Triplet<>("stick", 0, 4), new Triplet<>("branch", 1, 2),new Triplet<>("leaf", 1, 3)
            ), 68,
            new float[]{60,11, 60,9, 55,9, 51,13, 45,13, 45,26, 51,26, 55,30, 60,30, 60,28,
            69,28, 69,30, 74,30, 78,26, 84,26, 84,13, 78,13, 74,9, 69,9, 69,11}, true,"tree", false,false,  new String[]{"verydark_green","dark_green","light_green","verylight_green"}, "un arbre de banyan"),



    banyan_ball_tree(128,160,-1,-1, 13, 8, 85,
                     Arrays.asList(
                             new Triplet<>("stick", 1, 4), new Triplet<>("branch", 1, 2),new Triplet<>("leaf", 1, 4)
            ), 58,
            new float[]{56,15, 56,13, 52,13, 39,21, 39,29, 33,29, 29,33, 29,36, 31,36, 31,40, 46,48, 56,48, 56,46,
            69,46, 69,48, 79,48, 94,40, 94,36, 96,36, 96,33, 92,29, 86,29, 86,21, 73,13, 69,13, 69,15}, true, "tree", false, false, new String[]{"verydark_green","dark_green","light_green","verylight_green"}, "Un arbre à boule de banyan"),

    clear_trunk_little_tree(64,32,-1,-1, 1, 3, 9,
                            Arrays.asList(
                                    new Triplet<>("stick", 2, 4)
            ), 28,
                    new float[]{21,16, 25,18, 28,18, 28,16, 37,16, 37,18, 40,18, 44,16,
            46,8, 44,3, 40,3, 40,1, 37,1, 37,3, 28,3, 28,1, 25,1, 25,3, 21,3, 19,8}, true, "tree", false, false, new String[]{}, "Un petit arbre à tronc clair"),

    clear_trunk_tree(64,64,-1,-1, 7, 5, 11,
                     Arrays.asList(
                             new Triplet<>("stick", 1, 3)
            ), 54,
                    new float[]{13,15, 5,20, 5,22, 7,22, 7,26, 3,30, 3,32, 16,44, 22,44, 27,40, 38,40, 43,44, 49,44,
            62,32, 62,30, 58,26, 58,22, 60,22, 60,20, 55,15, 52,15, 52,9, 50,9, 50,7, 44,7, 38,11, 27,11, 21,7, 15,7, 15,9, 13,9}, true, "tree", false, false, new String[]{}, "Un arbre à tronc clair"),

    fir_1_tree(128,160,-1,-1, 15, 12, 110,
               Arrays.asList(
                       new Triplet<>("stick", 1, 4), new Triplet<>("branch", 0, 1),new Triplet<>("leaf", 0, 3),new Triplet<>("apple", 0, 2)
            ), 124,
            new float[]{61,15, 55,23, 43,27, 43,32, 55,32, 61,36,
            66,36, 72,32, 84,32, 84,27, 72,23, 66,15}, true, "tree", false, false, new String[]{"verydark_green","dark_green","light_green","verylight_green"}, "Un arbre de sapin"),

    fir_2_tree(128,96,-1,-1, 9, 10, 72,
               Arrays.asList(
                       new Triplet<>("stick", 1, 4), new Triplet<>("branch", 1, 2),new Triplet<>("leaf", 1, 4),new Triplet<>("apple", 0, 2)
            ), 88,
            new float[]{61,9, 53,15, 47,15, 47,17, 41,17, 41,19, 39,19, 39,24, 47,30, 53,30, 59,36,
            70,36, 76,30, 82,30, 90,24, 90,19, 88,19, 88,17, 82,17, 82,15, 76,15, 68,9}, true, "tree", false,false, new String[]{"verydark_green","dark_green","light_green","verylight_green"}, "Un arbre de sapin"),

    fir_dead_tree(128,128,-1,-1, 3, 10, 110,
                  Arrays.asList(
                          new Triplet<>("stick", 6, 8),new Triplet<>("branch", 0, 2)
            ), 124,
            new float[]{61,3, 55,11, 43,15, 43,20, 55,20, 61,24,
            66,24, 72,20, 84,20, 84,15, 72,11, 66,3}, true, "tree", false, false, new String[]{}, "Un arbre de sapin mort"),

    japanese_maple_tree(128,128,-1,-1, 17, 9, 65,
                        Arrays.asList(
                                new Triplet<>("stick", 0, 3), new Triplet<>("branch", 0, 2),new Triplet<>("leaf", 0, 3),new Triplet<>("apple", 0, 1)
            ), 96,
            new float[]{61,17, 55,25, 49,25, 49,27, 45,27, 45,29, 50,34, 57,34, 62,38, 67,38,
            72,34, 79,34, 84,29, 84,27, 80,27, 80,25, 74,25, 68,17 }, true, "tree", false, false, new String[]{"verydark_green","dark_green","light_green","verylight_green","orange_green","orange","red"}, "Un arbre d'érable japonais"),

    liana_tree(192, 256, -1, -1, 39, 10, 200,
            Arrays.asList(
                    new Triplet<>("stick", 2, 8), new Triplet<>("branch", 2, 5), new Triplet<>("leaf", 0, 4)
            ), 214,
            new float[]{73, 55, 81, 58, 87, 59, 93, 60, 100, 60, 112, 56, 112, 50, 100, 39, 93, 39, 90, 43, 81, 43, 73, 49}, true, "tree", false,false, new String[]{"verydark_green", "dark_green", "light_green"}, "Un arbre de liane"),

    oak_tree(128, 160, -1, -1, 27, 15, 100,
            Arrays.asList(
                    new Triplet<>("stick", 0, 3), new Triplet<>("branch", 1, 2), new Triplet<>("leaf", 1, 4), new Triplet<>("apple", 0, 2)
            ), 116,
            new float[]{45, 39, 49, 44, 59, 44, 59, 46, 64, 46, 64, 44, 74, 44, 80, 37, 80, 34, 76, 29, 64, 29, 64, 27, 59, 27, 59, 29, 49, 29, 45, 36}, true, "tree", false,false, new String[]{"verydark_green", "dark_green", "light_green", "verylight_green"}, "Un chêne"),

    spruce_tree(128, 160, -1, -1, 27, 15, 104,
            Arrays.asList(
                    new Triplet<>("stick", 0, 3), new Triplet<>("branch", 1, 1), new Triplet<>("leaf", 1, 3)
            ), 114,
            new float[]{62, 29, 62, 27, 57, 27, 54, 33, 49, 33, 47, 35, 47, 38, 49, 40, 55, 40, 55, 44, 57, 46, 62, 46, 62, 44, 67, 44, 67, 46, 72, 46, 74, 44, 74, 40, 80, 40, 82, 38, 82, 35, 80, 33, 75, 33, 72, 27, 67, 27, 67, 29}, true, "tree", false, false, new String[]{"verydark_green", "dark_green", "light_green", "verylight_green"}, "Un épicéa"),

    boulder(64, 64, -1, -1, 1, 30, 25,
            List.of(
                    new Triplet<>("rock", 5, 10)
            ), 60,
            new float[]{11, 13, 11, 24, 15, 24, 15, 30, 19, 34, 30, 34, 30, 32, 46, 32, 46, 30, 54, 30, 62, 24, 62, 15, 58, 13, 57, 7, 54, 3, 43, 3, 43, 5, 27, 5, 27, 7, 19, 7}, true, "rock", false, false, new String[]{}, "Un rocher"),

    boulder_little(32, 32, -1, -1, 1, 15, 10,
            List.of(
                    new Triplet<>("rock", 1, 5)
            ), 17,
            new float[]{3, 9, 3, 12, 9, 18, 15, 20, 26, 20, 32, 16, 32, 13, 20, 3, 13, 3}, true, "rock", true, false, new String[]{}, "Un petit rocher"),

    box(16,32 ,16,30,2,5 ,18,
            List.of(
                    new Triplet<>("branch", 1, 1)),
            14,
            new float[]{2,6, 2,11, 6,14, 11,14, 15,11, 15,16, 11,3, 6,3}, true, "chest", true, true, new String[]{}, "Un coffre pour ranger tout un tas de trucs..."),
    //BlockEntity Animated
    flower_blue1(32, 32, 32, 32, 1, 1, 30,
            List.of(
                    new Triplet<>("leaf", 0, 1)
            ), 30,
            new float[]{1,1, 1,30, 30,30, 30,1}, false, "grasses", false, false, new String[]{}, "Une jolie fleur bleue"),

    grasses1(32, 32, 32, 32, 1, 1, 30,
            List.of(
                    new Triplet<>("leaf", 0, 1)
            ), 30,
            new float[]{1,1, 1,30, 30,30, 30,1}, false, "grasses", false, false, new String[]{}, "Des herbes hautes"),

    twigs(32, 32, 32, 32, 1, 1, 30,
            List.of(
                    new Triplet<>("leaf", 0, 1)
            ), 30,
            new float[]{1,1, 1,30, 30,30, 30,1}, false, "twigs", false, false, new String[]{}, "Des brindilles");



    //  From AbstractObject
    private final int imageWidth;
    private final int imageHeight;
    private final int contentImageWidth;
    private final int contentImageHeight;//TODO a calculer...
    private final int offsetY;

    //  From AbstractEntity
    /**equal to maxDurability*/
    private final float maxLife;
    private final int stature;
    private final List<Triplet<String,Integer,Integer>> deathLoot;
    //Hitbox info
    /**Hauteur du rectangle hitbox (sans vu isometrique (nombre de pixels en Y non transp))*/
    private final int hitboxHeight;

    /**Points de l'hitbox précise (polygon)*/
    private float[] listOriginalPolygon;
    //From BlockEntity
    private final boolean breakable;
    private final String type; //Permet de savoir quels outils peuvent casser le block
    private final boolean movable;
    private final boolean craftable;

    /**Liste des couleurs possible pour l'item (green...) */
    private final String[] listColor;
    private final String description;

    //tempo
    private final Texture textureFirstElement;

    BlockEntityType(int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight ,int offsetY, float maxLife, int stature, List<Triplet<String, Integer, Integer>> deathLoot, int hitboxHeight, float[] listOriginalPolygon,boolean breakable, String type, boolean movable, boolean craftable, String[] listColor, String description) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.contentImageWidth = contentImageWidth;
        this.contentImageHeight = contentImageHeight;
        this.offsetY = offsetY;
        this.maxLife = maxLife;
        this.stature = stature;
        this.deathLoot = deathLoot;
        this.hitboxHeight = hitboxHeight;
        this.breakable = breakable;
        this.type = type;
        this.movable = movable;
        this.craftable = craftable;
        this.listColor = listColor;
        this.listOriginalPolygon = listOriginalPolygon;
        this.description = description;
        if(listColor.length == 0) {
            this.textureFirstElement = new Texture(Gdx.files.internal(getFullNameFileFromColor("default")));
        }else{
            this.textureFirstElement = new Texture(Gdx.files.internal(getFullNameFileFromColor(listColor[0])));
        }


    }

    //Getters

    public int imageWidth(){
        return imageWidth;
    }

    public int imageHeight(){
        return imageHeight;
    }

    public int contentImageWidth(){
        return contentImageWidth;
    }

    public int contentImageHeight(){
        return contentImageHeight;
    }

    public int offsetY(){
        return offsetY;
    }

    public float maxLife(){
        return maxLife;
    }

    public int stature(){
        return stature;
    }

    public List<Triplet<String,Integer,Integer>> deathLoot(){
        return deathLoot;
    }

    public int hitboxHeight(){
        return hitboxHeight;
    }

    public boolean breakable(){
        return breakable;
    }

    public String type(){
        return type;
    }

    public List<ToolType> breakableByFromType(){
        switch (type){
            case "tree":
                return Arrays.asList(ToolType.hache_de_guerre,ToolType.hache);
            case "rock":
                return Arrays.asList(ToolType.pioche);
            default:
                return null;
        }
    }

    public boolean movable(){
        return movable;
    }

    public String[] listColor(){
        return listColor;
    }

    /**Rectangle hitbix selon la position 0 0 */
    public Rectangle getApproximativeHitbox(){
        //System.out.println("listOriginalPolygon" + Arrays.toString(listOriginalPolygon));
        return getPreciseHitbox().getBoundingRectangle();
    }

    /**Polygon hitbox selon la position 0 0, à partir de listOriginalPolygon */
    public Polygon getPreciseHitbox(){
        return new Polygon(listOriginalPolygon);
    }

    /**Permet d'avoir le nom du fichier de l'image de l'item à partir de la couleur souhaité.
     * Verifie si la couleur souhaitée existe pour l'itemType en question
     * @param color
     * @return Si Color existe retourne nomType+color sinon met un message d'erreur et retourne null
     */
    public String getFullNameFileFromColor(String color){
        if(!color.equals("default")){
            for(String col : listColor){
                //System.out.println("colo"+col);
                //System.out.println("TEST" + "src/main/resources/Blocks/"+name()+"-"+col+".png");
                if(col.equals(color)){
                    return "src/main/resources/Blocks/"+name()+"-"+col+".png";
                }
            }
            //System.out.println("ERROR TEST" + "src/main/resources/Blocks/"+name()+"-"+color+".png");

            System.out.println("Error during getFullNameFilFromColor, the color "+ color+ " doesn't exist for " + name() );
            return null;
        }else{
            if(craftable)
                return "src/main/resources/Blocks/Craftable/"+name()+".png";
            else
            return "src/main/resources/Blocks/"+name()+".png";
        }
    }

    public String description(){
        return description;
    }

    /** Pour afficher la texture dans le craftBlockMenu*/
    public Texture texture(){
        return textureFirstElement;
    }


}
