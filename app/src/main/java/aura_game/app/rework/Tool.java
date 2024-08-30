package aura_game.app.rework;

import aura_game.app.rework.TypeEnum.LootableObjectType;
import aura_game.app.rework.TypeEnum.ToolType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.apache.commons.lang3.tuple.Pair;

public class Tool extends LootableObject{
    private float durability;
    private float maxDurability;
    private float damage;

    /**Texture noir et blanc pour affichage sur le wheel Menu (basé sur la texture de base avec effet contraste noir et blanc eleve de "Photos"*/
    private Texture textureBlackAndWhite;

    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenght;
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un coté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    private final Pair<Integer,Integer> hitZonePointDecallage;


    public Tool(ToolType tl, int x, int y, boolean bounce, Vector2 dir, boolean onGround) {
        super(LootableObjectType.valueOf(tl.name()), x, y, bounce, dir, onGround);
        this.textureBlackAndWhite = new Texture("src/main/resources/Weapons/BlackWhite/"+getName()+".png");//Size 64 * 64
        this.hitZoneLenght =tl.hitZoneLenght();
        this.hitZonePointDecallage = tl.hitZonePointDecallage();
        this.durability = tl.durability();
        this.maxDurability = tl.durability();
        this.damage = tl.damage();
    }

    public void reduceDurability(){
        durability--;
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

    public Texture textureBlackAndWhite() {
        return textureBlackAndWhite;
    }

    public Pair<Integer, Integer> hitZoneLenght() {
        return hitZoneLenght;
    }

    public Pair<Integer, Integer> hitZonePointDecallage() {
        return hitZonePointDecallage;
    }

    /**
     * Cette méthode calcule et renvoie un rectangle correspondant à la zone de dégâts de l'outil actuellement utilisé par l'entité.
     * La zone de dégâts est déterminée en fonction de la direction de l'entité et de l'outil.
     *
     * @param ent l'entité portant l'arme
     * @return Rectangle Un objet Rectangle représentant la zone de dégâts.
     */
    public Rectangle zoneDegat(ToolWieldingEntity ent){
        int marge = 15;
        String dir = ent.stateComponant().getCurrentOrientation().getDirection();
        int width = dir.equals("U") || dir.equals("D") ? hitZoneLenght.getLeft() : hitZoneLenght.getRight();
        int height = dir.equals("U") || dir.equals("D") ? hitZoneLenght.getRight() : hitZoneLenght.getLeft();
        int startX = (int)ent.hitbox().approximativeHitbox().getX() + hitZonePointDecallage.getLeft();
        int startY = (int)ent.hitbox().approximativeHitbox().getY() + (dir.equals("U") ? marge + ent.hitbox().height + hitZonePointDecallage.getRight() : -height + marge - hitZonePointDecallage.getRight());

        if(dir.equals("R") || dir.equals("L")) {
            startX += dir.equals("R") ? ent.hitbox().width + hitZonePointDecallage.getRight() : -width - hitZonePointDecallage.getRight();
        }

        return new Rectangle(startX, startY, width, height);
    }

    /** défini la durabilité de l'outil
     * @param durability la durabilité de l'outil
     */
    public void setSolidity(float durability) {
        durability = durability;
    }
}
