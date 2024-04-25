package aura_game.app;

import java.util.ArrayList;
import java.util.List;

import aura_game.app.Type.ToolType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aura_game.app.GameManager.AudioManager;
import aura_game.app.Objects.PlayableEntity;
import aura_game.app.Objects.Tool.Tool;

/**Par exemple Tool Ranged, Tool Melee, Sorts, Compétences */
public class Wheel<T>{

    private final String NAME;
    private Sprite wheelMenuSprite;

    /**Tool actuel favori  du joueur (il a fait enter dessus) pour cette catégorie (ce wheel) */
    private Tool actualFavoriteToolForThisWheel;
    /**Element selectionné (pas actuel) pour ce wheel */
    private Tool selectedElement;
    private List<Tool> content;
    /**Positions (bas a gauche) d'affichage du slot 64*64 x1 y1.. x6 y6*/
    private final int[] positionsSlot;
    /*Indice de l'élément sélectionnée (pas actuel du joueur)*/
    private int selectedSlotIndex;
    /**Slot au maximum dans le content*/
    private int totalSlot;
    private final int sizeSlot;
    private int slotRestant;
    private int slotOccupied;
    /**X de début du wheel (gauche) */
    private int startMenuX;
    /**Y de début du wheel (bas) */
    private int startMenuY;
    private final Sprite selectedSlotSprite;   


    /**Slot rond de taille 64*64 situé en bas à droite de l'image, possède le meleeWeapons actuel du joueur*/
    private Sprite spriteActualToolSlot;


    private AudioManager audioManager;    


    /**Par exemple Tool Ranged, Tool Melee, Sorts, Compétences */
    public Wheel(String name, String colorMenu,int totalSlot, int sizeSlot, String selectedSlotName, int slotActualToolDecalX) {

        this.NAME = name;
        wheelMenuSprite = new Sprite(new Texture(Gdx.files.internal("src/main/resources/wheelMenu_"+colorMenu+".png")));
        wheelMenuSprite.setCenter(900, 300);

        this.startMenuX = (int) wheelMenuSprite.getX();
        this.startMenuY = (int) wheelMenuSprite.getY();
        System.out.println("wheelMenu points is "+ startMenuX + " "+ startMenuY);
        this.content = new ArrayList<>();
        this.positionsSlot = new int[]{169,399, 85,377, 24,316, 2,232, 24,148, 85,87,        169,65, 253,87, 314,148, 336,232, 314,316, 253,377} ;  //x1 y1.. x12 y12  
        this.selectedSlotIndex = 0;
        this.totalSlot = totalSlot;
        this.sizeSlot = sizeSlot;

        this.slotRestant = totalSlot;
        this.slotOccupied = 0;
        this.selectedSlotSprite = new Sprite(new Texture(Gdx.files.internal("src/main/resources/"+selectedSlotName+".png")));
        this.selectedElement = null;
        this.actualFavoriteToolForThisWheel = null;
        //Slot de taille 64*64 des tools actuels
        this.spriteActualToolSlot = new Sprite(new Texture(Gdx.files.internal("src/main/resources/Rond_slot64_Tool_jarques(blue).png")));
        this.spriteActualToolSlot.setPosition(990+slotActualToolDecalX, 50);//A 50 des bords
        this.audioManager = AudioManager.getInstance();

    }

    /** 
     * Ce lance lorsqu'on appuie sur {@code enter} quand le wheelMenu est ouvert.
     * Defini l'arme actuelle (le nom) de cette categorie du joueur à l'arme du selected slotIndex.
     * Defini le tool actuel pour cette category à celui selectionné également.
     * <p> Attention: set le tool actuel du type, mais pas directement le tool du joueur
     * @param player
     */
    public void setActualToolForCategory(PlayableEntity player){
        //if(selectedElement !=null){
            this.actualFavoriteToolForThisWheel = selectedElement;
        //}
    }

    /**
     * Defini l'arme actuelle du joueur (en main) a l'arme actuelle favorite de ce wheel (soit l'arme actuelle de melee, soit ranged) (set ou update)
     * {@code Met à jour } les informations de l'outil actuellement équipé ainsi que la currentEquippedToolCategory à cette category si besoin
     * @param player
     */
    public void setActualEquippedToolToFavoriteOfThisWheel(PlayableEntity player){
        if(actualFavoriteToolForThisWheel !=null) {
            player.getToolManager().setCurrentEquippedToolName(actualFavoriteToolForThisWheel.getName());
            player.getToolManager().setCurrentEquippedTool(actualFavoriteToolForThisWheel);
            player.getToolManager().haveAToolEquippedNow();
            if (!player.getToolManager().getCurrentEquippedToolCategory().equals(NAME)) {//Si pas c'est la catégorie actuelle
                player.getToolManager().setCurrentEquippedToolCategory(NAME);//Met à jour la catégorie de l'outil actuel du joueur
            }
            player.getToolManager().updateEquippedToolInfo();//Met à jour les informations de l'outil actuellement équipé car l'arme équipé va directement changer (soit car on a modifié l'arme qui était de la catégorie actuelle, soit car on a changé de catégorie d'arme)

        }else{
            player.getToolManager().setCurrentEquippedToolName("");
            player.getToolManager().setCurrentEquippedTool(null);
        }
    }

    public String getNAME() {
        return NAME;
    }

    /**
     * Ajoute à la liste l'élement en parametre s'il reste de la place
     * @param element 
     * @return true si l'ajout a fonctionné sinon false
     * 
     */
    public boolean add(Tool element) {
        boolean result= false;
        if (slotRestant > 0  ) {
            result = content.add(element);
            if(result){
                slotOccupied++;
                slotRestant--;
                System.out.println("Added " + element.getLootType().getName() + " to "+ this);
                //Si c'est le premier ajout, on le met en selected
                if(slotOccupied == 1){
                    selectedElement = element;
                    this.selectedSlotSprite.setPosition(startMenuX+positionsSlot[selectedSlotIndex*2], startMenuY + positionsSlot[selectedSlotIndex*2+1]-sizeSlot);
                }
                return true;

            }
        }
        System.out.println("Can't add " + element.getLootType().getName()+" to wheelMenu, there is no place anymore");
        return false;
    }

    /**
     * Supprime un outil du menu Wheel.
     * Si l'outil est présent dans le menu, il est retiré, le nombre de slots occupés est décrémenté et le nombre de slots restants est incrémenté.
     * Si l'index du slot sélectionné est supérieur au nombre de slots occupés (après suppression) et supérieur à 0, l'index du slot sélectionné est décrémenté.
     * La position du sprite du slot sélectionné est ensuite mise à jour.
     *
     * @param element L'outil à supprimer du menu.
     * @return {@code true} si l'outil a été supprimé avec succès, {@code false} sinon.
     */
    public boolean remove(Tool element) {
        if(content.remove(element)){
            slotOccupied--;
            slotRestant++;
            //Evite que le slot selectionné soit hors tableau car on a vider un slot
            if(selectedSlotIndex > (slotOccupied)-1 && selectedSlotIndex > 0 ){//Commence à 0
                selectedSlotIndex--;
                this.selectedSlotSprite.setPosition(startMenuX+positionsSlot[selectedSlotIndex*2], startMenuY + positionsSlot[selectedSlotIndex*2+1]-sizeSlot);
            }
            return true;
        }
        return false;
    }

    /**
     * Jette l'outil actuellement sélectionné du menu Wheel sur le terrain.
     * Si l'outil sélectionné n'est pas null, il est retiré du menu Wheel et un nouvel objet de type Loot est créé à la position du joueur.
     * Si l'outil jeté est l'outil actuellement favori du joueur pour cette catégorie d'outil, l'outil favori est mis à jour.
     *
     * @param player L'entité jouable qui jette l'outil.
     * @return {@code true} si l'outil a été jeté avec succès, {@code false} sinon.
     */
    public boolean drop(PlayableEntity player){
        if(selectedElement != null){
            Tool oldSelected = selectedElement;
            ToolType tt= ToolType.valueOf(selectedElement.getName());
            remove(selectedElement);
            int mx = player.getEntityStateMachine().getCurrentOrientation().getX();
            int my = player.getEntityStateMachine().getCurrentOrientation().getY();
            LootManager.getInstance().spawnTool(tt,player.getLootSpawnCenterX(tt.lootType().width()), player.getPosC_Y()+tt.lootType().offY(),true, LootManager.getInstance().getJumpVec(mx, my, 1), selectedElement.getSolidity());
            selectedElement = content.isEmpty() ? null : content.get(selectedSlotIndex);
            if(selectedElement ==null){
                System.out.println("    Selected element is null");
            }
            //Besoin de refresh le tool actuel du joueur si c'est celui qu'on a drop
            if(selectedElement ==null || (actualFavoriteToolForThisWheel !=null && actualFavoriteToolForThisWheel.getName().equals(oldSelected.getName()))){
                setActualToolForCategory(player);//Met à jour l'arme actuelle de la category
                setActualEquippedToolToFavoriteOfThisWheel(player);
                System.out.println("Droped the actual tool of the player");
            }
            return true;
        }
        return false;
    }

    /**Bouge le slotSelected de WheelMenu 
     * @param direction "R" ou "L"
     */
    public void moveSlotSelected(String direction){
        //Si on appuie sur gauche et qu'on peut encore aller à gauche
        if(direction.equals("R") && selectedSlotIndex > 0){
            audioManager.playSound(audioManager.getSoundMoveSelectedItem(), 0.1f);
                selectedSlotIndex--;
                selectedElement = content.get(selectedSlotIndex);
                this.selectedSlotSprite.setPosition(startMenuX+positionsSlot[selectedSlotIndex*2], startMenuY + positionsSlot[selectedSlotIndex*2+1]-sizeSlot);
        }
        else if(direction.equals("L") && selectedSlotIndex < slotOccupied -1){
            audioManager.playSound(audioManager.getSoundMoveSelectedItem(), 0.1f);
                selectedSlotIndex++;
                selectedElement = content.get(selectedSlotIndex);
                this.selectedSlotSprite.setPosition(startMenuX+positionsSlot[selectedSlotIndex*2], startMenuY + positionsSlot[selectedSlotIndex*2+1]-sizeSlot);
        }
        
    }
    
    /**
     * Met l'index du slot selectionné à index, et l'élément selectionné à l'élement du content correspondant
     */
    public void selectMeleeTool(int index) {
        if (index >= 0 && index < totalSlot) {
            selectedSlotIndex = index;
            selectedElement = content.get(selectedSlotIndex);
            this.selectedSlotSprite.setPosition(startMenuX+ positionsSlot[selectedSlotIndex*2], startMenuY +  positionsSlot[selectedSlotIndex*2+1]-sizeSlot);//! mauvais calcul du coup
        }

    }

    /**Affiche à l'ecran le slot rond et le tool actuel du joueur, qui ont déjà une position défini,
     * Si actualPlayerTool est null, ne fait rien
     */
    public void renderActualTool(SpriteBatch batch) {
        if(actualFavoriteToolForThisWheel !=null){
            spriteActualToolSlot.draw(batch);
            batch.draw(actualFavoriteToolForThisWheel.getTextureBlackAndWhite(),spriteActualToolSlot.getX(), spriteActualToolSlot.getY());
        }
    }


    //TODO drop ???
    public void render(SpriteBatch batch, BitmapFont font){
        wheelMenuSprite.draw(batch);
        int i = 0;
        for(Tool element : content){
            batch.draw(element.getTextureBlackAndWhite(),startMenuX +  positionsSlot[i*2], startMenuY   + positionsSlot[i*2+1]-sizeSlot);
        i++;
        }
        if(slotOccupied > 0){
            selectedSlotSprite.draw(batch);
        }
    }        

}






