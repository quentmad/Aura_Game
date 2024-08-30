package aura_game.app.rework.TypeEnum;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Enumération des types d'outils.
 * Les outils sont des objets lootables avec des attributs supplémentaires.
 * On utilise LootableObjectType et ToolType (les 2 par outil) pour construire les outils.
 */
public enum ToolType {

    hache(3,100, Pair.of(30, 40),Pair.of(0, 0)),
    marteau(3,100,Pair.of(30, 40),Pair.of(0, 0)),
    pioche(3,100, Pair.of(30, 40),Pair.of(0, 0)),
    pelle(3,100, Pair.of(30, 40),Pair.of(0, 0)),

    flail( 3,100,Pair.of(30, 40),Pair.of(0, 0)),
    hache_de_guerre(3,100, Pair.of(30, 40),Pair.of(0, 0)),
    mace(3,100,Pair.of(30, 40),Pair.of(0, 0)),
    simple_staff(3,100, Pair.of(30, 40),Pair.of(0, 0)),
    diamond_staff( 3,100,Pair.of(30, 40),Pair.of(0, 0)),
    gnarled_staff(3,100, Pair.of(30, 40),Pair.of(0, 0)),
    loop_staff(3,100,Pair.of(30, 40),Pair.of(0, 0)),
    longue_lance( 3,100,Pair.of(30, 40),Pair.of(0, 0)),

    magic(6,100, Pair.of(30, 40),Pair.of(0, 0)),
    magic_alt( 7,100,Pair.of(30, 40),Pair.of(0, 0)),
    trident(5,100, Pair.of(30, 40),Pair.of(0, 0)),
    spear_metal( 6,140,Pair.of(30, 40),Pair.of(0, 0)),
    spear_wooden(6,90, Pair.of(30, 40),Pair.of(0, 0)),

    arc_normal(2,90,Pair.of(30, 40),Pair.of(0, 0)),
    arc_great(2,130, Pair.of(30, 40),Pair.of(0, 0)),
    fleches(5,10,Pair.of(30, 40),Pair.of(0, 0)),
    arbalette(2,100, Pair.of(30, 40),Pair.of(0, 0)),

    longue_epee(9,100,Pair.of(30, 40),Pair.of(0, 0)),
    katana(8,100, Pair.of(30, 40),Pair.of(0, 0)),
    sabre(7,100,Pair.of(30, 40),Pair.of(0, 0)),
    rappier(7,100,Pair.of(30, 40),Pair.of(0, 0)),
    poignard(4,100,Pair.of(30, 40),Pair.of(0, 0));


    //Linked to LootableObjectType via valueOf (name)
    private float durability;
    private float maxDurability;
    private float damage;

    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenght;
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un coté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    private final Pair<Integer,Integer> hitZonePointDecallage;

    ToolType(float damage, float solidity, Pair<Integer,Integer> hitZoneLenght, Pair<Integer,Integer> hitZonePointDecallage ) {
        this.durability = solidity;
        this.maxDurability = solidity;
        this.damage = damage;

        this.hitZoneLenght = hitZoneLenght;
        this.hitZonePointDecallage = hitZonePointDecallage;
    }

    public float durability() {
        return durability;
    }

    public float maxDurability() {
        return maxDurability;
    }

    public float damage() {
        return damage;
    }

    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    public Pair<Integer, Integer> hitZoneLenght() {
        return hitZoneLenght;
    }

    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un côté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    public Pair<Integer, Integer> hitZonePointDecallage() {
        return hitZonePointDecallage;
    }








}
