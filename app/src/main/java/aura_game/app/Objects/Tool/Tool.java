package aura_game.app.Objects.Tool;

import aura_game.app.Objects.Loot;
import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import aura_game.app.Type.ToolType;

/**Un outil est un loot avec des attributs supplémentaire
 * On utilise LootType et ToolType (les 2 par outil) pour construire les tools
 * Les outils spéciaux (arcs, bombes...) ne sont pas ici
 */
public class Tool extends Loot {
    /**Dégats que fait l'outil*/
    private final float damage;
    private float solidity;
    private float maxSolidity;
    /**Texture noir et blanc pour affichage sur le wheel Menu (basé sur la texture de base avec effet contraste noir et blanc eleve de "Photos"*/
    private Texture textureBlackAndWhite;
    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenght;
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un coté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    private final Pair<Integer,Integer> hitZonePointDecallage;    

    public Tool(ToolType toolType, int x, int y, boolean bounce, Vector2 dir, boolean collected) {

        super(toolType.lootType(), x, y, bounce, dir,collected);
        this.damage = toolType.damage();
        this.maxSolidity = toolType.maxSolidity();
        this.solidity = maxSolidity;
        this.textureBlackAndWhite = new Texture("src/main/resources/Weapons/BlackWhite/"+toolType.lootType().getName()+".png");//Size 64 * 64
        this.hitZoneLenght = toolType.hitZoneLenght();
        this.hitZonePointDecallage = toolType.hitZonePointDecallage();

    }

    /**Texture noir sur blanc pour affichage sur le wheel Menu (basé sur la texture de base avec effet contraste noir et blanc eleve de "Photos"*/
    public Texture getTextureBlackAndWhite(){
        return textureBlackAndWhite;
    }

    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    public Pair<Integer, Integer> getHitZoneLenght() {
        return hitZoneLenght;
    }
    
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un
     * côté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    public Pair<Integer, Integer> getHitZonePointDecallage() {
        return hitZonePointDecallage;
    }

    public float getDamage() {
        return damage;
    }

    public float getSolidity() {
        return solidity;
    }

    public void setSolidity(float solidity) {
        this.solidity = solidity;
    }
}
