package aura_game.app.rework;

import aura_game.app.ToolManager;
import aura_game.app.rework.TypeEnum.ActorEntityType;
import com.badlogic.gdx.math.Rectangle;


public class ToolWieldingEntity extends ActorEntity{


    ToolManager toolManager;

    public ToolWieldingEntity(ActorEntityType type, int posX, int posY) {
        super(type, posX, posY);
        this.toolManager = new ToolManager(this);

    }

    @Override
    public void hit(){
        //Rectangle currentHitZone = toolManager.getCurrentEquippedTool().zoneDegat(this);
        Rectangle currentHitZone = zoneDegatFromDirection(stateComponant().getCurrentOrientation().getDirection());
        for(AbstractEntity ent : physics().getCollidingObjectsInBothGrids(currentHitZone)){
            ent.hurt(damage());
            reduceDurabilityToTool();
        }

    }


    public ToolManager toolManager(){
        return toolManager;
    }


    public float currentDamage(){
        return toolManager.getCurrentEquippedTool().damage(); //todo ou default damage sinon
    }

    /**
     * Réduit la durabilité de l'outil actuellement équipé de 1.
     */
    public void reduceDurabilityToTool(){
            if(toolManager.haveAToolEquipped()) {
                toolManager.getCurrentEquippedTool().reduceDurability();//TODO stocker ici le nom de l'outil actuel ?
            }
    }





}
