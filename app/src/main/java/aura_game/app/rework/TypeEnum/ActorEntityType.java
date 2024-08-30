package aura_game.app.rework.TypeEnum;

import aura_game.app.Util.Triplet;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public enum ActorEntityType {
    player("Aiden_Complete_V3", 64,64, -1,-1,0,6,
            50,List.of(), 32,50,10,4,4, Pair.of(15, 20),Pair.of(4, -5)),

    cerf1("cerf1", 48,60,-1,-1, 0, 12,
            45, List.of( new Triplet<>("stick", 0, 4)), 32,56,16,3,1,Pair.of(30, 40),Pair.of(0, 0)),

    autruche("autruche", 48,48,-1,-1,0,10,
            50,List.of(new Triplet<>("stick", 0, 4)), 20,48,10, 4, 1.5f, Pair.of(30, 40),Pair.of(0, 0)),
    bassicArrow("basicArrow",16,16,-1,-1,0, 3,
            16,List.of(),16,16,16,3,2, Pair.of(16, 16),Pair.of(0, 0));


    /**Nom pour charger le fichier png spriteSheet*/
    private final String name;
    //  From AbstractObject
    private final int imageWidth;
    private final int imageHeight;
    private final int contentImageWidth;
    private final int contentImageHeight;
    private final int offsetY;

    //  From AbstractEntity
    /**equal to maxDurability*/
    private final float maxLife;
    private final int stature;
    private final List<Triplet<String,Integer,Integer>> deathLoot;
    //Hitbox info
    /**Largeur du rectangle hitbox (colission)*/
    private final  int hitboxWidth;
    /**Hauteur du rectangle hitbox (sans vu isometrique (nombre de pixels en Y non transp))*/
    private final int hitboxHeight;
    /**Hauteur de l'hitbox de l'entite pour marcher etc (collision)*/
    private final int hitboxHeightFlat;


    //From MovableEntity
    private final int speed;
    private final String texturePath;

    //  From ActorEntity
    private final float damage;
    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenghtDefault;
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un côté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité
     */
    private final Pair<Integer,Integer> hitZonePointDecallageDefault;


    ActorEntityType(String name, int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY, float maxLife, int stature, List<Triplet<String, Integer, Integer>> deathLoot, int hitboxWidth, int hitboxHeight, int hitboxHeightFlat, int speed, float damage, Pair<Integer, Integer> hitZoneLenghtDefault, Pair<Integer, Integer> hitZonePointDecallageDefault) {
        this.name = name;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.contentImageWidth = contentImageWidth;
        this.contentImageHeight = contentImageHeight;
        this.offsetY = offsetY;
        this.maxLife = maxLife;
        this.stature = stature;
        this.deathLoot = deathLoot;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.hitboxHeightFlat = hitboxHeightFlat;
        this.speed = speed;
        this.texturePath = "src/main/resources/Entities/"+this.name()+".png";
        this.damage = damage;
        this.hitZoneLenghtDefault = hitZoneLenghtDefault;
        this.hitZonePointDecallageDefault = hitZonePointDecallageDefault;
    }


    ///getters


    public String getName() {
        return name;
    }

    public int imageWidth() {
        return imageWidth;
    }

    public int imageHeight() {
        return imageHeight;
    }

    public int contentImageWidth() {
        return contentImageWidth;
    }

    public int contentImageHeight() {
        return contentImageHeight;
    }

    public int offsetY() {
        return offsetY;
    }

    public float maxLife() {
        return maxLife;
    }

    public int stature() {
        return stature;
    }

    public List<Triplet<String, Integer, Integer>> deathLoot() {
        return deathLoot;
    }

    public int hitboxWidth() {
        return hitboxWidth;
    }

    public int hitboxHeight() {
        return hitboxHeight;
    }

    public int hitboxHeightFlat() {
        return hitboxHeightFlat;
    }

    /**Rectangle hitbix selon la position 0 0 (bounding rectangle du polygon) */
    public Rectangle getApproximativeHitbox(){
        return getPreciseHitbox().getBoundingRectangle();
    }

    /**Polygon hitbox selon la position 0 0, à partir de listOriginalPolygon, en prenant en compte le centrage de l'hitbox */
    public Polygon getPreciseHitbox(){
        //La hitbox est centrée sur l'image
        int difPosHitboxX = imageWidth/2 - hitboxWidth/2;
        int difPosHitboxY = 0;
        return new Polygon(new float[]{difPosHitboxX,difPosHitboxY,  difPosHitboxX,((difPosHitboxY)+hitboxHeightFlat),  (difPosHitboxX+hitboxWidth),((difPosHitboxY)+hitboxHeightFlat),/*enlever suivant*/ (difPosHitboxX+hitboxWidth),((difPosHitboxY)+hitboxHeightFlat),  (difPosHitboxX+hitboxWidth),(difPosHitboxY),               /*TEMPO POUR REGLER PB GENERATION INTERMEDIAIRE*/ difPosHitboxX,difPosHitboxY});
    }

    public int speed() {
        return speed;
    }

    public String texturePath() {
        return texturePath;
    }

    public float damage() {
        return damage;
    }

    public Pair<Integer, Integer> hitZoneLenghtDefault() {
        return hitZoneLenghtDefault;
    }

    public Pair<Integer, Integer> hitZonePointDecallageDefault() {
        return hitZonePointDecallageDefault;
    }
}
