package aura_game.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aura_game.app.Objects.Loot;
import aura_game.app.Objects.PlayableEntity;
import aura_game.app.Type.LootType;

/**Inventaire du joueur, singleton */
public class InventoryMenu extends BasicMenu {
    private static InventoryMenu instance;
    private PlayableEntity player;

    //-----------DRAW INVENTORY-------------
    private List<LootStack> content;

    //TODO: faire un max par slot ?  

    private InventoryMenu(String colorMenu) {
        super(colorMenu);
        this.content = new ArrayList<>();
        
    }

    public void initialize(PlayableEntity player){
        this.player = player;
    }

    public static InventoryMenu getInstance() {
        if (instance == null) {
            instance = new InventoryMenu("BLUE");
        }
        return instance;
    }

    /**
     * Verifie si le loot est déjà présent dans l'inventaire, et dans la quantité souhaitée
     * @param lootType
     * @param quantity
     * @return
     */
    public boolean hasLoot(LootType lootType, int quantity) {
        for (LootStack lootStack : content) {//Si l'id item est déjà présent dans l'inventaire on rajoute une qté
            if (lootStack.getLootType() == lootType) {
                return lootStack.getQuantity() >= quantity;
            }
        }
        return false;
    }

    /**TODO: a changer si y'a un max de loot par slot
     * Retourne la quantité du loot dans l'inventaire
     * @param lootType
     * @return
     */
    public int getLootQuantity(LootType lootType) {
        for (LootStack lootStack : content) {//Si l'id item est déjà présent dans l'inventaire on rajoute une qté
            if (lootStack.getLootType() == lootType) {
                return lootStack.getQuantity();
            }
        }
        return 0;
    }

    /**TODO: notif en bas a droite d'ajout
     * Ajoute à l'inventaire la quantité précisé de loot
     * @param loot loot a rajouter dans l'inventaire (on ajoute son lootType)
     * @return True si l'opération s'est effectué avec succès
     */
    public boolean addToInventory(Loot loot, int quantity){
        LootType lootTypeCollected = loot.getLootType();
        //TODO:URGENT mettre dans l'enum avec item (bruit quand... bruit quand...)
        for (LootStack lootStack : content) {//Si l'id item est déjà présent dans l'inventaire on rajoute une qté
            if (lootStack.getLootType() == lootTypeCollected) {
                lootStack.addQuantity(quantity);
                getAudioManager().playSound(getAudioManager().getSoundPickedUp(), 0.1f);//TODO:Mauvais endroit
                return true;
            }
        }//Sinon on le met dans un nouveau slot
        if (slotRestantMenu >0) {
            content.add(new LootStack(lootTypeCollected, quantity));//ou newItem.getQuantity()
            //System.out.print(slotRestantMenu-1);
            slotRestantMenu--;
            getAudioManager().playSound(getAudioManager().getSoundPickedUp(), 0.1f);//TODO:Mauvais endroit
            return true;
        }
        return false;

    }

    /**
     * Retire à l'inventaire la quantité demandés du loot (lors de craft, drop...)
     * @return le loot dont on a retiré la quantité //TODO utile ? 
     */
    public boolean removeFromInventory(LootType lootType,int quantity){
        if (!content.isEmpty()) {
            if(lootType !=null){
                //TODO:URGENT mettre dans l'enum avec loot (bruit quand... bruit quand...)
                //audioManager.playSound(audioManager.getSoundBreakWood(), 0.5f);
                for (LootStack lootStack : content) {//Si l'id item est déjà présent on l'enleve 
                    if (lootStack.getLootType() == lootType) {
                        
                        if(lootStack.getQuantity()>quantity){
                            lootStack.removeQuantity(quantity);
                        }else if (lootStack.getQuantity() == quantity){//PAS DE ELSE ??
                            content.remove(lootStack);
                            slotRestantMenu++;
                            //Evite que le slot selectionné soit hors tableau car on a vider un slot
                            if(slotSelected > (nbSlotsMenu-slotRestantMenu)-1 && slotSelected > 0 ){//Commence à 0
                                slotSelected--;
                            }
                        }else{
                            System.out.println("The quantity of loot to remove is not suffisant");
                            return false;}
                        return true ;//TODO: ici on retourne l'item type du itemStack mais ils devraient pas tous avoir la meme coordonnées
                    }
                }
            }
        }
        System.out.println("The lootType isn't present in your inventory, it's not possible to remove it");
        return false;

    }

    /**Drop le loot selectionné depuis l'inventaire */
    public boolean drop(){
        if(!content.isEmpty()){
            if(content.get(slotSelected).getQuantity()>0){
                LootType lt= content.get(slotSelected).getLootType();
                removeFromInventory(lt, 1);
                int mx = player.getEntityStateMachine().getCurrentOrientation().getX();
                int my = player.getEntityStateMachine().getCurrentOrientation().getY();
                LootManager.getInstance().spawnLoot(lt,player.getLootSpawnCenterX(lt.width()), player.getPosC_Y()+lt.offY(),true, LootManager.getInstance().getJumpVec(mx, my, 1) );
                return true;
            }
        }
        return false;
    }

    /**Affiche le menu inventaire et les loots, quand l'inventaire est ouvert  */
    public void render(SpriteBatch batch, BitmapFont font) {
            // Configurez la position et le style du texte
            font.setColor(Color.WHITE);
            font.getData().setScale(1.0f);
            // Affichez le menu à l'écran
            menuSprite.draw(batch);
            // Parcours du tableau de loots
            for (int i = 0; i < content.size(); i++) {
                LootStack lootStack = content.get(i);
                Pair<Integer,Integer> xy = getXYPositionLoot(i);
                if(i == slotSelected){   
                    selectedSlotSprite.setPosition(xy.getLeft(), xy.getRight());
                    selectedSlotSprite.draw(batch);//TODO: faire dans le changement de slotSelected pas chaque refresh

                }
                // Dessin de l'image du loot à sa position                                      //TODO s'affiche petit                                                                          
                batch.draw(lootStack.getTexture(), xy.getLeft() + marge, xy.getRight() + marge, lootWidth - (2*marge), lootHeight  - (2*marge));
                // Affichez le nombre de loots dans le slot
                font.draw(batch, "x"+ lootStack.getQuantity(), xy.getLeft() +10, xy.getRight()+15);
            }
            drawTextCenterIn("INVENTORY",600,500,2.0f,batch,font);


    }
}