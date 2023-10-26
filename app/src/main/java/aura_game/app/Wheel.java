package aura_game.app;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aura_game.app.GameManager.AudioManager;
import aura_game.app.Objects.PlayableEntity;
import aura_game.app.Objects.Tool;

/**Par exemple Tool Ranged, Tool Melee, Sorts, Compétences */
public class Wheel<T>{

    private Sprite wheelMenuSprite;
    /**Tool actuel du joueur (il a enter dessus) */
    private Tool actualPlayerTool;
    /**Element selectionné (pas actuel) */
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
    public Wheel(String colorMenu,int totalSlot, int sizeSlot, String selectedSlotName, int slotActualToolDecalX) {

        wheelMenuSprite = new Sprite(new Texture(Gdx.files.internal("src/main/resources/wheelMenu_"+colorMenu+".png")));
        wheelMenuSprite.setCenter(900, 300);

        this.startMenuX = (int) wheelMenuSprite.getX();
        this.startMenuY = (int) wheelMenuSprite.getY();
        System.out.println("wheelMenu points is "+ startMenuX + " "+ startMenuY);
        this.content = new ArrayList<Tool>();
        this.positionsSlot = new int[]{169,399, 85,377, 24,316, 2,232, 24,148, 85,87,        169,65, 253,87, 314,148, 336,232, 314,316, 253,377} ;  //x1 y1.. x12 y12  
        this.selectedSlotIndex = 0;
        this.totalSlot = totalSlot;
        this.sizeSlot = sizeSlot;

        this.slotRestant = totalSlot;
        this.slotOccupied = 0;
        this.selectedSlotSprite = new Sprite(new Texture(Gdx.files.internal("src/main/resources/"+selectedSlotName+".png")));;
        this.selectedElement = null;
        this.actualPlayerTool = null;
        //Slot de taille 64*64 des tools actuels
        this.spriteActualToolSlot = new Sprite(new Texture(Gdx.files.internal("src/main/resources/Rond_slot64_Tool_jarques(blue).png")));
        this.spriteActualToolSlot.setPosition(990+slotActualToolDecalX, 50);//A 50 des bords
        this.audioManager = AudioManager.getInstance();

    }

    /** 
     * Ce lance lorsqu'on appuie sur {@code enter} quand le wheelMenu est ouvert.
     * Defini l'arme actuelle (le nom) de cette categorie du joueur à l'arme du selected slotIndex.
     * Defini le tool actuel a celui selectionné également.
     * @param player
     */
    public void setActualTool(PlayableEntity player){
        if(selectedElement !=null){
            this.actualPlayerTool = selectedElement;
            player.setCurrentMeleeToolName(selectedElement.getName());
            System.out.println(player.getCurrentMeleeToolName());
        }
    }




    /**
     * Ajoute à la liste l'élement en parametre si il reste de la place
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
                System.out.println("Added " + element.getLootType().getName() + " to "+this.toString());
                return true;
            }
        }
        System.out.println("Can't add " + element.getLootType().getName()+" to wheelMenu, there is no place anymore");
        return false;
    }

    /**
     * Retire à la liste l'élement en parametre
     * @param element 
     * @return {@code true} si le remove a fonctionné sinon {@code false}
     */
    public boolean remove(Tool element) {
        if(content.remove(element)){
            slotOccupied--;
            //Evite que le slot selectionné soit hors tableau car on a vider un slot
            if(selectedSlotIndex > (slotOccupied)-1 && selectedSlotIndex > 0 ){//Commence à 0
                selectedSlotIndex--;
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
        if(direction == "R" && selectedSlotIndex > 0){
            audioManager.playSound(audioManager.getSoundMoveSelectedItem(), 0.1f);
                selectedSlotIndex--;
                selectedElement = content.get(selectedSlotIndex);
                this.selectedSlotSprite.setPosition(startMenuX+positionsSlot[selectedSlotIndex*2], startMenuY + positionsSlot[selectedSlotIndex*2+1]-sizeSlot);
        }
        else if(direction == "L"  && selectedSlotIndex < slotOccupied -1){
            audioManager.playSound(audioManager.getSoundMoveSelectedItem(), 0.1f);
                selectedSlotIndex++;
                selectedElement = content.get(selectedSlotIndex);
                this.selectedSlotSprite.setPosition(startMenuX+positionsSlot[selectedSlotIndex*2], startMenuY + positionsSlot[selectedSlotIndex*2+1]-sizeSlot);
        }
        
    }
    
    /**
     * Met l'index du slot selectionné à index, et le l'élément selectionné à l'élement du content correspondant
     */
    public void selectMeleeTool(int index) {
        if (index >= 0 && index < totalSlot) {
            selectedSlotIndex = index;
            selectedElement = content.get(selectedSlotIndex);
            this.selectedSlotSprite.setPosition(startMenuX+ positionsSlot[selectedSlotIndex*2], startMenuY +  positionsSlot[selectedSlotIndex*2+1]-sizeSlot);//! mauvais calcul du coup
        }

    }

    /**Affiche a l'ecran le slot rond et le tool actuel du joueur, qui ont déjà une position défini,
     * Si actualPlayerTool est null, ne fait rien
     */
    public void renderActualTool(SpriteBatch batch) {
        if(actualPlayerTool !=null){
            spriteActualToolSlot.draw(batch);
            System.out.println(spriteActualToolSlot.getX()+"y:"+ spriteActualToolSlot.getY());
            batch.draw(actualPlayerTool.getTextureBlackAndWhite(),spriteActualToolSlot.getX(), spriteActualToolSlot.getY());
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






