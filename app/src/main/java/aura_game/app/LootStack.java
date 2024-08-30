package aura_game.app;

import aura_game.app.rework.TypeEnum.LootableObjectType;
import com.badlogic.gdx.graphics.Texture;

/**
 * Permet de stocker dans l'inventaire l'lootType avec son nombre
 */
public class LootStack {
    private LootableObjectType lootType;
    private Texture texture; //For inventory
    private int quantity;

    public LootStack(LootableObjectType lootType, int quantity) {
        this.lootType = lootType;
        this.quantity = quantity;
        texture = new Texture(lootType.texturePath());
    }

    // Getters et setters pour lootType et quantity

    public LootableObjectType getLootType() {
        return lootType;
    }

    public void setItem(LootableObjectType lootType) {
        this.lootType = lootType;
    }

    public int getQuantity() {
        return quantity;
    }
    /**Ajoute 1 */
    public void addOneQuantity(){
        this.quantity++;
    }
    /**Ajoute la quantité souhaitée */
    public void addQuantity(int qt){
        this.quantity+=qt;
    }

    /**Enlève la quantité souhaitée */
    public void removeQuantity(int qt){
        this.quantity-=qt;
    }

    public void removeOneQuantity(){
        this.quantity--;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**@return la texture du loot, pour pouvoir l'afficher dans l'inventaire */
    public Texture getTexture(){
        return texture;
    }
}
