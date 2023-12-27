package aura_game.app.Objects;

//import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

//import aura_game.app.LootManager;
import aura_game.app.SpriteSheet.*;
import aura_game.app.Type.EntityType;

public class PlayableEntity extends Entity {

    /*Arme/outil qu'a actuellement le joueur en main, null/"" sinon */
    //private int currentToolIndex;//!remove
    //private Tool currentTool;//!remove

    private String currentToolName;

    
    /**SpriteSheet Texture de taille 64,128,192*/
    private final SpriteSheetInfo[] spriteTool;

    private int currentToolSpriteY;

    private int currentToolSizeSprite;

    /**Accès à l'unique instance de ToolsSpriteSheetData*/
    private final ToolsSpriteSheetData toolsspriteSheetData;

    public PlayableEntity(EntityType typeSheet, int speed) {
        super(typeSheet, speed, 20, 20);
        this.toolsspriteSheetData = ToolsSpriteSheetData.getInstance();
        this.spriteTool = new SpriteSheetInfo[]{new SpriteSheetInfo(64,"tools64"), new SpriteSheetInfo(128,"tools128"), new SpriteSheetInfo(192,"tools192")};
        this.currentToolSpriteY = -1;
        this.currentToolSizeSprite = -1;
        //this.currentToolIndex = -1;
        //this.currentTool = null;
        this.currentToolName = "";
    }

    /** @return L'index de l'arme/outil actuellement selectionné par la main du player*/
    /*public int getCurrentToolIndex(){
        return currentToolIndex;
    }*/



    /**Pour les entités on retourne la valeur par defaut.
     *  Décallage par rapport au point en bas a gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un coté touchant le rectangleHitbox (selon la direction) 
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    /*private Pair<Integer, Integer> getCurrentHitZonePointDecallage() {
        if(currentTool != -1){
            return 0;
        }
        return getHitZonePointDecallageDefault();
    }

    /**Pour les entités on retourne la valeur par defaut.
     * Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    /*private Pair<Integer, Integer> getCurrentHitZoneLenght() {
        return getHitZoneLenghtDefault();
    }*/


    /**
     * @return Le nom de l'arme selectionné, "" si il n'y en a pas
     */ /*
    public String getCurrentToolName(){
        if(currentToolIndex >=0 && currentToolIndex <= toolsspriteSheetData.getTools().length){
            return toolsspriteSheetData.getTools()[currentToolIndex];//TODO FAIRE SELON CEUX DISPO ET PAS TOUS
        }
        return "";
    }*/

    public String getCurrentToolName(){
        return currentToolName;
    }
    public void setCurrentToolName(String toolName){
        this.currentToolName = toolName;
    }

    /*
    public void setCurrentToolIndex(int currentTool){
        this.currentToolIndex = currentTool;
    }*/

    /** @return Le spriteY actuel de l'arme actuelle */
    public int getCurrentToolSpriteY(){
        return currentToolSpriteY;
    }

    /**@return La taille des sprites (64,128,192...) de l'arme actuelle*/
    public int getCurrentToolSizeSprite(){
        return currentToolSizeSprite;
    }
    /**
     * @return {@code true} si un outil/arme est actuellement selectionné, sinon {@code false}
     */
    public boolean isToolSelected(){
        String selec = getCurrentToolName();
        if ( selec == ""){ 
            return false;
        }
        return true;
    }

    /**
     * Appelé lorsque l'action actuelle change (dans changeAction()) et met à jour spriteY et size par rapport aux sprites de la tool
     *///TODO modif here: ùaj les 2
    public void updateSpriteToolInfo(){
        int[] actualActionTool = null;
        if(getCurrentToolName() != ""){//Alors une arme est selectionné
            actualActionTool =  getToolsspriteSheetData().getActionInfo(getCurrentToolName(), getActualActionName());
        }    
        if(actualActionTool != null){
            System.out.print(getCurrentToolName());
            currentToolSpriteY = actualActionTool[0];
            currentToolSizeSprite = actualActionTool[1];
            setCurrentBeginX(actualActionTool[2]);
            setCurrentSpriteX(actualActionTool[2]);
        }else{
            System.out.println("remove tool" );
            //currentToolIndex = -1;
            currentToolSpriteY = -1;
            currentToolSizeSprite = -1;
            setCurrentBeginX(0);
            setCurrentSpriteX(0);
        }
    }

    /**
     * Appelé lorsque le joueur a un tool en main dans l'inventaire et met à jour le currentToolIndex, 
     * le reste sera mis à jour selon se currentToolIndex en debut d'action par changeAction
     *  (car cela depend de l'action mais il ni en a pas pendant l'inventaire ouvert)
     */
    /*
    public void updateCurrentToolIndex(String tool){
            currentToolIndex = getToolsspriteSheetData().getToolIndex(tool);
    }*/


    /**
     * Selectionne le TextureRegion[][] correspondant a la size de la Sprite. Pour draw la bonne image de Tool/arme
     * @param sizeSprite
     * @return
     */
    public TextureRegion[][] getTextureRegionTool(int sizeSprite){
        switch( sizeSprite){
            case 64: 
                return spriteTool[0].spriteSheetRegions();
            case 128:
                return spriteTool[1].spriteSheetRegions();
            case 192:
               return spriteTool[2].spriteSheetRegions(); 
            default:
                throw new IllegalArgumentException("La taille"+ sizeSprite + "n'a pas de TextureRegion[][] correspondante");   

        }
    }
    /**TODO: pas suivant mais select dans inventory
     * Permet de selectionner l'outil suivant du tableau. 
     * On s'arrete a 1 après la fin du tableau pour permettre de déselectionner les armes quand on a fait le tour
     */
    /*
    public void selectNextTool(){
        currentTool = (currentTool + 1) % (toolsspriteSheetData.getTools().length);
        System.out.println("current tool is"+currentTool);

    }*/

    /**SpriteSheet Texture de taille 64,128,192*/
    public SpriteSheetInfo[] getSpriteTool() {
        return spriteTool;
    }

    /**Accès à l'unique instance de ToolsSpriteSheetData*/
    public ToolsSpriteSheetData getToolsspriteSheetData() {
        return toolsspriteSheetData;
    }

    /**
     * Modifie le tableau stockant la valeur de chaque sprite pour l'action en cours
     * en fonction de l'action actuelle qui permet de mettre à jour les valeurs de spritesActionDuration[]
     */
    public boolean updateSpriteDurationFromActionName(){
        String action = getActualActionName().substring(0, getActualActionName().length() - 2);
        switch (action) {
            case "Slash":
            //Si c'est un outil qui commence pas a 0 c'est donc un tool donc on veut la val 4, sinon par defaut 0+1
                this.getSpritesActionDuration()[3] = getCurrentBeginX()+ 1;
                this.getSpritesActionDuration()[4] = getCurrentBeginX()+ 1;
                this.getSpritesActionDuration()[5] = getDefaultSpriteDuration() + 1;
                return true;
            case "Shoot":
                this.getSpritesActionDuration()[7] = getDefaultSpriteDuration() + 1;
                this.getSpritesActionDuration()[8] = getDefaultSpriteDuration() + 2;

                return true;
                //Spellcast..., death
            default:
                this.getSpritesActionDuration()[3] = getDefaultSpriteDuration();
                this.getSpritesActionDuration()[4] = getDefaultSpriteDuration();
                this.getSpritesActionDuration()[5] = getDefaultSpriteDuration();
                this.getSpritesActionDuration()[7] = getDefaultSpriteDuration();
                this.getSpritesActionDuration()[8] = getDefaultSpriteDuration();
                return false;
            }
    }

}
