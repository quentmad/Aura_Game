package aura_game.app.Type;

import org.apache.commons.lang3.tuple.Pair;

/**Un outil est un loot avec des attributs supplémentaire
 * On utilise LootType et ToolType (les 2 par outil) pour construire les tools
 * Les outils spéciaux (arcs, bombes...) ne sont pas ici
 */
public enum ToolType {
    //! VALUE OF APRES POUR PAS STOCK LE LOOTTYPE ???
    hache(LootType.hache,3,100, Pair.of(30, 40),Pair.of(0, 0)),
    marteau(LootType.marteau, 3,100,Pair.of(30, 40),Pair.of(0, 0)),
    pioche(LootType.pioche,3,100, Pair.of(30, 40),Pair.of(0, 0)),
    pelle(LootType.pelle,3,100, Pair.of(30, 40),Pair.of(0, 0)),
    
    flail(LootType.flail, 3,100,Pair.of(30, 40),Pair.of(0, 0)),
    hache_de_guerre(LootType.hache_de_guerre,3,100, Pair.of(30, 40),Pair.of(0, 0)),
    mace(LootType.mace, 3,100,Pair.of(30, 40),Pair.of(0, 0)),
    simple_staff(LootType.simple_staff,3,100, Pair.of(30, 40),Pair.of(0, 0)),
    diamond_staff(LootType.diamond_staff, 3,100,Pair.of(30, 40),Pair.of(0, 0)),
    gnarled_staff(LootType.gnarled_staff,3,100, Pair.of(30, 40),Pair.of(0, 0)),
    loop_staff(LootType.loop_staff, 3,100,Pair.of(30, 40),Pair.of(0, 0)),
    longue_lance(LootType.longue_lance, 3,100,Pair.of(30, 40),Pair.of(0, 0)),
    
    magic(LootType.magic,6,100, Pair.of(30, 40),Pair.of(0, 0)),
    magic_alt(LootType.magic_alt, 7,100,Pair.of(30, 40),Pair.of(0, 0)),
    trident(LootType.trident,5,100, Pair.of(30, 40),Pair.of(0, 0)),
    spear_metal(LootType.spear_metal, 6,140,Pair.of(30, 40),Pair.of(0, 0)),
    spear_wooden(LootType.spear_wooden,6,90, Pair.of(30, 40),Pair.of(0, 0)),

    arc_normal(LootType.arc_normal, 2,90,Pair.of(30, 40),Pair.of(0, 0)),
    arc_great(LootType.arc_great,2,110, Pair.of(30, 40),Pair.of(0, 0)),
    fleches(LootType.fleches, 5,10,Pair.of(30, 40),Pair.of(0, 0)),
    arbalette(LootType.arbalette,2,100, Pair.of(30, 40),Pair.of(0, 0)),
   
    longue_epee(LootType.longue_epee, 9,100,Pair.of(30, 40),Pair.of(0, 0)),
    katana(LootType.katana,8,100, Pair.of(30, 40),Pair.of(0, 0)),
    sabre(LootType.sabre, 7,100,Pair.of(30, 40),Pair.of(0, 0)),
    rappier(LootType.rappier, 7,100,Pair.of(30, 40),Pair.of(0, 0)),
    poignard(LootType.poignard, 4,100,Pair.of(30, 40),Pair.of(0, 0));


    /**stick, leaf */
    private final LootType lootType;
    private final float damage;
    private final float maxSolidity;
    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenght;
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un côté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    private final Pair<Integer,Integer> hitZonePointDecallage;    
 

    ToolType(LootType lootType, float damage, float maxSolidity,Pair<Integer,Integer> hitZoneLenght,Pair<Integer,Integer> hitZonePointDecallage ) {
        this.lootType = lootType;
        this.damage = damage;
        this.maxSolidity = maxSolidity;
        this.hitZoneLenght = hitZoneLenght;
        this.hitZonePointDecallage = hitZonePointDecallage;
    }


    public LootType lootType() {
        return lootType;
    }

    public float damage() {
        return damage;
    }


    public float maxSolidity() {
        return maxSolidity;
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
