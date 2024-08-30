package aura_game.app.CraftableBlock;
import java.util.ArrayList;
import java.util.List;

import aura_game.app.GameManager.FontManager;
import aura_game.app.GameManager.Game;
import aura_game.app.Notifications.Notification;
import aura_game.app.Notifications.NotificationManager;
import aura_game.app.rework.ActorEntity;
import aura_game.app.rework.Debug;
import aura_game.app.rework.Point;
import aura_game.app.rework.Region;
import aura_game.app.rework.TypeEnum.BlockEntityType;
import aura_game.app.rework.TypeEnum.LootableObjectType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.apache.commons.lang3.tuple.Pair;

import aura_game.app.HashmapManager;
import aura_game.app.InventoryMenu;
import aura_game.app.LootStack;

/**Craft pour les blocks qu'on place et craft via le Plan*/
public class CraftBlockManager {

    private Region currentRegion;
    /**Un nom et une quantité à partir d'une liste de loots avec quantité*/
    private HashmapManager<Pair<String,Integer>, List<LootStack>> recipeManager;
    /** Liste des blocks en cours de création (location validé ou non)*/
    private List<BlockPlan> blocksInCreation;
    private Texture textureArrowWhiteRight;
    private Texture textureArrowWhiteUp;
    private Texture slotRemainingLoot;
    private Texture slotContour;
    private Texture textureCancel;

    /**Slot sélectionné pour ajouter un élément à un block en cours de création (parmis les RemainingLoots)*/
    private int selectedSlotToAdd;
    /**Block le plus proche du joueur, pour ajouter des éléments (si - de 100 de distance)*/
    private BlockPlan closestBlock;


    /**Contient les loots disponible au craft. Permet d'afficher dans le menu tous les loots craftable,
     * et retrouvé via selectedSlot les ingredients du craft sélectionné pour les afficher  */
    private List<BlockEntityType> availableBlockCraftable;
    private InventoryMenu inventoryMenu;
    private boolean inPlacementMode;

    public CraftBlockManager() {
        this.currentRegion = Game.getInstance().getRegion();
        this.recipeManager = new HashmapManager<>();
        this.blocksInCreation = new ArrayList<>();
        this.availableBlockCraftable = new ArrayList<>();
        this.textureArrowWhiteRight = new Texture("src/main/resources/UI/arrow_right.png");
        this.textureArrowWhiteUp = new Texture("src/main/resources/UI/arrow_up.png");
        this.slotRemainingLoot = new Texture("src/main/resources/slot32pix_BLUE.png");
        this.slotContour = new Texture("src/main/resources/contour_slot_32v2.png");
        this.textureCancel = new Texture("src/main/resources/UI/X.png");
        this.inventoryMenu = InventoryMenu.getInstance();
        initHashmap();
        initAvailableLoot();
        this.inPlacementMode = false;
        this.selectedSlotToAdd = 0;
        this.closestBlock = null;

    }

    /**Loots disponible à crafter */
    public List<BlockEntityType> getAvailableBlockCraftable(){
        return availableBlockCraftable;
    }

    public HashmapManager<Pair<String,Integer>, List<LootStack>> getRecipeManager(){
        return recipeManager;
    }

    /**@return le lootType d'availableLoots à l'emplacement i*/
    public BlockEntityType getAvailableBlockCraftableI(int i){
        return availableBlockCraftable.get(i);
    }

    public void addToBlockInCreation(BlockPlan blockPlan, ActorEntity actor){
        System.out.println("before adding : blocksInCreation size : "+blocksInCreation.size());
        this.blocksInCreation.add(blockPlan);
        System.out.println("after adding : blocksInCreation size : "+blocksInCreation.size());
        this.inPlacementMode = true;
        Game.getInstance().getRegion().interactionComponent().abstractObjectsOnGround().add(blockPlan);
        blockPlan.setRemainingLoots(copyLootStackList(recipeManager.getData(Pair.of(blockPlan.block().name(),1))));
        moveLocationBlockPlan(actor, 0,0);//To set the collision color
        System.out.println("Block in creation added : "+blockPlan.block().name());
    }

    public boolean removeToBlockInCreation(int blockPlanID){
        for(BlockPlan bp : blocksInCreation){
            if(blockPlanID == bp.id()){
                Game.getInstance().getRegion().interactionComponent().abstractObjectsOnGround().remove(bp);
                return blocksInCreation.remove(bp);
            }
        }
        return false;
    }

    public int getBlocksInCreationSize(){
        return blocksInCreation.size();
    }

    /**
     * Permet de valider l'emplacement d'un block dans le plan, qui pourra ensuite etre crafté, si il est a moins de 100 de distance
     * @return
     */
    public boolean validatePlaceBlockPlan(ActorEntity actor){
        //validate the block in the list of blocks in creation
        for(BlockPlan blockPlan : blocksInCreation){
            if(!blockPlan.isLocationChoosen() && blockPlan.isLocationPossible() && blockPlan.block().calculateDistance(actor) < 100){
                blockPlan.setLocationChoosen(true);
                inPlacementMode = false;
                NotificationManager.getInstance().addNotification(new Notification("Block "+blockPlan.block().name()+" placed", NotificationManager.NotificationPlace.BOTTOM_RIGHT, 1,"src/main/resources/windowsMenu2BadColor.png", FontManager.getInstance().getFontDescription(), 5, false));//BAD COLOR PNG
            return true;
            }
        }
        return false;
    }

    /** Craft
     * Si le selectedSlotToAdd est le dernier de la liste des loots restants, on annule le craft (X)
     * Ajoute un element du selectedSlotElement au block en création le plus proche, si son emplacement a été choisi.
     * Si l'action a réussi, on retire les loots de l'inventaire et on les ajoute au block en création
     * Si l'action a réussi et qu'il ne manque plus rien on place le block sur la carte, le craft est fini,
     * @return
     */
    public boolean addElementToClosestBlockInCreation() {
        if(selectedSlotToAdd == recipeManager.getData(Pair.of(closestBlock.block().name(),1)).size()){
            System.out.println("cancel");
            removeToBlockInCreation(closestBlock.id());
            Game.getInstance().getRegion().interactionComponent().abstractObjectsOnGround().remove(closestBlock);
            closestBlock = null;
            selectedSlotToAdd = 0;
            return false;
        }
        LootableObjectType element = closestBlock.getRemainingLoots().get(selectedSlotToAdd).getLootType();
        System.out.println("test adding element : "+element.name());
        //Si l'emplacement est choisi et que c'est le bon block
        if (closestBlock.isLocationChoosen()) {
            System.out.println("step1 ok");
            System.out.println("has loot: "+ (inventoryMenu.hasLoot(element, 1)));
            //Si l'element est présent dans l'inventaire du joueur et encore nécessaire dans le craft
            if (inventoryMenu.hasLoot(element, 1) && closestBlock.removeOneElementFromRemainingLoots(element)) {
                slideSelectedSlotToAdd(0);//Assure que le slot sélectionné est bien dans les limites
                System.out.println("step2 ok");
                //On retire l'element de l'inventaire
                inventoryMenu.removeFromInventory(element, 1);

                //Si le craft est fini
                if (closestBlock.isRemainingLootsEmpty()) {
                    //On retire le block de la liste des blocks en création
                    System.out.println("remove " + removeToBlockInCreation(closestBlock.id()) + blocksInCreation.size());
                    currentRegion.addBlockOnMap(closestBlock.block());
                    selectedSlotToAdd = 0;
                    this.closestBlock = null;
                    System.out.println("craft finished");
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * Permet de déplacer un block dans le plan, si l'emplacement n'est pas encore validé
     * @param dx deplacement en x
     * @param dy deplacement en y
     * @return
     */
    public boolean moveLocationBlockPlan(ActorEntity actor, int dx, int dy){
        //move the block in the list of blocks in creation
        for(BlockPlan blockPlan : blocksInCreation){
            if(!blockPlan.isLocationChoosen()){
                blockPlan.setLocationPossible((blockPlan.block().physics().isColliding(blockPlan.block(), new Point(blockPlan.block().posC().x() + dx,blockPlan.block().posC().y() + dy))==0));
                blockPlan.block().move(dx,dy);
                Game.getInstance().getRegion().interactionComponent().abstractObjectsOnGround().setNeedSort(true);
                //Annulation : TODO PLUS PROPRE
                if(blockPlan.block().calculateDistance(actor) > 100)moveLocationBlockPlan(actor, -dx, -dy);

                return true;
            }
        }
        return false;
    }

    /**Ajout des crafts /ingrédients à la hashmap
     * Il y a tous les craft dès l'initialisation
     */
    public void initHashmap() {

        List<LootStack> ingredientsBox = new ArrayList<>() {{
            add(new LootStack(LootableObjectType.stick, 5));
            add(new LootStack(LootableObjectType.branch, 4));
        }};
        recipeManager.addData(Pair.of("box",1), ingredientsBox);
    }

    /**Initialise les craft disponible dès le début du jeu, on débloque les autres au fur et à mesure */
    public void initAvailableLoot() {
        availableBlockCraftable.add(BlockEntityType.box);
    }

    /**
     * Render les fleches, infos de validation...
     * @param batch
     */
    public void render(SpriteBatch batch){
        if(blocksInCreation.size() > 0) {
            renderRemainingLoots(batch, closestBlock);
            for (BlockPlan blockPlan : blocksInCreation) {
                if (!blockPlan.isLocationChoosen()) {//max 1 pas encore placé
                    Debug.getInstance().drawTextBeginIn("Placement en cours", 100, 500, 2.5f, batch, FontManager.getInstance().getFontDescription());
                    int pX = blockPlan.block().getPosOnScreenX(currentRegion.camera().position().x());
                    int pY = blockPlan.block().getPosOnScreenY(currentRegion.camera().position().y());
                    //Right arrow
                    batch.draw(textureArrowWhiteRight, pX + blockPlan.block().imageWidth(), pY);//recalculer
                    //Left arrow (rotation of the right arrow)
                    batch.draw(textureArrowWhiteRight, pX - textureArrowWhiteRight.getWidth(), pY, textureArrowWhiteRight.getWidth(), textureArrowWhiteRight.getHeight(), 0, 0, textureArrowWhiteRight.getWidth(), textureArrowWhiteRight.getHeight(), true, false);
                    //Up arrow
                    batch.draw(textureArrowWhiteUp, pX, pY + blockPlan.block().hitbox().height + 6);
                    //Down arrow (rotation of the up arrow)
                    batch.draw(textureArrowWhiteUp, pX, pY - textureArrowWhiteUp.getHeight(), textureArrowWhiteUp.getWidth(), textureArrowWhiteUp.getHeight(), 0, 0, textureArrowWhiteUp.getWidth(), textureArrowWhiteUp.getHeight(), false, true);

                }
            }
        }
    }

    /**Affiche ce qu'il reste à craft (près du block) pour le block en cours de création*/
    public void renderRemainingLoots(SpriteBatch batch, BlockPlan blockPlan){
        if(blockPlan != null){
            int totalWidthSlots = (blockPlan.getRemainingLoots().size()) * 48 + 6;
            List<LootStack> recipe = recipeManager.getData(Pair.of(blockPlan.block().name(),1));
            System.out.println("Rendering remaining loots of " + blockPlan.block().name() + blockPlan.getRemainingLoots().size() );
            int pX = blockPlan.block().getPosOnScreenX(currentRegion.camera().position().x());
            int pY = blockPlan.block().getPosOnScreenY(currentRegion.camera().position().y());
            for(int i = 0; i < recipe.size()+1; i++){
                if(i < recipe.size())
                batch.draw(slotRemainingLoot, pX + i * 48  - totalWidthSlots/2, pY - blockPlan.block().offsetY() + slotRemainingLoot.getHeight() + 6);
                if(i == selectedSlotToAdd){
                    batch.draw(slotContour, pX + i * 48 - totalWidthSlots/2, pY - blockPlan.block().offsetY() + slotRemainingLoot.getHeight() + 6);
                }
                if(i < recipe.size()) {
                    batch.draw(recipe.get(i).getTexture(), pX+ i * 48 - totalWidthSlots / 2, pY - blockPlan.block().offsetY() + slotRemainingLoot.getHeight() + 6);
                    String msg = (recipe.get(i).getQuantity() - blockPlan.getRemainingLoots().get(i).getQuantity()) + "/" + recipe.get(i).getQuantity();
                    Debug.getInstance().drawTextBeginIn(msg, pX + i * 48 - totalWidthSlots / 2, pY + slotRemainingLoot.getHeight() + 10, 0.9f, batch, FontManager.getInstance().getFontDescription());
                }else{
                    batch.draw(textureCancel,pX + i * 48 - totalWidthSlots/2, pY - blockPlan.block().offsetY() + 1 + slotRemainingLoot.getHeight() + 6);
                }

            }
        }
    }

    /**
     * Déplace le slot sélectionné pour ajouter un élément à un block en cours de création (ou cancel)
     * w : -1
     * x : 1
     * @param i
     */
    public void slideSelectedSlotToAdd(int i){
        selectedSlotToAdd += i;
        if(selectedSlotToAdd < 0) selectedSlotToAdd = 0;
        else if(selectedSlotToAdd > closestBlock.getRemainingLoots().size() || (selectedSlotToAdd < closestBlock.getRemainingLoots().size() && closestBlock.getRemainingLoots().get(selectedSlotToAdd).getQuantity() == 0)) {
            selectedSlotToAdd = closestBlock.getFirstNotEmptyRemainingLootIndex();
        }
    }



    public BlockPlan getClosestBlock() {
        return closestBlock;
    }

    /**
     * @return le plus proche des blocks en création (déjà placé)) si il est à moins de 100 de distance du joueur
     */
    public void calculClosestBlockInCreation(ActorEntity actor){
        if(blocksInCreation.size() == 0) return;
        BlockPlan closestBlockl = null;
        double minDistance = Double.MAX_VALUE;
        for(BlockPlan blockPlan : blocksInCreation){
            if(blockPlan.isLocationChoosen() && blockPlan.block().calculateDistance(actor) < minDistance){
                minDistance = blockPlan.block().calculateDistance(actor);
                closestBlockl = blockPlan;
            }
        }
        if(minDistance < 50){
                System.out.println("Closest block : " + closestBlockl.block().name());
                closestBlock = closestBlockl;
        }else{
            System.out.println("No block close enough to add element");
            closestBlock = null;
            selectedSlotToAdd = 0;
        }
    }

    public List<LootStack> copyLootStackList(List<LootStack> original) {
        List<LootStack> copy = new ArrayList<>();
        for (LootStack lootStack : original) {
            copy.add(new LootStack(lootStack.getLootType(), lootStack.getQuantity()));
        }
        return copy;
    }


}

