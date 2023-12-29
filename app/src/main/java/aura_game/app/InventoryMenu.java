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

    //GETTERS et SETTERS AFIN D'Y ACCEDER DEPUIS BASICMENUMANAGER


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

    /* a maj
    public boolean drop(LootType lootType){
        removeFromInventory(lootType, 1);
        LootManager.getInstance().spawnLoot(lootType,player.getLootSpawnCenterX(lootType.width()), player.getPosC_Y()+lootType.offY(),true, LootManager.getInstance().getJumpVec(player.getCurrentDeplacement().getLeft(),player.getCurrentDeplacement().getRight() , 1) );
        return true;
    }*/

    /**Drop le loot selectionné */
    public boolean drop(){
        if(!content.isEmpty()){
            if(content.get(slotSelected).getQuantity()>0){
                LootType lt= content.get(slotSelected).getLootType();
                //int oldSlotRestant = slotRestantMenu;
                removeFromInventory(lt, 1);
                /*if(oldSlotRestant !=slotRestantMenu){
                    updateToolIndexFrom(slotSelected);//COMME CA ?
                }*/
                LootManager.getInstance().spawnLoot(lt,player.getLootSpawnCenterX(lt.width()), player.getPosC_Y()+lt.offY(),true, LootManager.getInstance().getJumpVec(player.getCurrentDeplacement().getLeft(),player.getCurrentDeplacement().getRight() , 1) );
                return true;
            }
        }
        return false;
    }

        /** Appelle moveSlotSelected et met à jour également l'arme actuelle (si il selectionne une arme)//TODO modif selon facon de changer d'armes/acceder + tard
         * @param direction "R" ou "L"
         * @return
         *///! update : pu de tool dans Inventory
        /*public int moveSlotUpdateCurrentTool(String direction){//TODO: vers haut et bas aussi
            int newSlotSelected = moveSlotSelected(direction);
           // updateToolIndexFrom(newSlotSelected);//TODO enlever !!! wheelmenu les tools
                

            return newSlotSelected; 
        }*/

        /**Met a jour le currentTool a partir du slot selected en parametre
         * Si le slot correspond a un tool, on défini le currentTool à l'index correspondant, sinon -1
         * Voir updateCurrentToolIndex la méthode appelé avec le String tool (ou "")
         * @param slotSelected le slot selectionné dans l'inventaire
         */ /* //! ENLEVERRRRR OLD 
        private void updateToolIndexFrom(int slotSelected) {
            LootType lootSelected = content.get(slotSelected).getLootType();

            if(lootSelected !=null && !lootSelected.getName().equals(player.getCurrentToolName()) && lootSelected.type().equals("tool")){
                System.out.println(lootSelected.getName());
                player.updateCurrentToolIndex(lootSelected.getName());

            }else if(!player.getCurrentToolName().equals("")){//TODO voir quand lancer else pour "desarmer"
                player.updateCurrentToolIndex("");//Aucune arme   
            }
        }*/




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