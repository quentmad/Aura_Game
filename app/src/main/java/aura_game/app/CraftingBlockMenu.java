package aura_game.app;

import aura_game.app.CraftableBlock.CraftBlockManager;
import aura_game.app.GameManager.FontManager;
import aura_game.app.rework.TypeEnum.BlockEntityType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
/**Semblable à l'InventoryMenu, mais on affiche à gauche les crafts pour blocks disponible
 * et à droite les ingrédients, taille nécessaire + description pour crafter le block sélectionné */
public class CraftingBlockMenu extends BasicMenu {

    /**Inventaire crafting du joueur, singleton */
    private static CraftingBlockMenu instance;

    private CraftBlockManager craftBlockManager;

    private InventoryMenu playerInventory;
    /**Sprite de la texture de la fenêtre sur lequel apparait la description, liste des loots pour le craft... */
    private Sprite infoWindowSprite;
    private Texture slot64;

    //-----------Infos sur les slots de taille 64 (où on affiche les ingredients)
    private final int blockWidth64;
    private final int blockHeight64;
    private final int startSlotX64;// Position de départ de la première image de blockIngredient
    /**De bas en haut*/
    private final int startSlotY64;
    private final int padding64;//?
    private final int numColumns64;
    private final int marge64;

    /**Nombre de slots total dans les ingrédients de droite de craft*/
    private final int nbSlotsIngredients;
    /**Correspondant au slotSelected,(Pour affichage). Permet de pas rechercher 60 fois par secondes dans la hashmap quand on bouge pas */
    private BlockEntityType blockSelected;
    //private int slotRestantIngredients;
    /**Ingredients (pour affichage) nécessaire au craft de lootSelected */
    private List<LootStack> ingredientsSelected;

    private CraftingBlockMenu(String colorMenu) {
        super(colorMenu);
        this.infoWindowSprite = new Sprite(new Texture(Gdx.files.internal("src/main/resources/window_info_"+colorMenu+".png")));
        this.infoWindowSprite.setPosition(870,161);
        this.slot64 = new Texture(Gdx.files.internal("src/main/resources/slot_64_"+colorMenu+".png"));
        this.blockWidth64 = 64;
        this.blockHeight64 = 64;
        this.startSlotX64 = 890+20;
        this.startSlotY64 = 141+60;
        this.padding64 = 10;
        this.numColumns64 = 3;
        this.marge64 = 10;
        this.nbSlotsIngredients = 6;
    }

    public void initialize(CraftBlockManager craftBlockManager, InventoryMenu playerInventory){
        this.craftBlockManager = craftBlockManager;
        this.playerInventory = playerInventory;
        this.slotRestantMenu = nbSlotsMenu - craftBlockManager.getAvailableBlockCraftable().size();
        this.blockSelected = craftBlockManager.getAvailableBlockCraftableI(slotSelected);
        //this.slotRestantIngredients = nbSlotsIngredients;//UTILE ?
        this.ingredientsSelected= craftBlockManager.getRecipeManager().getData(Pair.of(blockSelected.name(),1));

    }

    public static CraftingBlockMenu getInstance() {
        if (instance == null) {
            instance = new CraftingBlockMenu("BLUE");
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
        blockSelected = craftBlockManager.getAvailableBlockCraftableI(newSlotSelected);
        if(blockSelected !=null){
            ingredientsSelected = craftBlockManager.getRecipeManager().getData((Pair.of(blockSelected.name(),1)));
        }

        return newSlotSelected;
    }

    public void render(SpriteBatch batch) {
        // Affichez le menu à l'écran
        menuSprite.draw(batch);

        // Parcours du tableau de loots
        for (int i = 0; i < craftBlockManager.getAvailableBlockCraftable().size(); i++) {
            BlockEntityType blockType = craftBlockManager.getAvailableBlockCraftableI(i);
            //System.out.println("LootType: "+lootType.texture());
            Pair<Integer,Integer> xy = getXYPositionLoot(i);
            // Calcul de la position de l'image du block
            if(i == slotSelected){
                selectedSlotSprite.setPosition(xy.getLeft(), xy.getRight());
                selectedSlotSprite.draw(batch);
            }
            // Dessin de l'image du lootType craftable à sa position                    //! taille fake limite (16) ici
            batch.draw(blockType.texture(), xy.getLeft() + marge+10, xy.getRight() + marge,40+ blockType.imageWidth() - (2*marge), 40+blockType.imageHeight()  - (2*marge));
        }
        //TODO pour écrire
        drawTextCenterIn("CRAFTING BLOCK",640,600,2.0f,batch, FontManager.getInstance().getFontMenuTitle());
        //draw des ingredients du slot sélectionné, avec les ingredients
        if(ingredientsSelected !=null && blockSelected !=null){
            infoWindowSprite.draw(batch);//Fenetre d'informations
            drawTextCenterIn(blockSelected.name(),1000+20,425+60,1.5f,batch, FontManager.getInstance().getFontTitle2());//Nom du block
            FontManager.getInstance().getFontDescription().draw(batch, blockSelected.description(), 870+20,380+60,260,10,true);//Draw description
            int j = 0;
            for(LootStack ingredient : ingredientsSelected){
                if(j> nbSlotsIngredients){break;}
                Pair<Integer,Integer> xy64 = getXYPositionLoot64(j);
                batch.draw(slot64,xy64.getLeft(), xy64.getRight());//cadre du slot
                batch.draw(ingredient.getTexture(), xy64.getLeft() + marge64, xy64.getRight() + marge64, blockWidth64 - (2*marge64), blockHeight64 - (2*marge64));
                FontManager.getInstance().getFontDescription().draw(batch,playerInventory.getLootQuantity(ingredient.getLootType())+"/"+ingredient.getQuantity(), xy64.getLeft() +10, xy64.getRight()+15);//Exemple 2/3, ou 17/5
                j++;
            }
            //TODO:bouton pour craft...
        }
    }

    /**Pour l'affichage des ingredients, de gauche à droite et bas en haut */
    public Pair<Integer,Integer> getXYPositionLoot64(int i){
        int x = startSlotX64 + (i % numColumns64) * (blockWidth64 + padding64);
        int y = startSlotY64 + (i / numColumns64) * (blockHeight64 + padding64);
        return Pair.of(x, y);

    }

    public void dispose() {

    }

    public BlockEntityType getBlockSelected() {
        return blockSelected;
    }

    public CraftBlockManager getCraftBlockManager() {
        return craftBlockManager;
    }

}
