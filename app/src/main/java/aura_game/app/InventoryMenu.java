package aura_game.app;

import java.util.ArrayList;
import java.util.List;

import aura_game.app.GameManager.FontManager;
import aura_game.app.Notifications.NotificationIconText;
import aura_game.app.Notifications.NotificationManager;

import aura_game.app.rework.Player;
import aura_game.app.rework.TypeEnum.LootableObjectType;
import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**Inventaire du joueur, singleton */
public class InventoryMenu extends BasicMenu {
    private static InventoryMenu instance;
    private Player player;

    //-----------DRAW INVENTORY-------------
    private List<LootStack> content;

    //TODO: faire un max par slot ?  

    private InventoryMenu(String colorMenu) {
        super(colorMenu);
        this.content = new ArrayList<>();
        
    }

    public void initialize(Player player){
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
    public boolean hasLoot(LootableObjectType lootType, int quantity) {
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
    public int getLootQuantity(LootableObjectType lootType) {
        for (LootStack lootStack : content) {//Si l'id item est déjà présent dans l'inventaire on rajoute une qté
            if (lootStack.getLootType() == lootType) {
                return lootStack.getQuantity();
            }
        }
        return 0;
    }

    /**TODO: notif en bas a droite d'ajout
     * Ajoute à l'inventaire la quantité précisé de loot
     * @param lootType loot a rajouter dans l'inventaire (on ajoute son lootType)
     * @return True si l'opération s'est effectué avec succès
     */
    public boolean addToInventory(LootableObjectType lootType, int quantity){
        boolean success = false;
        //LootableObjectType lootTypeCollected = LootableObjectType.valueOf(loot.getName());
        //TODO:URGENT mettre dans l'enum avec item (bruit quand... bruit quand...)
        for (LootStack lootStack : content) {//Si l'id item est déjà présent dans l'inventaire on rajoute une qté
            if (lootStack.getLootType() == lootType) {
                lootStack.addQuantity(quantity);
                getAudioManager().playSound(getAudioManager().getSoundPickedUp(), 0.1f);//TODO:Mauvais endroit
                success =  true;
                break;
            }
        }//Sinon on le met dans un nouveau slot
        if (slotRestantMenu >0 && !success) {
            content.add(new LootStack(lootType, quantity));//ou newItem.getQuantity()
            //System.out.print(slotRestantMenu-1);
            slotRestantMenu--;
            getAudioManager().playSound(getAudioManager().getSoundPickedUp(), 0.1f);//TODO:Mauvais endroit
            success =  true;
        }
        if(success) NotificationManager.getInstance().addNotification(new NotificationIconText(lootType.name(), lootType.texture(),quantity, FontManager.getInstance().getFontDescription(), 5, false));
        return success;

    }

    /**
     * Retire à l'inventaire la quantité demandés du loot (lors de craft, drop...)
     * @return le loot dont on a retiré la quantité //TODO utile ? 
     */
    public boolean removeFromInventory(LootableObjectType lootType, int quantity){
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
                LootableObjectType lt= content.get(slotSelected).getLootType();
                removeFromInventory(lt, 1);
                int mx = player.stateComponant().getCurrentOrientation().getX();
                int my = player.stateComponant().getCurrentOrientation().getY();
                LootManager.getInstance().spawnLoot(lt,player.getLootSpawnCenterX(lt.imageWidth()), player.posC().y()+lt.offsetY(),true, LootManager.getInstance().getJumpVec(mx, my, 1) );
                return true;
            }
        }
        return false;
    }

    /**Affiche le menu inventaire et les loots, quand l'inventaire est ouvert  */
    public void render(SpriteBatch batch) {
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
                FontManager.getInstance().getFontDescription().draw(batch, "x"+ lootStack.getQuantity(), xy.getLeft() +10, xy.getRight()+15);
            }
            drawTextCenterIn("INVENTORY",640,600,2.0f,batch,FontManager.getInstance().getFontMenuTitle());


    }
}