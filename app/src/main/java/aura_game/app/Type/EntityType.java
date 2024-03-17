package aura_game.app.Type;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import aura_game.app.Util.Triplet;


//TODO idee : faire comme les lemmings des "strategy" en fonction de l'action actuelle (qui ont chacune U L D R ou just 1 pour hurt
public enum EntityType {//TODO: ajouter speed ici ?
    player("Aiden_Complete", 64, 64, 50, 32, 50, 10,  List.of(), 1, Pair.of(15, 20),Pair.of(4, -5)),

    cerf1("cerf1", 48, 60, 45, 32, 56, 16,//end x : 3
            //new String[] {"Walk_D","Walk_L", "Walk_R", "Walk_U"},
            List.of(
                    new Triplet<>("stick", 0, 4)
            ), 1, Pair.of(30, 40),Pair.of(0, 0)),

    autruche("autruche", 48, 48, 50, 20, 48, 10,//end x : 6
            //new String[] {"Walk_D","Walk_L", "Walk_R", "Walk_U"},
            List.of(
                    new Triplet<>("stick", 0, 4)
            ), 1, Pair.of(30, 40),Pair.of(0, 0));


    /**Nom pour charger le fichier png spriteSheet*/
    private final String name;
    /**Largeur d'un sprite dans le sprite sheet*/
    private final int spriteWidth;
    /**Hauteur d'un sprite dans le sprite sheet*/
    private final int spriteHeight;
    /**La taille de l'entite en hauteur (1m70...)*/
    private final int tall;
    /**Largeur du rectangle hitbox (colission)*/
    private final  int hitboxWidth;
    /**Hauteur du rectangle hitbox (sans vu isometrique (nombre de pixels en Y non transp))*/
    private final int hitboxHeight;
    /**Hauteur de l'hitbox de l'entite pour marcher etc (collision)*/
    private final int hitboxHeightFlat;
    /**le endSpriteX default qui permet de retrouver la dernière Direction, car lorsque l'action s'arrête, ça passe à null */
    //private final int lastEndSpriteX;
    //private final int[] spriteSheetXEnd;
    /**Permet de retrouver le nom de l'action correspondant dans spriteSheetActions */
    //private final String[] spriteSheetActionsName;
    private final List<Triplet<String, Integer, Integer>> deathLoots;
    //Pour les coups/hit:
    /**Dégats que fait l'entité, sans armes */
    private final float degatDefault;
    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenghtDefault;
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un côté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    private final Pair<Integer,Integer> hitZonePointDecallageDefault;


    EntityType(String name, int spriteWidth, int spriteHeight, int tall, int hitboxWidth, int hitboxHeight, int hitboxHeightFlat,
        List<Triplet<String, Integer, Integer>> deathLoots, float degatDefault,Pair<Integer,Integer> hitZoneLenghtDefault,Pair<Integer,Integer> hitZonePointDecallageDefault) {
        this.name = name;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.tall = tall;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.hitboxHeightFlat = hitboxHeightFlat;
        this.deathLoots = deathLoots;
        this.degatDefault = degatDefault;
        this.hitZoneLenghtDefault = hitZoneLenghtDefault;
        this.hitZonePointDecallageDefault = hitZonePointDecallageDefault;
    }
    /**@return le name  */
    public String getName(){
        return name;
    }
    /**@return la largeur d'une sprite de la spriteSheet*/
    public int spriteWidth() {
        return spriteWidth;
    }
    /**@return la hauteur d'une sprite de la spriteSheet*/
    public int spriteHeight() {
        return spriteHeight;
    }
    /**@return La taille de l'entite en hauteur (1m70...)*/
    public int tall() {
        return tall;
    }

    /**@return Largeur du rectangle hitbox (colission)*/
    public int hitboxWidth() {
        return hitboxWidth;
    }

    /**@return Hauteur du rectangle hitbox (sans vu isometrique)*/
    public int hitboxHeight() {
        return hitboxHeight;
    }

    /**@return Hauteur de l'hitbox de l'entite pour marcher etc (collision)*/
    public int hitboxHeightFlat() {
        return hitboxHeightFlat;
    }

    public List<Triplet<String, Integer, Integer>> deathLoots(){
        return deathLoots;
    }

    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    public Pair<Integer, Integer> hitZoneLenghtDefault() {
        return hitZoneLenghtDefault;
    }

    /**Dégats que fait l'entité, sans armes */
    public float degatDefault() {
        return degatDefault;
    }

    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un côté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    public Pair<Integer, Integer> hitZonePointDecallageDefault() {
        return hitZonePointDecallageDefault;
    }

}
