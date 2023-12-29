package aura_game.app.Type;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import aura_game.app.Util.Triplet;

public enum EntityType {//TODO: ajouter speed ici ?
    player("player", 64, 64, 50, 32, 50, 10, 8,///TODO C'est 8 mais le 9 remet a 0.. (faire +1%nb)
        new int[] {
           //order:U L D R
           //SpellCast Thrust   Walk      Slash         Shoot      Hurt
            6,6,6,6, 7,7,7,7, 8,8,8,8,   5,5,5,5,     12,12,12,12, 5 // endSpriteX
        },new String[] {
            "SpellCast_U", "SpellCast_L",  "SpellCast_D", "SpellCast_R", "Thrust_U","Thrust_L", "Thrust_D", "Thrust_R", "Walk_U","Walk_L", "Walk_D", "Walk_R",
            "Slash_U", "Slash_L", "Slash_D", "Slash_R", "Shoot_U", "Shoot_L", "Shoot_D", "Shoot_R", "Hurt_D"},
            List.of(), 1, Pair.of(15, 20),Pair.of(4, -5)),

    cerf1("cerf1", 48, 60, 45, 32, 56, 16, 3,
        new int[] {
            // Walk: D L R U
            2, 2, 2, 2  // endSpriteX
        },new String[] {"Walk_D","Walk_L", "Walk_R", "Walk_U"},
            List.of(
                    new Triplet<>("stick", 0, 4)
            ), 1, Pair.of(30, 40),Pair.of(0, 0)),

    autruche("autruche", 48, 48, 50, 20, 48, 10, 6,
        new int[] {
            // Walk: D L R U
             5, 5, 5, 5  // endSpriteX
        },new String[] {"Walk_D","Walk_L", "Walk_R", "Walk_U"},
            List.of(
                    new Triplet<>("stick", 0, 4)
            ), 1, Pair.of(30, 40),Pair.of(0, 0));

        /*Pour rappel tab[][] le premier correspond a la hauteur et le 2e a la largeur
        Par exemple tab[0][1] vaut 2
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9} */

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
    private final int lastEndSpriteX;
    private final int[] spriteSheetXEnd;
    /**Permet de retrouver le nom de l'action correspondant dans spriteSheetActions */
    private final String[] spriteSheetActionsName;
    private final List<Triplet<String, Integer, Integer>> deathLoots;
    //Pour les coups/hit:
    /**Dégats que fait l'entité, sans armes */
    private final float degatDefault;
    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenghtDefault;
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un côté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    private final Pair<Integer,Integer> hitZonePointDecallageDefault;


    EntityType(String name, int spriteWidth, int spriteHeight, int tall, int hitboxWidth, int hitboxHeight, int hitboxHeightFlat,int lastEndSpriteX,
        int[] spriteSheetActions,String[] spriteSheetActionsName, List<Triplet<String, Integer, Integer>> deathLoots, float degatDefault,Pair<Integer,Integer> hitZoneLenghtDefault,Pair<Integer,Integer> hitZonePointDecallageDefault) {
        this.name = name;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.tall = tall;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.hitboxHeightFlat = hitboxHeightFlat;
        this.lastEndSpriteX = lastEndSpriteX;
        this.spriteSheetXEnd = spriteSheetActions;
        this.spriteSheetActionsName = spriteSheetActionsName;
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

    /**@return le endSpriteX default qui permet de retrouver la derniere Direction, car lorsque l'action s'arrête, ça passe à null */
    public int lastEndSpriteX(){
        return lastEndSpriteX;
    }
    /**@return le tableau de spriteEndX*/
    public int[] spriteSheetXEnd() {
        return spriteSheetXEnd;
    }
    /**@return les actions. Permet de retrouver le nom de l'action correspondant dans spriteSheetActions */
    public String[] spriteSheetActionsName() {
        return spriteSheetActionsName;
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
