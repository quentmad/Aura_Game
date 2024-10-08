package aura_game.app;

import aura_game.app.GameManager.Game;
import aura_game.app.SpriteSheet.SpriteSheetInfo;
import aura_game.app.SpriteSheet.ToolsSpriteSheetData;
import aura_game.app.rework.Tool;
import aura_game.app.rework.ToolWieldingEntity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ToolManager {

    private boolean haveAToolEquipped;
    private ToolWieldingEntity character;
    /**Nom de l'outil actuel (dans la main) */
    private String currentEquippedToolName;
    private Tool currentEquippedTool;
    private String currentEquippedToolCategory;

    /**SpriteSheet Texture de taille 64,128,192*/
    private final SpriteSheetInfo[] spriteTool;

    private int currentToolSpriteY;

    private int currentToolSizeSprite;

    /**Accès à l'unique instance de ToolsSpriteSheetData*/
    private final ToolsSpriteSheetData toolsspriteSheetData;

    public ToolManager(ToolWieldingEntity character) {
        this.character = character;
        this.toolsspriteSheetData = ToolsSpriteSheetData.getInstance();
        //Par défaut: pas d'arme selectionné
        this.spriteTool = new SpriteSheetInfo[]{new SpriteSheetInfo(64,"tools64"), new SpriteSheetInfo(128,"tools128"), new SpriteSheetInfo(192,"tools192")};
        this.currentToolSpriteY = -1;
        this.currentToolSizeSprite = -1;
        this.currentEquippedToolName = "";
        this.currentEquippedToolCategory = "";
        this.currentEquippedTool = null;
        this.haveAToolEquipped = false;
    }



    public String getCurrentEquippedToolName(){
        return currentEquippedToolName;
    }
    public void setCurrentEquippedToolName(String toolName){
        this.currentEquippedToolName = toolName;
    }

    public void setCurrentEquippedTool(Tool tool){
        this.currentEquippedTool = tool;
    }

    public Tool getCurrentEquippedTool(){
        return currentEquippedTool;
    }

    public String getCurrentEquippedToolCategory(){
        return currentEquippedToolCategory;
    }
    public void setCurrentEquippedToolCategory(String toolCategory){
        this.currentEquippedToolCategory = toolCategory;
    }

    /**Permet d'update les infos sur l'outil actuellement équipé que s'il y a une arme d'équipé*/
    public void haveAToolEquippedNow(){
        this.haveAToolEquipped = true;
    }

    public boolean haveAToolEquipped(){
        return haveAToolEquipped;
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
        return !getCurrentEquippedToolName().isEmpty();
    }

    /**Met à jour les informations de l'outil actuellement équipé (dont le currentEquippedToolName a été modifié)
     * Met à jour spriteY et size par rapport aux sprites de la tool
     * Appelé une fois que le {@code currentEquippedToolName} ait été modifié via {@code setActualToolPlayer()} par le wheel en question :
     * - Lorsque l'action ou la direction de l'entité change.
     * - Lorsque l'outil "préféré" pour une catégorie change, si cette catégorie est actuellement choisie par le joueur.
     * / Lorsque vous changez de catégorie d'outil choisie (setActualEquippedToolByFavoriteOfThisWheel() dans WheelManager)
     * - N'update pas si le joueur n'a pas d'arme en main
     * */


    public boolean updateEquippedToolInfo(){
        if(haveAToolEquipped) {
            int[] actualActionTool = null;//spriteY, size, beginX pour l'action_direction
            if (!getCurrentEquippedToolName().isEmpty()) {//Alors une arme est selectionné
                String actionD = character.stateComponant().getCurrentStateName() + "_" + character.stateComponant().getCurrentOrientation().getDirection();
                actualActionTool = getToolsspriteSheetData().getActionInfo(getCurrentEquippedToolName(), actionD);
                if (actualActionTool != null) {
                    currentToolSpriteY = actualActionTool[0];
                    currentToolSizeSprite = actualActionTool[1];
                    character.setCurrentBeginX(actualActionTool[2]);//?
                    System.out.println("update info tool");
                    //Met a jour le current hitZonePointDecallage et hitZoneLenght de l'arme actuelle
                    character.setCurrentHitZonePointDecallage(currentEquippedTool.hitZonePointDecallage());
                    character.setCurrentHitZoneLenght(currentEquippedTool.hitZoneLenght());
                    return true;
                }
            }else {
                haveAToolEquipped = false;
                currentEquippedTool = null;
            }
            System.out.println("L'outil " + getCurrentEquippedToolName() + " n'a pas de sprite pour l'action, ou il n'y a pas d'armes");
            //Pas d'arme ou pas de sprite pour l'action actuelle

            currentToolSpriteY = -1;
            currentToolSizeSprite = -1;
            character.setCurrentBeginX(0);
            character.setCurrentHitZonePointDecallage(character.hitZonePointDecallageDefault());
            character.setCurrentHitZoneLenght(character.hitZoneLenghtDefault());
        }
        return false;
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

    public void render(SpriteBatch batch){

        if(!currentEquippedToolName.isEmpty() && currentToolSizeSprite != -1){
            //Décallage de x et y si on a une sprite plus grande que celle du personnage
            int marge = (currentToolSizeSprite - 64);
            if(marge>0){marge/=2;}else{marge=0;}
            TextureRegion[][] textureReg= getTextureRegionTool(currentToolSizeSprite );
            batch.draw(textureReg[currentToolSpriteY][character.stateComponant().getCurrentState().getCurrentSpriteX()], character.getPosOnScreenX(Game.getInstance().getRegion().camera().position().x())-marge, character.getPosOnScreenY(Game.getInstance().getRegion().camera().position().y())-marge);
        }
    }



}
