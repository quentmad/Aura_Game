package aura_game.app.Objects;

//import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

//import aura_game.app.LootManager;
import aura_game.app.SpriteSheet.*;
import aura_game.app.Type.EntityType;

public class PlayableEntity extends Entity {

    //---- Tool -----
    private String currentToolName;

    
    /**SpriteSheet Texture de taille 64,128,192*/
    private final SpriteSheetInfo[] spriteTool;

    private int currentToolSpriteY;

    private int currentToolSizeSprite;

    /**Accès à l'unique instance de ToolsSpriteSheetData*/
    private final ToolsSpriteSheetData toolsspriteSheetData;
    //-----

    public PlayableEntity(EntityType typeSheet, int speed) {
        super(typeSheet, speed, 20, 20);
        this.toolsspriteSheetData = ToolsSpriteSheetData.getInstance();
        this.spriteTool = new SpriteSheetInfo[]{new SpriteSheetInfo(64,"tools64"), new SpriteSheetInfo(128,"tools128"), new SpriteSheetInfo(192,"tools192")};
        this.currentToolSpriteY = -1;
        this.currentToolSizeSprite = -1;
        this.currentToolName = "";
    }

    public String getCurrentToolName(){
        return currentToolName;
    }
    public void setCurrentToolName(String toolName){
        this.currentToolName = toolName;
    }

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
        return !getCurrentToolName().isEmpty();
    }

    /**
     * Appelé lorsque l'action actuelle change (dans changeAction()) et met à jour spriteY et size par rapport aux sprites de la tool
     *///TODO modif here: maj les 2
    public void updateSpriteToolInfo(){
        int[] actualActionTool = null;
        System.out.println("current tool empty ? :"+!getCurrentToolName().equals(""));
        if(!getCurrentToolName().equals("")){//Alors une arme est selectionné
            actualActionTool =  getToolsspriteSheetData().getActionInfo(getCurrentToolName(), getEntityStateMachine().getCurrentStateName());
        }    
        if(actualActionTool != null){
            System.out.print(getCurrentToolName());
            currentToolSpriteY = actualActionTool[0];
            currentToolSizeSprite = actualActionTool[1];
            setCurrentBeginX(actualActionTool[2]);
            //setCurrentSpriteX(actualActionTool[2]);
        }else{
            System.out.println("remove tool" );
            //currentToolIndex = -1;
            currentToolSpriteY = -1;
            currentToolSizeSprite = -1;
            setCurrentBeginX(0);
            //setCurrentSpriteX(0);
        }
    }

    /**
     * Sélectionne le TextureRegion[][] correspondant à la size de la Sprite. Pour draw la bonne image de Tool/arme
     * @param sizeSprite
     * @return Le TextureRegion[][] correspondant
     */
    public TextureRegion[][] getTextureRegionTool(int sizeSprite){
        return switch (sizeSprite) {
            case 64 -> spriteTool[0].spriteSheetRegions();
            case 128 -> spriteTool[1].spriteSheetRegions();
            case 192 -> spriteTool[2].spriteSheetRegions();
            default ->
                    throw new IllegalArgumentException("La taille" + sizeSprite + "n'a pas de TextureRegion[][] correspondante");
        };
    }


    /**SpriteSheet Texture de taille 64,128,192*/
    public SpriteSheetInfo[] getSpriteTool() {
        return spriteTool;
    }

    /**Accès à l'unique instance de ToolsSpriteSheetData*/
    public ToolsSpriteSheetData getToolsspriteSheetData() {
        return toolsspriteSheetData;
    }


}
