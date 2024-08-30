package aura_game.app;
import java.util.List;

import aura_game.app.GameManager.FontManager;
import aura_game.app.rework.TypeEnum.LootableObjectType;
import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aura_game.app.Craft.CraftLootManager;

/**Semblable à l'InventoryMenu, mais on affiche à gauche les crafts disponible
 * et à droite les ingrédients + description pour crafter le loot sélectionné */
public class CraftingLootMenu extends BasicMenu {
    
    /**Inventaire crafting du joueur, singleton */
        private static CraftingLootMenu instance;

        private CraftLootManager craftLootManager;

        private InventoryMenu playerInventory;
        /**Sprite de la texture de la fenêtre sur lequel apparait la description, liste des loots pour le craft... */
        private Sprite infoWindowSprite;
        private Texture slot64;

        //-----------Infos sur les slots de taille 64 (où on affiche les ingredients)
        private final int lootWidth64;
        private final int lootHeight64;
        private final int startSlotX64;// Position de départ de la première image de lootIngredient
        /**De bas en haut*/
        private final int startSlotY64;
        private final int padding64;//?
        private final int numColumns64;
        private final int marge64;

        /**Nombre de slots total dans les ingrédients de droite de craft*/
        private final int nbSlotsIngredients;
        /**Correspondant au slotSelected,(Pour affichage). Permet de pas rechercher 60 fois par secondes dans la hashmap quand on bouge pas */
        private LootableObjectType lootSelected;
        //private int slotRestantIngredients;
        /**Ingredients (pour affichage) nécessaire au craft de lootSelected */
        private List<LootStack> ingredientsSelected; // Pour le lootSelected    
    
        private CraftingLootMenu(String colorMenu) {
            super(colorMenu);
            this.infoWindowSprite = new Sprite(new Texture(Gdx.files.internal("src/main/resources/window_info_"+colorMenu+".png")));
            this.infoWindowSprite.setPosition(870,161);
            this.slot64 = new Texture(Gdx.files.internal("src/main/resources/slot_64_"+colorMenu+".png"));
            this.lootWidth64 = 64;
            this.lootHeight64 = 64;
            this.startSlotX64 = 890+20;
            this.startSlotY64 = 141+60;
            this.padding64 = 10;
            this.numColumns64 = 3;
            this.marge64 = 10;
            this.nbSlotsIngredients = 6;
        }

        public void initialize(CraftLootManager craftLootManager, InventoryMenu playerInventory){
            this.craftLootManager = craftLootManager;
            this.playerInventory = playerInventory;
            this.slotRestantMenu = nbSlotsMenu - craftLootManager.getAvailableLoots().size();
            this.lootSelected = craftLootManager.getAvailableLootsI(slotSelected);
            //this.slotRestantIngredients = nbSlotsIngredients;//UTILE ?
            this.ingredientsSelected= craftLootManager.getRecipeManager().getData(Pair.of(lootSelected.name(),1));

        }
        
        public static CraftingLootMenu getInstance() {
            if (instance == null) {
                instance = new CraftingLootMenu("BLUE");
            }
            return instance;
        }

        /** Appelle moveSlotSelected et met à jour également lootSelected et ingredientsSelected
         * @param direction "R" ou "L"
         * @return
         */
        public int moveSlotAndLootsSelected(String direction){//TODO: vers haut et bas aussi
            int newSlotSelected = moveSlotSelected(direction);
            //On défini le lootType(craft) et ses ingrédients selon le slot sélectionné
            lootSelected = craftLootManager.getAvailableLootsI(newSlotSelected);
            if(lootSelected !=null){
                ingredientsSelected = craftLootManager.getRecipeManager().getData((Pair.of(lootSelected.name(),1)));
            }

            return newSlotSelected; 
        }

        public void render(SpriteBatch batch) {
                // Affichez le menu à l'écran
                menuSprite.draw(batch);
    
                // Parcours du tableau de loots
                for (int i = 0; i < craftLootManager.getAvailableLoots().size(); i++) {
                    LootableObjectType lootType = craftLootManager.getAvailableLootsI(i);
                    //System.out.println("LootType: "+lootType.texture());
                    Pair<Integer,Integer> xy = getXYPositionLoot(i);
                    // Calcul de la position de l'image du loot
                    if(i == slotSelected){
                        selectedSlotSprite.setPosition(xy.getLeft(), xy.getRight());
                        selectedSlotSprite.draw(batch);
                    }
                    // Dessin de l'image du lootType craftable à sa position                    //! taille fake limite (16) ici   
                    batch.draw(lootType.texture(), xy.getLeft() + marge, xy.getRight() + marge,16+ lootWidth - (2*marge), 16+lootHeight  - (2*marge));
                }
                //TODO pour écrire
                drawTextCenterIn("CRAFTING STATION",640,600,2.0f,batch,FontManager.getInstance().getFontMenuTitle());
                //draw des ingredients du slot sélectionné, avec les ingredients
                if(ingredientsSelected !=null && lootSelected !=null){
                    infoWindowSprite.draw(batch);//Fenetre d'informations
                    drawTextCenterIn(lootSelected.name(),1000+20,425+60,1.5f,batch, FontManager.getInstance().getFontTitle2());//Nom du loot
                    FontManager.getInstance().getFontDescription().draw(batch, lootSelected.description(), 870+20,380+60,260,10,true);//Draw description
                    int j = 0;
                    for(LootStack ingredient : ingredientsSelected){
                        if(j> nbSlotsIngredients){break;}
                        Pair<Integer,Integer> xy64 = getXYPositionLoot64(j);
                        batch.draw(slot64,xy64.getLeft(), xy64.getRight());//cadre du slot                                                
                        batch.draw(ingredient.getTexture(), xy64.getLeft() + marge64, xy64.getRight() + marge64, lootWidth64 - (2*marge64), lootHeight64  - (2*marge64));
                        FontManager.getInstance().getFontDescription().draw(batch,playerInventory.getLootQuantity(ingredient.getLootType())+"/"+ingredient.getQuantity(), xy64.getLeft() +10, xy64.getRight()+15);//Exemple 2/3, ou 17/5
                        j++;
                    }
                    //TODO:bouton pour craft...
                }
        }
        
        /**Pour l'affichage des ingredients, de gauche à droite et bas en haut */
        public Pair<Integer,Integer> getXYPositionLoot64(int i){
            int x = startSlotX64 + (i % numColumns64) * (lootWidth64 + padding64);
            int y = startSlotY64 + (i / numColumns64) * (lootHeight64 + padding64);
            return Pair.of(x, y);
    
        }
    
        public void dispose() {

        }

        public CraftLootManager getCraftManager() {
            return craftLootManager;
        }

        public LootableObjectType getLootSelected() {
            return lootSelected;
        }
    
    }

