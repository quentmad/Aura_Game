package aura_game.app.CraftableBlock;

import aura_game.app.LootStack;
import aura_game.app.rework.AbstractObject;
import aura_game.app.rework.BlockEntity;
import aura_game.app.rework.Region;
import aura_game.app.rework.TypeEnum.BlockEntityType;
import aura_game.app.rework.TypeEnum.LootableObjectType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;


/**Image aspect of the block the player want to craft (and place directly)*/
public class BlockPlan extends AbstractObject {

    /**List of the remaining loots to craft the block*/
    private List<LootStack> RemainingLoots;
    private BlockEntity block;
    private final int id;
    private boolean locationChoosen;
    private boolean locationPossible;
    private final Texture texturePlanColission;

    public BlockPlan(BlockEntityType type, int initPosX, int initPosY, String color, int id) {
        super(type.name(),type.imageWidth(), type.imageHeight(), type.contentImageWidth(), type.contentImageHeight(), type.offsetY(), "src/main/resources/Blocks/Craftable/"+type.name() + "_aspect.png", initPosX, initPosY);
        this.block = new BlockEntity(type, initPosX/32 +1, initPosY/32 + 1, color);
        posC().set(block.posC().x(), block.posC().y());

        this.texturePlanColission = new Texture("src/main/resources/Blocks/Craftable/"+type.name() + "_aspect_colission.png");
        this.id = id;
        this.locationChoosen = false;
        this.locationPossible = false;

    }


    public int id(){
        return id;
    }

    /**
     * Set to true if no colission, false otherwise
     * @param b
     */
    public void setLocationPossible(boolean b){
        locationPossible = b;
    }

    public boolean isLocationPossible() {
        return locationPossible;
    }

    public BlockEntity block(){
        return block;
    }

    public boolean isLocationChoosen(){
        return locationChoosen;
    }

    public void setLocationChoosen(boolean locationChoosen){
        this.locationChoosen = locationChoosen;
    }

    public void setRemainingLoots(List<LootStack> remainingLoots){
        this.RemainingLoots = remainingLoots;
    }

    public List<LootStack> getRemainingLoots(){
        return RemainingLoots;
    }

    /**
     * Retire un élément de la liste des loots restants si possible, sinon renvoie false
     * @param loot
     * @return
     */
    public boolean removeOneElementFromRemainingLoots(LootableObjectType loot){
        for(LootStack lootStack : RemainingLoots){
            if(lootStack.getLootType().equals(loot) && lootStack.getQuantity() > 0){
                lootStack.removeOneQuantity();
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(SpriteBatch batch, Region region){
        //System.out.println("Rendering block plan " + block.name()+ ", locationChoosen : "+ locationChoosen + " locationPossible : "+locationPossible);
        if(!locationChoosen){
            if(locationPossible){
                batch.draw(texture(), block.getPosOnScreenX(region.camera().position().x()), block.getPosOnScreenY(region.camera().position().y()));
            }else{
                batch.draw(texturePlanColission,  block.getPosOnScreenX(region.camera().position().x()), block.getPosOnScreenY(region.camera().position().y()));
                //System.out.println("Rendering block plan collision at " + block.getPosOnScreenX(region.camera().position().x()) + " " + block.getPosOnScreenY(region.camera().position().y()));
            }
        }else{
            batch.draw(texture(),  block.getPosOnScreenX(region.camera().position().x()), block.getPosOnScreenY(region.camera().position().y()));
        }

    }


    public boolean isRemainingLootsEmpty() {
        for(LootStack lootStack : RemainingLoots){
            if(lootStack.getQuantity() > 0){
                return false;
            }
        }
        return true;
    }

    public int getFirstNotEmptyRemainingLootIndex(){
        for(int i = 0; i < RemainingLoots.size(); i++){
            if(RemainingLoots.get(i).getQuantity() > 0){
                return i;
            }
        }
        return -1;
    }
}
